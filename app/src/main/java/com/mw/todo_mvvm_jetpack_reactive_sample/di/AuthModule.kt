package com.mw.todo_mvvm_jetpack_reactive_sample.di

import com.mw.todo_mvvm_jetpack_reactive_sample.data.repository.AuthRepositoryImpl
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
abstract class AuthModule {

    @Binds
    @ActivityRetainedScoped
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

}
