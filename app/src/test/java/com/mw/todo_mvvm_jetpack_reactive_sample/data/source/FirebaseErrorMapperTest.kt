package com.mw.todo_mvvm_jetpack_reactive_sample.data.source

import android.content.Context
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestoreException
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FirebaseErrorMapperTest {

    private val context = mockk<Context>(relaxed = true)
    private val cut: FirebaseErrorMapper = FirebaseErrorMapper(context)

    @Test
    fun `Given IllegalArgumentException Then return InvalidArgumentsException`() {
        // Given
        val error = mockk<IllegalArgumentException>()

        // When
        val actual = cut.map(error)

        // Then
        assertTrue(actual is AuthErrors.InvalidArgumentsException)
    }

    @Test
    fun `Given not handled type of throwable Then return UnknownException`() {
        // Given
        val error = mockk<Exception>()

        // When
        val actual = cut.map(error)

        // Then
        assertTrue(actual is AuthErrors.UnknownException)
    }

    @Test
    fun `Given FirebaseInvalidUserException Then return UserNotFound`() {
        // Given
        val error: FirebaseAuthException = mockk<FirebaseAuthInvalidUserException>()
        val expected = AuthErrors.UserNotFoundException("") // thx to mockk relaxed = true

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual) // thx to mockk relaxed = true
    }

    @Test
    fun `Given FirebaseAuthInvalidCredentialsException and error code is invalid email Then return WrongEmailFormatException`() {
        // Given
        val error: FirebaseAuthException = mockk<FirebaseAuthInvalidCredentialsException>()
        every { error.errorCode } returns "ERROR_INVALID_EMAIL"
        val expected = AuthErrors.WrongEmailFormatException("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
        assertTrue(actual is AuthErrors.WrongEmailFormatException)
    }

    @Test
    fun `Given FirebaseAuthInvalidCredentialsException and error code is weak password Then return WeakPasswordException`() {
        // Given
        val error: FirebaseAuthException = mockk<FirebaseAuthInvalidCredentialsException>()
        every { error.errorCode } returns "ERROR_WEAK_PASSWORD"
        val expected = AuthErrors.WeakPasswordException("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Given FirebaseFirestoreException and error code is PERMISSION_DENIED Then return UnauthorizedException`() {
        // Given
        val error = mockk<FirebaseFirestoreException>()
        every { error.code } returns FirebaseFirestoreException.Code.PERMISSION_DENIED
        val expected = FirestoreErrors.UnauthorizedException("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Given FirebaseFirestoreException and error code is UNAUTHENTICATED Then return UnauthorizedException`() {
        // Given
        val error = mockk<FirebaseFirestoreException>()
        every { error.code } returns FirebaseFirestoreException.Code.UNAUTHENTICATED
        val expected = FirestoreErrors.UnauthorizedException("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Given FirebaseFirestoreException and error code is CANCELLED Then return CanceledException`() {
        // Given
        val error = mockk<FirebaseFirestoreException>()
        every { error.code } returns FirebaseFirestoreException.Code.CANCELLED
        val expected = FirestoreErrors.CanceledException("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Given FirebaseFirestoreException and error code is UNKNOWN Then return UnknownException`() {
        // Given
        val error = mockk<FirebaseFirestoreException>()
        every { error.code } returns FirebaseFirestoreException.Code.UNKNOWN
        val expected = FirestoreErrors.UnknownException("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Given FirebaseFirestoreException and error code is NOT_FOUND Then return NotFoundException`() {
        // Given
        val error = mockk<FirebaseFirestoreException>()
        every { error.code } returns FirebaseFirestoreException.Code.NOT_FOUND
        val expected = FirestoreErrors.NotFound("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Given FirebaseFirestoreException and error code is ALREADY_EXISTS Then return AlreadyExists`() {
        // Given
        val error = mockk<FirebaseFirestoreException>()
        every { error.code } returns FirebaseFirestoreException.Code.ALREADY_EXISTS
        val expected = FirestoreErrors.AlreadyExists("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Given FirebaseFirestoreException and error code is UNAVAILABLE Then return NetworkException`() {
        // Given
        val error = mockk<FirebaseFirestoreException>()
        every { error.code } returns FirebaseFirestoreException.Code.UNAVAILABLE
        val expected = FirestoreErrors.NetworkException("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
    }

    @Test
    fun `Given FirebaseFirestoreException and error code is not handled like DATA_LOSS Then return UnknownException`() {
        // Given
        val error = mockk<FirebaseFirestoreException>()
        every { error.code } returns FirebaseFirestoreException.Code.DATA_LOSS
        val expected = FirestoreErrors.UnknownException("")

        // When
        val actual = cut.map(error)

        // Then
        assertEquals(expected, actual)
    }
}
