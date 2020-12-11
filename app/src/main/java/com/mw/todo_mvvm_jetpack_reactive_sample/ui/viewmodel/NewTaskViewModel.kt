package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.CreateNewTaskUseCase

class NewTaskViewModel @ViewModelInject constructor(
    private val createNewTaskUseCase: CreateNewTaskUseCase
) : ViewModel() {

    // Two-way databinding
    // TODO: Model
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    fun createTask() {
        createNewTaskUseCase.createNewTask()
    }
}
