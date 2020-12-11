package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.GetTasksUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TasksNavigationDestination
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class TasksViewModel @ViewModelInject constructor(
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    private val _navigationDestination = SingleLiveEvent<TasksNavigationDestination>()
    val navigationDestination = _navigationDestination

    private val _tasksList = MutableLiveData<List<Task>>()
    val tasksList = _tasksList

    private val compositeDisposable = CompositeDisposable()

    init {
        observeTasks()
    }

    fun addNewTask() {
        navigationDestination.value = TasksNavigationDestination.NewTask
    }

    private fun observeTasks() {
        compositeDisposable.add(
            getTasksUseCase.observeTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Timber.d("Observing tasks: $it")
                    _tasksList.value = it
                }, {
                    Timber.e("Error observing tasks: $it")
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
