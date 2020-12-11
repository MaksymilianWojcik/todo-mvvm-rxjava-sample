package com.mw.todo_mvvm_jetpack_reactive_sample.data.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

private const val COLLECTION_TASKS = "Tasks"

class FirebaseDataSource @Inject constructor(private val dbRef: FirebaseFirestore) {

    private val tasksRef: CollectionReference = dbRef.collection(COLLECTION_TASKS)

    /**
     * Returns task list
     */
    fun getAllTasksSingle(): Single<List<Task>> {
        return Single.create {
            tasksRef.get().addOnSuccessListener { result ->
                val tasksList = result.toObjects(Task::class.java)
                it.onSuccess(tasksList)
            }.addOnFailureListener { e ->
                Timber.e("Error getting tasks: $e")
                it.onError(e)
            }
        }
    }

    /**
     * Returns task list observable with real time firestore updates
     */
    fun observeTasks(): Observable<List<Task>> {
        return Observable.create { emitter ->
            tasksRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    emitter.onError(error)
                } else {
                    snapshot?.let {
                        val tasksList = it.toObjects(Task::class.java)
                        emitter.onNext(tasksList)
                    } ?: emitter.onError(Throwable("Empty snapshot"))
                }
            }
        }
    }

    /**
     * Creates a new task in firestore. This provides offline syncing that cannot be turned off
     */
    fun createTaskWithOfflineSyncing(task: Task): Completable {
        Timber.d("Creating new task")
        return Completable.create { emitter ->
            tasksRef.add(task).addOnSuccessListener { documentReference ->
                // in case we would need uuid of inserted task
                val id = documentReference.id
                emitter.onComplete()
            }.addOnFailureListener { e ->
                emitter.onError(e)
            }
        }
    }

    /**
     * Creates a new tas in firestore. This disables offline syncing so exceptions can be handled
     */
    fun createTaskWithoutOfflineSyncing(task: Task): Completable {
        return Completable.create { emitter ->
            val tasksDoc = tasksRef.document()
            dbRef.runTransaction { transaction ->
                transaction.set(tasksDoc, task)
            }.addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(it.exception ?: Exception("Error inserting task"))
                }
            }.addOnFailureListener { e ->
                if (!emitter.isDisposed) emitter.onError(e)
            }
        }
    }

}
