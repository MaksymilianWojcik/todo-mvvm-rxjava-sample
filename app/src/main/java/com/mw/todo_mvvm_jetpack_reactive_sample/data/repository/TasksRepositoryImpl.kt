package com.mw.todo_mvvm_jetpack_reactive_sample.data.repository

import com.mw.todo_mvvm_jetpack_reactive_sample.data.source.FirebaseDataSource
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.TasksRepository
import javax.inject.Inject

class TasksRepositoryImpl  @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
) : TasksRepository {

    override fun getAllTasks() {
        return firebaseDataSource.getAllTasks()
    }

    override fun createNewTask() {
        return firebaseDataSource.createTask()
    }

}
