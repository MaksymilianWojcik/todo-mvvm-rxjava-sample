package com.mw.todo_mvvm_jetpack_reactive_sample.data.model

data class Task(
    var id: String? = null,
    var title: String = "",
    var description: String = "",
    @field:JvmField var isCompleted: Boolean = false
) {
    constructor() : this(null)
}
