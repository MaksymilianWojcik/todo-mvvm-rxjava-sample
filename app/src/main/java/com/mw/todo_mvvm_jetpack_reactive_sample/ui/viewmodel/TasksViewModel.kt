package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.model.TasksNavigationDestination
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.SingleLiveEvent

class TasksViewModel @ViewModelInject constructor() : ViewModel() {

    private val _navigationDestination = SingleLiveEvent<TasksNavigationDestination>()
    val navigationDestination = _navigationDestination

    fun addNewTask() {
        navigationDestination.value = TasksNavigationDestination.NewTask
    }
}
