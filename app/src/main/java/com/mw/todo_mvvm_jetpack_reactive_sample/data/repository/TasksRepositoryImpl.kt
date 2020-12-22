package com.mw.todo_mvvm_jetpack_reactive_sample.data.repository

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.data.source.FirebaseDataSource
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TasksRepositoryImpl  @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : TasksRepository {

    override fun getAllTasks(): Single<List<Task>>  = firebaseDataSource.getAllTasksSingle()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun observeTasks(): Observable<List<Task>> = firebaseDataSource.observeTasks()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun createNewTask(task: Task): Completable = firebaseDataSource.createTaskWithOfflineSyncingAndSetId(task)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun completeTask(task: Task): Completable = firebaseDataSource.completeTask(task)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun activateTask(task: Task): Completable = firebaseDataSource.activateTask(task)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun clearCompletedTasks(): Completable = firebaseDataSource.clearCompletedTasks()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())

    override fun removeTasks(tasks: List<Task>): Completable = firebaseDataSource.removeTasks(tasks)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
