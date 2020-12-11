package com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository

interface TasksRepository {
    fun getAllTasks()
    fun createNewTask()
}
