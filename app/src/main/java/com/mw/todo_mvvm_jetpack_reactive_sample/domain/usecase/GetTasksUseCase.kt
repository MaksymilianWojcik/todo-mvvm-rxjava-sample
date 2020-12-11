package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    fun observeTasks(): Observable<List<Task>> = repository.observeTasks()

    fun getAllTasks(): Single<List<Task>> = repository.getAllTasks()
}
