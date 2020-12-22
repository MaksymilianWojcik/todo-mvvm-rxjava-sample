package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository
    // TODO: error mapper
) {

    fun signInWithEmail(email: String, password: String) = authRepository.signInWithEmail(email, password)
}
