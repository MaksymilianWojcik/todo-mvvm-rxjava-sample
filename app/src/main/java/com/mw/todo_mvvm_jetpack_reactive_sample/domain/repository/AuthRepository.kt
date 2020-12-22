package com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository

import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.User
import io.reactivex.Completable
import io.reactivex.Observable

interface AuthRepository {
    fun signInWithEmail(email: String, password: String): Completable
    fun getSignedInUser(): User?
    fun signOut()
    fun observeAuthState(): Observable<Boolean>
}
