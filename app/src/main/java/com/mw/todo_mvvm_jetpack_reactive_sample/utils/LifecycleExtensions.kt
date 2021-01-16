package com.mw.todo_mvvm_jetpack_reactive_sample.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

fun validateSources(vararg liveDatas: LiveData<Boolean>): LiveData<Boolean> {
    return MediatorLiveData<Boolean>().also { mediatorLiveData ->
        // check current values at the moment of declaration
        mediatorLiveData.value = liveDatas.all {
            it.value == true
        }

        // observe source liveDatas
        for (source in liveDatas) {
            mediatorLiveData.addSource(source) { valid ->
                var isValid = valid
                // if source emits true
                if (isValid) {
                    // than for every source added to mediatorLiveData
                    for (liveData in liveDatas) {
                        // if it is not current source
                        if (liveData != source) {
                            // and if its value is not true
                            if (liveData.value != true) {
                                // set isValid to false
                                isValid = false
                                break
                            }
                        }
                    }
                }
                mediatorLiveData.value = isValid
            }
        }
    }
}
