package com.mw.todo_mvvm_jetpack_reactive_sample.utils

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.ioToMain(): Observable<T> = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Flowable<T>.ioToMain(): Flowable<T> = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.ioToMain() = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun Completable.ioToMain() = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

fun TextInputEditText.textChanges() = Observable.create<String> { emitter ->
    val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            emitter.onNext(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }
    this.addTextChangedListener(textWatcher)
    emitter.setCancellable { this.removeTextChangedListener(textWatcher) }
}

fun <T> Observable<T>.continueOnError(): Observable<T> {
    return onErrorResumeNext { _: Throwable -> Observable.empty() }
}

fun <T> Flowable<T>.continueOnError(): Flowable<T> {
    return onErrorResumeNext { _: Throwable -> Flowable.empty() }
}

fun <T> Observable<T>.ignoreErrors(errorHandler: (Throwable) -> Unit) =
    retryWhen { errors ->
        errors
            .doOnNext { errorHandler(it) }
            .map { 0 }
    }
