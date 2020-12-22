package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.ClearCompletedTasksUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.GetTasksUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.UpdateTaskStatusUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.UpdateTaskStatusUseCase.TaskStatus
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TaskFilterType
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TasksNavigationDestination
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

private const val KEY_TASKS_FILTER_SAVED_STATE = "keyTasksFilter"

class TasksViewModel @ViewModelInject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val clearCompletedTasksUseCase: ClearCompletedTasksUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _navigationDestination = SingleLiveEvent<TasksNavigationDestination>()
    val navigationDestination = _navigationDestination

    // holds all tasks
    private val _tasksList = MutableLiveData<List<Task>>()

    // holds filtered tasks, used to display content in adapter
    private val _filteredTasksList = MutableLiveData<List<Task>>(_tasksList.value)
    val filteredTasksList: LiveData<List<Task>> = _filteredTasksList

    private val compositeDisposable = CompositeDisposable()

    init {
        observeTasks()
        setFilters(getSavedFilterType())
    }

    fun setFilters(taskFilterType: TaskFilterType) {
        savedStateHandle.set(KEY_TASKS_FILTER_SAVED_STATE, taskFilterType)
        when (taskFilterType) {
            TaskFilterType.ALL_TASKS -> {
                _filteredTasksList.value = filterTasks(_tasksList.value ?: emptyList(), TaskFilterType.ALL_TASKS)
            }
            TaskFilterType.ACTIVE_TASKS -> {
                _filteredTasksList.value = filterTasks(_tasksList.value ?: emptyList(), TaskFilterType.ACTIVE_TASKS)
            }
            TaskFilterType.COMPLETED_TASKS -> {
                _filteredTasksList.value = filterTasks(_tasksList.value ?: emptyList(), TaskFilterType.COMPLETED_TASKS)
            }
        }
    }

    fun addNewTask() {
        navigationDestination.value = TasksNavigationDestination.NewTask
    }

    fun changeTaskStatus(task: Task, isCompleted: Boolean) {
        val status = if (isCompleted) TaskStatus.Completed else TaskStatus.Active
        compositeDisposable.add(
            updateTaskStatusUseCase.updateTaskStatus(task, status)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Update task")
                }, {
                    Timber.d("Error: $it")
                })
        )
    }

    fun clearCompletedTasks() {
        val completedTasks = _tasksList.value?.filter { it.isCompleted }.orEmpty()
        compositeDisposable.add(
            clearCompletedTasksUseCase.clearTasks(completedTasks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Cleared completed tasks")
                }, {
                    Timber.e("Error clearing completed tasks: $it")
                })
        )
    }

    fun clearActiveTasks() {
        val activeTasks = _tasksList.value?.filter { it.isCompleted.not() }.orEmpty()
        compositeDisposable.add(
            clearCompletedTasksUseCase.clearTasks(activeTasks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Cleared completed tasks")
                }, {
                    Timber.e("Error clearing completed tasks: $it")
                })
        )
    }

    private fun observeTasks() {
        compositeDisposable.add(
            getTasksUseCase.observeTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Observing tasks: $it")
                    _tasksList.value = it
                    _filteredTasksList.value = filterTasks(it, getSavedFilterType())
                }, {
                    Timber.e("Error observing tasks: $it")
                })
        )
    }

    private fun getSavedFilterType() : TaskFilterType {
        return savedStateHandle.get(KEY_TASKS_FILTER_SAVED_STATE) ?: TaskFilterType.ALL_TASKS
    }

    private fun filterTasks(tasksList: List<Task>, filterType: TaskFilterType): List<Task> {
        val tasksToShow = ArrayList<Task>()
        for (task in tasksList) {
            when (filterType) {
                TaskFilterType.ALL_TASKS -> tasksToShow.add(task)
                TaskFilterType.ACTIVE_TASKS -> if (task.isCompleted.not()) tasksToShow.add(task)
                TaskFilterType.COMPLETED_TASKS -> if (task.isCompleted) tasksToShow.add(task)
            }
        }
        return tasksToShow
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
