package com.mw.todo_mvvm_jetpack_reactive_sample.data.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

private const val COLLECTION_TASKS = "Tasks"

class FirebaseDataSource @Inject constructor(
    private val dbRef: FirebaseFirestore
) {

    private val tasksRef: CollectionReference = dbRef.collection(COLLECTION_TASKS)

    fun getAllTasks(){}

    fun createTask() {}

}
