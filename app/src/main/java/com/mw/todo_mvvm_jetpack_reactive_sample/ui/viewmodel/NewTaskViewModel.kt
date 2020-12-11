package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.CreateNewTaskUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.SingleLiveEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class NewTaskViewModel @ViewModelInject constructor(
    private val createNewTaskUseCase: CreateNewTaskUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val _loadingData = MutableLiveData<Boolean>()
    val loadingData: LiveData<Boolean> = _loadingData

    private val _snackbarMessage = SingleLiveEvent<Int>()
    val snackbarMessage = _snackbarMessage

    // Two-way databinding
    // TODO: Model
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    fun createTask() {
        _loadingData.value = true
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
                        _loadingData.value = false
                        _snackbarMessage.value = R.string.task_added_message
                    }, {
                        _loadingData.value = false
                        Timber.e("Failed to create new meeting: $it")
                        _snackbarMessage.value = R.string.error_failed_to_save_task
                    })
            )
        } else {
            _loadingData.value = false
            _snackbarMessage.value = R.string.error_empty_task
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}
