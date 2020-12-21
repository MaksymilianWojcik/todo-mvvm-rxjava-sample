package com.mw.todo_mvvm_jetpack_reactive_sample

import android.app.Application
import com.mw.todo_mvvm_jetpack_reactive_sample.analytics.AnalyticsTracker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @Inject
    lateinit var analyticsTracker: AnalyticsTracker

    override fun onCreate() {
        super.onCreate()
        AppLogger(analyticsTracker)
    }
}
