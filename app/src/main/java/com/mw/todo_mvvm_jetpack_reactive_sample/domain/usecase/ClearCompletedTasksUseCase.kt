package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import javax.inject.Inject

class ClearCompletedTasksUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    fun clearTasks() = repository.clearCompletedTasks()

    fun clearTasks(tasks: List<Task>) = repository.removeTasks(tasks)
}
