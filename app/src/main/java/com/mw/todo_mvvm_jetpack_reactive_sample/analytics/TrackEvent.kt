package com.mw.todo_mvvm_jetpack_reactive_sample.analytics

sealed class TrackEvent(var eventName: String, var eventParams: Map<String, String>) {
    data class TodoItemAdded(val eventId: String): TrackEvent(TAG_TODO_ADDED, mapOf(PARAM_ID to eventId))
    companion object {
        const val TAG_TODO_ADDED = "TODO_ADDED"
        const val PARAM_ID = "ID"
    }
}
