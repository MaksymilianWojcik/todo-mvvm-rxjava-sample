package com.mw.todo_mvvm_jetpack_reactive_sample.data.source

import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.User
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class FirebaseAuthSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authUI: AuthUI
) {

    val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    /**
     * Returns current user if logged in, else null
     */
    fun getUser(): User? = firebaseAuth.currentUser?.toUser

    fun getUserSingle(): Single<User> = Single.create { emitter ->
        firebaseAuth.currentUser?.let {
            emitter.onSuccess(it.toUser)
        } ?: emitter.onSuccess(User())
    }

    fun signOutCompletable() = Completable.fromAction { firebaseAuth.signOut() }

    fun signOut() = firebaseAuth.signOut()

    fun isUserLoggedIn(): Boolean = firebaseAuth.currentUser != null

    fun signInWithEmail(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    emitter.onComplete()
                } else {
                    emitter.onError(task.exception ?: Exception("Failed to login"))
                }
            }
        }
    }

    fun registerWithEmail(email: String, password: String): Completable {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        emitter.onComplete()
                        // TODO: Store user in firestore with extra details
                    } else {
                        emitter.onError(task.exception ?: Exception("Failed to register"))
                    }
                }
        }
    }

    fun observeAuthState(): Observable<Boolean> {
        return Observable.create<Boolean> { emitter ->
            val authStateListener = FirebaseAuth.AuthStateListener {
                if (!emitter.isDisposed) emitter.onNext(it.currentUser != null)
            }
            firebaseAuth.addAuthStateListener(authStateListener)
            emitter.setCancellable {
                firebaseAuth.removeAuthStateListener(authStateListener)
            }
        }
    }

}

internal val FirebaseUser.toUser : User
get() = User(
    id = this.uid,
    email = this.email ?: "",
    name = this.displayName ?: ""
)
