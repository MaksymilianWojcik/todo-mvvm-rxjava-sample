package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.EnableAnalyticsUseCase
import timber.log.Timber

class SettingsViewModel @ViewModelInject constructor(
    private val enableAnalyticsUseCase: EnableAnalyticsUseCase
) : ViewModel() {

    // two-way databinding
    val analyticsEnabled = MutableLiveData<Boolean>(false)

    fun enableAnalytics(enable: Boolean) {
        enableAnalyticsUseCase.enableAnalytics(enable)
    }
}
