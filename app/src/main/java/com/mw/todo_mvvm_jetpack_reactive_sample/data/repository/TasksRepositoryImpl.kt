package com.mw.todo_mvvm_jetpack_reactive_sample.data.repository

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.data.source.FirebaseDataSource
import com.mw.todo_mvvm_jetpack_reactive_sample.data.source.FirebaseErrorMapper
import com.mw.todo_mvvm_jetpack_reactive_sample.data.source.applyErrorMapper
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource,
    private val errorMapper: FirebaseErrorMapper
) : TasksRepository {

    override fun getAllTasks(): Single<List<Task>> = firebaseDataSource.getAllTasksSingle().applyErrorMapper(errorMapper)

    override fun observeTasks(): Observable<List<Task>> = firebaseDataSource.observeTasks()

    override fun completeTask(task: Task): Completable = firebaseDataSource.completeTask(task).applyErrorMapper(errorMapper)

    override fun activateTask(task: Task): Completable = firebaseDataSource.activateTask(task).applyErrorMapper(errorMapper)

    override fun clearCompletedTasks(): Completable = firebaseDataSource.clearCompletedTasks().applyErrorMapper(errorMapper)

    override fun createNewTask(task: Task): Completable =
        firebaseDataSource.createTaskWithOfflineSyncingAndSetId(task).applyErrorMapper(errorMapper)

    override fun removeTasks(tasks: List<Task>): Completable =
        firebaseDataSource.removeTasks(tasks).applyErrorMapper(errorMapper)
}
