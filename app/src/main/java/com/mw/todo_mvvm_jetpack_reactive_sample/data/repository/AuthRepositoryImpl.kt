package com.mw.todo_mvvm_jetpack_reactive_sample.data.repository

import com.mw.todo_mvvm_jetpack_reactive_sample.data.source.FirebaseAuthSource
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.AuthRepository
import io.reactivex.Completable
import io.reactivex.Observable
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthSource: FirebaseAuthSource
): AuthRepository {

    override fun signInWithEmail(email: String, password: String): Completable {
            return firebaseAuthSource.signInWithEmail(email, password)
        }

    override fun getSignedInUser() = firebaseAuthSource.getUser()

    override fun signOut() = firebaseAuthSource.signOut()

    override fun observeAuthState(): Observable<Boolean> = firebaseAuthSource.observeAuthState() // TODO: Mapping for AuthState
}
