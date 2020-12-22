package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserStateUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    fun isSignedIn() = authRepository.getSignedInUser()?.isLoggedIn ?: false

    fun getSignedInUser() = authRepository.getSignedInUser()

    fun observeAuthState() = authRepository.observeAuthState()
}
