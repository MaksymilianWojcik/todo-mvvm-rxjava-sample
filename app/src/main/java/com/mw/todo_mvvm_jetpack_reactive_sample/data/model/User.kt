package com.mw.todo_mvvm_jetpack_reactive_sample.data.model

data class User(
    val id: String? = null,
    val email: String = "",
    val name: String = ""
) {
    val isLoggedIn = id != null
}
