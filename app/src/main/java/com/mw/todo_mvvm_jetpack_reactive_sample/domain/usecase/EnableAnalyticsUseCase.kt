package com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase

import com.mw.todo_mvvm_jetpack_reactive_sample.analytics.AnalyticsTracker
import javax.inject.Inject

class EnableAnalyticsUseCase @Inject constructor(
    private val analyticsTracker: AnalyticsTracker
) {
    fun enableAnalytics(enable: Boolean) {
        analyticsTracker.setTrackingAndCrashlyticsEnabled(enable)
    }

}
