package com.mw.todo_mvvm_jetpack_reactive_sample

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.mw.todo_mvvm_jetpack_reactive_sample.analytics.AnalyticsTracker
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppLogger @Inject constructor(
    analyticsTracker: AnalyticsTracker
) {

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(AppLoggerDebug())
        } else {
            Timber.plant(AppLoggerRelease(analyticsTracker))
        }
    }

    private class AppLoggerDebug : Timber.DebugTree() {
        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
             // Workaround for devices that doesn't show lower priority logs
//            var priorityCopy = priority
//        if (Build.MANUFACTURER.equals("HUAWEI") || Build.MANUFACTURER.equals("samsung")) {
//            if (priority == Log.VERBOSE || priority == Log.DEBUG || priority == Log.INFO)
//                priorityCopy = Log.ERROR;
//        }
            super.log(priority, tag, message, throwable)
        }

        override fun createStackElementTag(element: StackTraceElement): String? {
            // Add line number to the log
            return super.createStackElementTag(element) + " (line " + element.lineNumber + ")"
        }
    }

    private class AppLoggerRelease(private val analyticsTracker: AnalyticsTracker) : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
            super.log(priority, tag, message, throwable)
            analyticsTracker.trackException(priority, tag, message, throwable)
        }
        override fun isLoggable(tag: String?, priority: Int): Boolean = priority == Log.ERROR
    }
}
