package com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.CreateNewTaskUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.SingleLiveEvent
import com.mw.todo_mvvm_jetpack_reactive_sample.utils.validateSources
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

    private val _closeScreen = MutableLiveData<Unit>()
    val closeScreen = _closeScreen

    // Two-way databinding
    // TODO: Model
    val title = MutableLiveData<String>()
    val description = MutableLiveData<String>()

    // validation
    val isFormValid = validateSources(
        title.map { it.isNullOrBlank().not() },
        description.map { it.isNullOrBlank().not() }
    )

    fun createTask() {
        _loadingData.value = true
        val isFormValid = isFormValid.value ?: false
        if (isFormValid) {
            val task = Task(
                title = title.value!!,
                description = description.value!!
            )
            compositeDisposable.add(
                createNewTaskUseCase(task)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        Timber.d("Created new meeting")
                        // TODO: UIState example, although its not necessary in our example
                        _loadingData.value = false
                        _snackbarMessage.value = R.string.task_added_message
                        _closeScreen.value = Unit
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
