package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import io.reactivex.Completable
import javax.inject.Inject

class CreateNewTaskUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    private fun createNewTask(task: Task): Completable {
        return repository.createNewTask(task)
    }

    operator fun invoke(task: Task) = createNewTask(task)
}
