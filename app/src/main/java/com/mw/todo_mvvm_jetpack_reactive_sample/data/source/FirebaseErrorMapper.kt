package com.mw.todo_mvvm_jetpack_reactive_sample.data.source

import android.content.Context
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestoreException
import com.mw.todo_mvvm_jetpack_reactive_sample.R
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

private const val ERROR_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
private const val ERROR_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD"

class FirebaseErrorMapper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun map(throwable: Throwable): ToDoError {
        return when (throwable) {
            is FirebaseAuthException -> mapFirebaseAuthException(throwable)
            is FirebaseFirestoreException -> mapFirebaseFirestoreException(throwable)
            is IllegalArgumentException -> AuthErrors.InvalidArgumentsException("Email or password cannot be empty")
            else -> AuthErrors.UnknownException(context.getString(R.string.error_login_unknown)) // TODO: return different error
        }
    }

    private fun mapFirebaseAuthException(exception: FirebaseAuthException): AuthErrors {
        return when (exception) {
            is FirebaseAuthInvalidUserException -> AuthErrors.UserNotFoundException(
                context.getString(
                    R.string.error_login_invalid_credentials
                )
            )
            is FirebaseAuthInvalidCredentialsException -> mapFirebaseAuthErrorCodes(exception.errorCode)
            else -> AuthErrors.UnknownException(context.getString(R.string.error_login_unknown))
        }
    }

    private fun mapFirebaseAuthErrorCodes(errorCode: String): AuthErrors {
        return when (errorCode) {
            ERROR_INVALID_EMAIL -> AuthErrors.WrongEmailFormatException(context.getString(R.string.error_login_invalid_email))
            ERROR_WEAK_PASSWORD -> AuthErrors.WeakPasswordException(context.getString(R.string.error_password_too_weak))
            else -> AuthErrors.UnknownException(context.getString(R.string.error_login_unknown))
        }
    }

    private fun mapFirebaseFirestoreException(exception: FirebaseFirestoreException): FirestoreErrors {
        // https://firebase.google.com/docs/reference/android/com/google/firebase/firestore/FirebaseFirestoreException.Code
        return when (exception.code) {
            FirebaseFirestoreException.Code.PERMISSION_DENIED, FirebaseFirestoreException.Code.UNAUTHENTICATED ->
                FirestoreErrors.UnauthorizedException(context.getString(R.string.error_unauthenticated_user))
            FirebaseFirestoreException.Code.CANCELLED ->
                FirestoreErrors.CanceledException(context.getString(R.string.error_login_unknown))
            FirebaseFirestoreException.Code.UNKNOWN ->
                FirestoreErrors.UnknownException(context.getString(R.string.error_login_unknown))
            FirebaseFirestoreException.Code.NOT_FOUND ->
                FirestoreErrors.NotFound(context.getString(R.string.error_login_unknown))
            FirebaseFirestoreException.Code.ALREADY_EXISTS ->
                FirestoreErrors.AlreadyExists(context.getString(R.string.error_login_unknown))
            FirebaseFirestoreException.Code.UNAVAILABLE ->
                FirestoreErrors.NetworkException(context.getString(R.string.error_no_network_connection))
            else -> FirestoreErrors.UnknownException(context.getString(R.string.error_login_unknown))
        }
    }
}

fun Completable.applyErrorMapper(mapper: FirebaseErrorMapper): Completable {
    return this.onErrorResumeNext { Completable.error(mapper.map(it)) }
}

fun <T> Single<T>.applyErrorMapper(mapper: FirebaseErrorMapper): Single<T> {
    return this.onErrorResumeNext { Single.error(mapper.map(it)) }
}

fun <T> Observable<T>.applyErrorMapper(mapper: FirebaseErrorMapper): Observable<T> {
    return onErrorResumeNext { throwable: Throwable ->
        Observable.error(mapper.map(throwable))
    }
}

sealed class ToDoError(message: String?) : Throwable(message)

sealed class AuthErrors(message: String) : ToDoError(message) {
    data class UserNotFoundException(val errorMessage: String) : AuthErrors(errorMessage)
    data class WrongEmailFormatException(val errorMessage: String) : AuthErrors(errorMessage)
    data class UnknownException(val errorMessage: String) : AuthErrors(errorMessage)
    data class InvalidArgumentsException(val errorMessage: String) : AuthErrors(errorMessage)
    data class WeakPasswordException(val errorMessage: String) : AuthErrors(errorMessage)
}

sealed class FirestoreErrors(message: String) : ToDoError(message) {
    data class UnauthorizedException(val errorMessage: String) : FirestoreErrors(errorMessage)
    data class UnknownException(val errorMessage: String) : FirestoreErrors(errorMessage)
    data class NotFound(val errorMessage: String) : FirestoreErrors(errorMessage)
    data class CanceledException(val errorMessage: String) : FirestoreErrors(errorMessage)
    data class AlreadyExists(val errorMessage: String) : FirestoreErrors(errorMessage)
    data class NetworkException(val errorMessage: String) : FirestoreErrors(errorMessage)
}
