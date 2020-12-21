package com.mw.todo_mvvm_jetpack_reactive_sample.data.store

import android.content.SharedPreferences
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.whenNotNull
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SharedPreferencesListener @Inject constructor(): SharedPreferences.OnSharedPreferenceChangeListener {

    private val listenerSubject = PublishSubject.create<Pair<String, Any>>()

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        whenNotNull(sharedPreferences.all[key]) { value ->
            listenerSubject.onNext(key to value)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <TYPE> observe(key: String): Observable<TYPE> = listenerSubject
        .filter { keyValuePair -> keyValuePair.key() == key }
        .map { it.value() as TYPE }
        .distinctUntilChanged()

    private fun Pair<String, Any>.key() = this.first
    private fun Pair<String, Any>.value() = this.second
}
