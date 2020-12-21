package com.mw.todo_mvvm_jetpack_reactive_sample.analytics

import android.app.Activity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import javax.inject.Singleton

private const val CRASHLYTICS_KEY_PRIORITY = "CRASH_PRIORITY"
private const val CRASHLYTICS_KEY_TAG = "CRASH_TAG"

@Singleton
class AnalyticsTracker @Inject constructor(
    private val firebaseAnalytics: FirebaseAnalytics,
    private val firebaseCrashlytics: FirebaseCrashlytics
) {

    // TODO: Handle consent - shared prefs
    var isEnabled: Boolean = true

    fun setTrackingAndCrashlyticsEnabled(isEnabled: Boolean) {
        // TODO: update shared prefs
        firebaseAnalytics.setAnalyticsCollectionEnabled(isEnabled)
        firebaseCrashlytics.setCrashlyticsCollectionEnabled(isEnabled)
    }

    fun shouldAskForUserConsent(): Boolean = false

    fun trackScreen(activity: Activity, screenName: String) {
        if (isEnabled.not()) return
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, activity::class.java.simpleName)
        }
    }

    fun trackEvent(trackEvent: TrackEvent) {
        if (isEnabled.not()) return
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
        if (isEnabled.not()) return
        firebaseCrashlytics.apply {
            setCustomKey(CRASHLYTICS_KEY_PRIORITY, priority)
            setCustomKey(CRASHLYTICS_KEY_TAG, tag ?: "")
            // To give yourself more context for the events leading up to a crash, you can add custom Crashlytics logs to your app.
            // Crashlytics associates the logs with your crash data and displays them in the Crashlytics page under the Logs tab
            log(message)
            throwable?.let { recordException(it) }
        }
    }
}
