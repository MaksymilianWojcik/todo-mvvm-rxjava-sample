package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import io.reactivex.Completable
import javax.inject.Inject

class UpdateTaskStatusUseCase @Inject constructor(
    private val repository: TasksRepository
) {

    fun updateTaskStatus(task: Task, taskStatus: TaskStatus): Completable {
        return when (taskStatus) {
            TaskStatus.Completed -> repository.completeTask(task)
            TaskStatus.Active -> repository.activateTask(task)
        }
    }

    sealed class TaskStatus {
        object Completed: TaskStatus()
        object Active: TaskStatus()
    }
}
