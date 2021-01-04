package com.mw.todo_mvvm_jetpack_reactive_sample.utils

import android.content.Context
import androidx.navigation.NavController
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

typealias NavigationCommand = (NavController).(Context) -> Unit

/**
 * Helper class for navigation, so we don't need to add LiveData's / SingleLiveEvents / Events
 * in every viewModel and observer them in Fragments where we want to use navigation
 */
@ActivityRetainedScoped
class NavigationDispatcher @Inject constructor() {

    private val navigationEmitter = SingleLiveEvent<NavigationCommand>()
    val navigationCommands: SingleLiveEvent<NavigationCommand> = navigationEmitter

    fun emit(navigationCommand: NavigationCommand) {
        navigationEmitter.value = navigationCommand
    }
}
