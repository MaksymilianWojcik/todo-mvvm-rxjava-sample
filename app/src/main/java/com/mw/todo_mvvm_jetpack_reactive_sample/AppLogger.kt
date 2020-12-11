package com.mw.todo_mvvm_jetpack_reactive_sample

import android.util.Log
import timber.log.Timber

class AppLogger {

    init {
        if (BuildConfig.DEBUG) {
            Timber.plant(AppLoggerDebug())
        } else {
            Timber.plant(AppLoggerRelease())
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

    private class AppLoggerRelease : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
            super.log(priority, tag, message, throwable)
            // TODO: crashlytics
        }
        override fun isLoggable(tag: String?, priority: Int): Boolean = priority == Log.ERROR
    }
}
