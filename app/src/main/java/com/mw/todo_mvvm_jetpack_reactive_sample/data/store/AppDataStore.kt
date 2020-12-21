package com.mw.todo_mvvm_jetpack_reactive_sample.data.store

import android.annotation.SuppressLint
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppDataStore @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val sharedPreferencesListener: SharedPreferencesListener
) {

    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferencesListener)
    }

    fun getString(key: String, default: String): String {
        return sharedPreferences.getString(key, default).orEmpty()
    }

    fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String, default: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, default)
    }

    fun getStringSet(
        key: String, default: Set<String> =
            emptySet()
    ): MutableSet<String>? = sharedPreferences.getStringSet(key, default)

    fun putStringSet(key: String, value: Set<String>) = sharedPreferences.edit().putStringSet(key, value).apply()

    fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    fun <TYPE> observe(key: String) = sharedPreferencesListener.observe<TYPE>(key)

    /**
     * This method will flush all the pending changes waiting to be written in the shared preferences file.
     * It will also remove the shared preferences listener.
     */
    @SuppressLint("ApplySharedPref")
    fun close() {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(sharedPreferencesListener)
        sharedPreferences.edit().commit()
    }
}
