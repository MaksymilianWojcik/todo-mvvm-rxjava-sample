package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.databinding.ObservableField
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.ClearCompletedTasksUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.GetTasksUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.UpdateTaskStatusUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.UpdateTaskStatusUseCase.TaskStatus
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.fragment.TasksFragmentDirections
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TaskFilterType
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TaskSortingType
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TasksNavigationDestination
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.NavigationDispatcher
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

private const val KEY_TASKS_FILTER_SAVED_STATE = "keyTasksFilter"
private const val KEY_TASKS_SORT_SAVED_STATE = "keyTasksSorting"

class TasksViewModel @ViewModelInject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val clearCompletedTasksUseCase: ClearCompletedTasksUseCase,
    private val navigationDispatcher: NavigationDispatcher,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    // for <include> in fragment_tasks:
//    val testMessage = MutableLiveData<String>("123")
//    val testString: ObservableField<String> = ObservableField("12345")

    // all tasks that are retrieved from firestore
    private val _allTasks = MutableLiveData<List<Task>>(emptyList()) // or .apply { value = emptyList() }

    // retrieve filterType from savedStateHandle, ALL_TASKS is an initial value
    private val _filterType: MutableLiveData<TaskFilterType> =
        savedStateHandle.getLiveData<TaskFilterType>(KEY_TASKS_FILTER_SAVED_STATE, TaskFilterType.ALL_TASKS)

    private val _sortType: MutableLiveData<TaskSortingType> =
        savedStateHandle.getLiveData(KEY_TASKS_SORT_SAVED_STATE, TaskSortingType.ASCENDING)

    // TODO: Refactor this later with MediatorLiveData
    // tasks that are showed in recyclerview
    private val _tasks = _allTasks.distinctUntilChanged().switchMap { tasks ->
        _filterType.distinctUntilChanged().switchMap { filter ->
            filterTasks(tasks, filter).switchMap { filtered -> // this will be invoked on _filterType change and than down stream
                _sortType.distinctUntilChanged().switchMap { sortType ->
                    sortTasks(filtered, sortType) // only this will be invoked when _sortType value changes
                }
            }
        }
    }
    val tasks: LiveData<List<Task>> = _tasks

    // example of transformation, to show empty items background
    val empty: LiveData<Boolean> = Transformations.map(_tasks) { it.isEmpty() }

    init {
        observeTasks()
    }

    fun addNewTask() {
        navigationDispatcher.emit { navController ->
            // TODO: Pass id for task edit and pass null for new task when clicked, not when addNewTask
            navController.navigate(TasksFragmentDirections.actionTasksFragmentToNewTaskFragment())
        }
    }

    fun setFilters(taskFilterType: TaskFilterType) {
        // we don't need to set _filterType.value here, as savedStateHandle will update it via LiveData
        savedStateHandle.set(KEY_TASKS_FILTER_SAVED_STATE, taskFilterType)
    }

    fun setSorting(taskSortingType: TaskSortingType) {
        savedStateHandle[KEY_TASKS_SORT_SAVED_STATE] = taskSortingType
    }

    fun changeTaskStatus(task: Task, isCompleted: Boolean) {
        val status = if (isCompleted) TaskStatus.Completed else TaskStatus.Active
        compositeDisposable.add(
            updateTaskStatusUseCase.updateTaskStatus(task, status)
                .subscribe({
                    Timber.d("Update task")
                }, {
                    Timber.d("Error: $it")
                })
        )
    }

    fun clearCompletedTasks() {
        val completedTasks = _allTasks.value?.filter { it.isCompleted }.orEmpty()
        compositeDisposable.add(
            clearCompletedTasksUseCase.clearTasks(completedTasks)
                .subscribe({
                    Timber.d("Cleared completed tasks")
                }, {
                    Timber.e("Error clearing completed tasks: $it")
                })
        )
    }

    fun clearActiveTasks() {
        val activeTasks = _allTasks.value?.filter { it.isCompleted.not() }.orEmpty()
        compositeDisposable.add(
            clearCompletedTasksUseCase.clearTasks(activeTasks) // TODO: Rename this UseCase
                .subscribe({
                    Timber.d("Cleared completed tasks")
                }, {
                    Timber.e("Error clearing completed tasks: $it")
                })
        )
    }

    private fun observeTasks() {
        compositeDisposable.add(
            getTasksUseCase()
                .subscribe({
                    Timber.d("Observing tasks: $it")
                    _allTasks.value = it
                }, {
                    // TODO: Handle showing error as a background
                    Timber.e("Error observing tasks: $it")
                })
        )
    }

    private fun filterTasks(taskList: List<Task>, filterType: TaskFilterType): LiveData<List<Task>> {
        val result = MutableLiveData<List<Task>>()
        when (filterType) {
            TaskFilterType.ALL_TASKS -> result.value = taskList
            TaskFilterType.ACTIVE_TASKS -> result.value = taskList.filter { it.isCompleted.not() }
            TaskFilterType.COMPLETED_TASKS -> result.value = taskList.filter { it.isCompleted }
        }
        return result
    }

    private fun sortTasks(tasksList: List<Task>, sortType: TaskSortingType): LiveData<List<Task>> {
        return MutableLiveData<List<Task>>(when (sortType) {
            TaskSortingType.ASCENDING -> tasksList.sortedBy { it.title }
            TaskSortingType.DESCENDING -> tasksList.sortedByDescending { it.title }
        })
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
