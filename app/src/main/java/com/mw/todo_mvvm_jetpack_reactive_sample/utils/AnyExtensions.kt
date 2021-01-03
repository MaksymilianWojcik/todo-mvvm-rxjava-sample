package com.mw.todo_mvvm_jetpack_reactive_sample.utils

/**
 * invokes [callback] if [input] is not null
 */
inline fun <T : Any, R> whenNotNull(input: T?, callback: (T) -> R): R? {
    return input?.let(callback)
}
