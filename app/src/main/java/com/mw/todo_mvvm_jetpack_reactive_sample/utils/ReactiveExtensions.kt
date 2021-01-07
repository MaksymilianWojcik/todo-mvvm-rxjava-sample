package com.mw.todo_mvvm_jetpack_reactive_sample.utils

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
