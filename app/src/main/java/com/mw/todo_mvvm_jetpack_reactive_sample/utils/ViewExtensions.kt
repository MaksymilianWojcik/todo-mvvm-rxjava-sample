package com.mw.todo_mvvm_jetpack_reactive_sample.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar

/**
 * Shows a snackbar message
 */
fun View.setupSnackbar(
    lifecycleOwner: LifecycleOwner,
    snackbarEvent: SingleLiveEvent<Int>,
    timeLength: Int
) {
    snackbarEvent.observe(lifecycleOwner, Observer { stringRes ->
        Snackbar.make(this, context.getString(stringRes), timeLength).show()
    })
}
