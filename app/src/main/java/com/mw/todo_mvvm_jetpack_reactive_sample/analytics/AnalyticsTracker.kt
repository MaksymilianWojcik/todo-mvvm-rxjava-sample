package com.mw.todo_mvvm_jetpack_reactive_sample.analytics

import android.app.Activity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.mw.todo_mvvm_jetpack_reactive_sample.data.store.AppDataStore
import javax.inject.Inject
import javax.inject.Singleton

private const val CRASHLYTICS_KEY_PRIORITY = "CRASH_PRIORITY"
private const val CRASHLYTICS_KEY_TAG = "CRASH_TAG"
private const val KEY_ANALYTICS_PREFERENCES = "analyticsKey"
private const val CONSENT_DIALOG_ENABLED = false

@Singleton
class AnalyticsTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics,
    private val appDataStore: AppDataStore // we could do separate store for settings here
) {

    fun isEnabled(): Boolean = appDataStore.getBoolean(KEY_ANALYTICS_PREFERENCES, false)

    fun setTrackingAndCrashlyticsEnabled(isEnabled: Boolean) {
        appDataStore.putBoolean(KEY_ANALYTICS_PREFERENCES, isEnabled)
        firebaseAnalytics.setAnalyticsCollectionEnabled(isEnabled)
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(isEnabled)
    }

    // TODO: Show dialog asking for user consent in the future
    fun shouldAskForUserConsent(): Boolean = CONSENT_DIALOG_ENABLED

    fun trackScreen(activity: Activity, screenName: String) {
        if (isEnabled().not()) return
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, activity::class.java.simpleName)
        }
    }

    fun trackEvent(trackEvent: TrackEvent) {
        if (isEnabled().not()) return
        // val (eventName, eventParams) = trackEvent
        firebaseAnalytics.logEvent(trackEvent.eventName) {
            param(trackEvent.eventName, trackEvent.eventName)
            trackEvent.eventParams.forEach { param(it.key, it.value) }
        }
    }

    fun trackException(
        priority: Int,
        tag: String?,
        message: String,
        throwable: Throwable?
    ) {
        if (isEnabled().not()) return
        firebaseCrashlytics.apply {
            setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)
            setCustomKey(CRASHLYTICS_KEY_TAG, tag ?: "")
            // To give yourself more context for the events leading to a crash, you can add custom Crashlytics logs to your app
            // Crashlytics associates the logs with your crash data and displays them in the Crashlytics page under the Logs tab
            log(message)
            throwable?.let { recordException(it) }
        }
    }
}
