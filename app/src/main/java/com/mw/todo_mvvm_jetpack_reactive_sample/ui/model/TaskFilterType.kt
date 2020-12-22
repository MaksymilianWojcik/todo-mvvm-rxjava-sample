package com.mw.todo_mvvm_jetpack_reactive_sample.ui.model

// cant be sealed class or object to be able to save it in SavedStateHandle
enum class TaskFilterType {
    ALL_TASKS, ACTIVE_TASKS, COMPLETED_TASKS
}
