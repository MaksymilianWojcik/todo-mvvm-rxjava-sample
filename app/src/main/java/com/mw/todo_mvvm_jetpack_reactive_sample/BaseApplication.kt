package com.mw.todo_mvvm_jetpack_reactive_sample

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppLogger()
    }
}
