package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.EnableAnalyticsUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.GetUserStateUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.SignInUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.SignOutUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

private const val MOCKED_TEST_EMAIL = "test@test.pl"
private const val MOCKED_TEST_PASSWORD = "test123"

class SettingsViewModel @ViewModelInject constructor(
    private val enableAnalyticsUseCase: EnableAnalyticsUseCase,
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val getUserStateUseCase: GetUserStateUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    // two-way databinding
    val analyticsEnabled = MutableLiveData<Boolean>(false)

    private val _signInOutButtonLabel = MutableLiveData<Int>(R.string.sign_in_label) // @StringRes
    val signInOutButtonLabel: LiveData<Int> = _signInOutButtonLabel

    init {
        analyticsEnabled.value = enableAnalyticsUseCase.isEnabled()
        compositeDisposable.add(
            getUserStateUseCase.observeAuthState()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isLoggedIn ->
                    if (isLoggedIn) {
                        _signInOutButtonLabel.value = R.string.sign_out_label
                    } else {
                        _signInOutButtonLabel.value = R.string.sign_in_label
                    }
                }, {
                    Timber.e("Error observing auth state")
                })
        )
    }

    fun enableAnalytics(enable: Boolean) {
        enableAnalyticsUseCase.enableAnalytics(enable)
    }

    fun performSignInOut() {
        if (getUserStateUseCase.isSignedIn()) {
            signOutUseCase.signOut()
        } else {
            compositeDisposable.add(
                signInUseCase.signInWithEmail(MOCKED_TEST_EMAIL, MOCKED_TEST_PASSWORD) // TODO: two-way databinding
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Timber.d("All good")
                    }, {
                        Timber.d("Error")
                    })
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
