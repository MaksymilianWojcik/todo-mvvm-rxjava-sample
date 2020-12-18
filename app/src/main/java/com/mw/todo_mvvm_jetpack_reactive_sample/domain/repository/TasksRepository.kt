package com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface TasksRepository {
    fun getAllTasks(): Single<List<Task>>
    fun observeTasks(): Observable<List<Task>>
    fun createNewTask(task: Task): Completable
    fun completeTask(task: Task): Completable
    fun activateTask(task: Task): Completable
}
