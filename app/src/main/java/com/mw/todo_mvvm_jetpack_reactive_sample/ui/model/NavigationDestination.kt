package com.mw.todo_mvvm_jetpack_reactive_sample.ui.model

sealed class TasksNavigationDestination {
    object NewTask : TasksNavigationDestination()
}
