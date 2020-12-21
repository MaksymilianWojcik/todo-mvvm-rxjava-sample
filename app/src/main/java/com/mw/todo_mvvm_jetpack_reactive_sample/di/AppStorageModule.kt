package com.mw.todo_mvvm_jetpack_reactive_sample.di

import android.content.Context
import android.content.SharedPreferences
import com.mw.todo_mvvm_jetpack_reactive_sample.data.store.AppDataStore
import com.mw.todo_mvvm_jetpack_reactive_sample.data.store.SharedPreferencesListener
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

private const val SHARED_PREFERENCES_FILE_NAME = "letsPlayAndroid"

@Module
@InstallIn(ApplicationComponent::class)
object AppStorageModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAppDataStore(sharedPreferences: SharedPreferences, listener: SharedPreferencesListener): AppDataStore {
        return AppDataStore(sharedPreferences, listener)
    }
}
