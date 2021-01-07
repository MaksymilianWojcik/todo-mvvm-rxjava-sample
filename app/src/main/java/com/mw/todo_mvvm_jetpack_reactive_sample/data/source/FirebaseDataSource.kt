package com.mw.todo_mvvm_jetpack_reactive_sample.data.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import io.reactivex.*
import timber.log.Timber
import javax.inject.Inject

private const val COLLECTION_TASKS = "Tasks"
private const val FIELD_TASK_IS_COMPLETED = "isCompleted"

class FirebaseDataSource @Inject constructor(private val dbRef: FirebaseFirestore) {

    private val tasksRef: CollectionReference = dbRef.collection(COLLECTION_TASKS)

    fun clearCompletedTasks(): Completable {
        // one way, performing query on firestore - extra calls for querying
        return Completable.create { emitter ->
            tasksRef.whereEqualTo(FIELD_TASK_IS_COMPLETED, true).get().addOnSuccessListener { querySnapshot ->
                val batch = dbRef.batch()
                querySnapshot.forEach { doc ->
                    batch.delete(doc.reference)
                }
                batch.commit()
                    .addOnSuccessListener {
                        emitter.onComplete()
                    }.addOnFailureListener {
                        emitter.onError(it)
                    }
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }

    private fun getCompletedTasks(): Single<List<Task>> {
        return Single.create { emitter ->
            val tasksCompleted = tasksRef.whereEqualTo(FIELD_TASK_IS_COMPLETED, true).get()
                .addOnSuccessListener { result ->
                    val tasksList = result.toObjects(Task::class.java)
                    emitter.onSuccess(tasksList)
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    fun removeTasks(tasks: List<Task>): Completable {
        // another way, using batch writes and provided list of documents to be removed
        return Completable.create { emitter ->
            dbRef.runBatch {
                tasks.map { tasksRef.document(it.id!!).delete() }
            }.addOnSuccessListener {
                emitter.onComplete()
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }

    fun completeTask(task: Task): Completable {
        return Completable.create { emitter ->
            val taskDoc = tasksRef.document(task.id!!)
            taskDoc.update(FIELD_TASK_IS_COMPLETED, true).addOnSuccessListener {
                emitter.onComplete()
            }.addOnFailureListener {
                emitter.onError(it)
            }
        }
    }

    fun activateTask(task: Task): Completable {
        return Completable.create { emitter ->
            task.id?.let {
                val taskDoc = tasksRef.document(it)
                // another way of updating doc
                taskDoc.set(task.copy(isCompleted = false))
            } ?: emitter.onError(Exception("Task id not provided!"))
        }
    }

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
     * We don't need to use Flowable as we don't have a lot of items over time being emitted that we have to control.
     * Observable is fine as we have only a few of them and no risk of any overflooding. Its a cold source example.
     */
    fun observeTasks(): Flowable<List<Task>> {
        return Flowable.create({ emitter ->
            tasksRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    emitter.onError(error)
                } else {
                    snapshot?.let { querySnapshot ->
                        val tasksList = querySnapshot.toObjects(Task::class.java)
                        emitter.onNext(tasksList)
                    } ?: emitter.onError(Throwable("Empty snapshot"))
                }
            }
        }, BackpressureStrategy.DROP)
    }

    /**
     * Returns task list observable with real time firestore updates with their ids
     */
    fun observeTasksWithTheirIds(): Observable<List<Task>> {
        return Observable.create { emitter ->
            tasksRef.addSnapshotListener { snapshot, error ->
                if (error != null) {
                    emitter.onError(error)
                } else {
                    snapshot?.let { querySnapshot ->
                        val tasksListTemp: MutableList<Task> = mutableListOf()
                        for (docSnapshot in querySnapshot) {
                            val task = docSnapshot.toObject(Task::class.java)
                            task.id = docSnapshot.id
                            tasksListTemp.add(task)
                        }
                        emitter.onNext(tasksListTemp)
                    } ?: emitter.onError(Throwable("Empty snapshot"))
                }
            }
        }
    }

    /**
     * Creates a new task in firestore. This provides offline syncing that cannot be turned off
     */
    fun createTaskWithOfflineSyncing(task: Task): Completable {
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

    /**
     * Creates a new task in firestore. This provides offline syncing that cannot be turned off and sets document id as task
     * param
     */
    fun createTaskWithOfflineSyncingAndSetId(task: Task): Completable {
        val taskDoc = tasksRef.document()
        task.id = taskDoc.id
        return setDocumentCompletable(taskDoc, task)
    }

    // This function sets provided document with provided data. This reduces boilerplate
    private fun setDocumentCompletable(documentReference: DocumentReference, data: Any): Completable {
        return Completable.create { emitter ->
            documentReference.set(data)
                .addOnSuccessListener {
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}
