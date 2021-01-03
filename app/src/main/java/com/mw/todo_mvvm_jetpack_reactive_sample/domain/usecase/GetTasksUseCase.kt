package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val repository: TasksRepository
) {
    private fun observeTasks(): Observable<List<Task>> = repository.observeTasks()

    // TODO: Stop observing tasks and retrieve data as Single. Add SwipeRefresh for that.
    //  Also handle filtering and sorting here
    fun getAllTasks(): Single<List<Task>> = repository.getAllTasks()

    // TODO: Refactor all usecases to use invoke() operator when they have only one function
    operator fun invoke() = observeTasks()
}
