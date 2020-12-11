package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.CreateNewTaskUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NewTaskViewModel @ViewModelInject constructor(
    private val createNewTaskUseCase: CreateNewTaskUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    // Two-way databinding
    // TODO: Model
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    fun createTask() {
        val currentTitle = title.value
        val currentDescription = description.value
        if (currentTitle != null && currentDescription != null) {
            val task = Task(
                title = currentTitle,
                description = currentDescription
            )
            compositeDisposable.add(
                createNewTaskUseCase.createNewTask(task)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Timber.d("Created new meeting")
                    }, {
                        Timber.e("Failed to create new meeting: $it")
                    })
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
