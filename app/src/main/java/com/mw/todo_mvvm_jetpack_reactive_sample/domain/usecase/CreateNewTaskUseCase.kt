package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import javax.inject.Inject

class CreateNewTaskUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    fun createNewTask() {
        return repository.createNewTask()
    }
}
