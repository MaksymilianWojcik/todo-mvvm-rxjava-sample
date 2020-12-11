package com.mw.todo_mvvm_jetpack_reactive_sample.data.repository

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.data.source.FirebaseDataSource
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class TasksRepositoryImpl  @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : TasksRepository {

    override fun getAllTasks(): Single<List<Task>>  = firebaseDataSource.getAllTasksSingle()

    override fun observeTasks(): Observable<List<Task>> = firebaseDataSource.observeTasks()

    override fun createNewTask(task: Task): Completable = firebaseDataSource.createTaskWithOfflineSyncingAndSetId(task)
}
