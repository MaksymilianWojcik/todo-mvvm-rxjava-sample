package com.mw.todo_mvvm_jetpack_reactive_sample.data

import android.os.Build
import com.mw.todo_mvvm_jetpack_reactive_sample.BuildConfig
import java.util.*
import javax.inject.Inject

class AppInfoDataProvider @Inject constructor() {

    private fun osVersionRelease(): String? = Build.VERSION.RELEASE

    private fun versionSdkInt(): Int = Build.VERSION.SDK_INT

    /**
     * Get the application version
     */
    fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    /**
     * Get the OS Version in the format "VERSION_CODE - VERSION_NAME", eg "9 - P"
     */
    fun getOSVersion(): String {
        return listOfNotNull(
            osVersionRelease(),
            getOSCodeName().takeIf { it.isNotEmpty() }
        ).joinToString(" - ")
    }

    /**
     * Get the device info in the format "MANUFACTURER MODEL", eg "HTC M9"
     */
    fun getDeviceInfo(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val defaultLocale = Locale.getDefault()
        return if (model.startsWith(prefix = manufacturer, ignoreCase = true)) {
            // if model already includes manufacturer, don't include it in name
            model.toUpperCase(defaultLocale)
        } else {
            "$manufacturer $model".toUpperCase(defaultLocale)
        }
    }

    /**
     * Get the application build number, eg 3421
     */
    fun getBuildNumber(): String {
        return BuildConfig.VERSION_CODE.toString()
    }

    private fun getOSCodeName(): String {
        // gather all the fields in Build.VERSION_CODES
        val versions = Build.VERSION_CODES::class.java.fields

        // get the OS code name using the field name from Build.VERSION_CODES
        val getOSCodeName: () -> String = {
            versions.firstOrNull { version ->
                // find the first field that matches the current Build.VERSION.SDK_INT
                val versionSDK = version.getInt(Build.VERSION_CODES::class)
                versionSDK == versionSdkInt()
            }?.name ?: "" // get the field name (eg JELLY_BEAN_MR1)
        }

        // run safely the function and return empty if it fails for any reason
        return runCatching(getOSCodeName).getOrDefault("")
    }
}
