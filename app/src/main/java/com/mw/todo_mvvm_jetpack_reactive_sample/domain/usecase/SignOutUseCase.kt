package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun signOut() = authRepository.signOut()
}
