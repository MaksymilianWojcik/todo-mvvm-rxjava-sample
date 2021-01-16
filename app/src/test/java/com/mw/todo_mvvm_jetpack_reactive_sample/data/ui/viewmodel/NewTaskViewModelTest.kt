package com.mw.todo_mvvm_jetpack_reactive_sample.data.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mw.todo_mvvm_jetpack_reactive_sample.data.model.Task
import com.mw.todo_mvvm_jetpack_reactive_sample.domain.usecase.CreateNewTaskUseCase
import com.mw.todo_mvvm_jetpack_reactive_sample.ui.viewmodel.NewTaskViewModel
import common.RxImmediateSchedulerRule
import common.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Completable
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

// Keep in mind, that currently this test is ugly written because we didn't use some extensions helpers for LiveData
// testing and we use a lot of libs for same thing for a showcase, like assertTrue from junit and assertTrue {} from kotlin test
class NewTaskViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    lateinit var loadingObserver2: Observer<Boolean>

    val createNewTaskUseCase = mockk<CreateNewTaskUseCase>()

    private val titleChangesList = mutableListOf<String>()
    private val titleObserver = Observer<String> {
        titleChangesList.add(it)
    }

    private val descriptionChangesList = mutableListOf<String>()
    private val descriptionObserver = Observer<String> {
        descriptionChangesList.add(it)
    }

    private val isFormValidChangesList = mutableListOf<Boolean>()
    private val isFormValidObserver = Observer<Boolean> {
        isFormValidChangesList.add(it)
    }

    private val loadingDataEventsList = mutableListOf<Boolean>()
    private val loadingDataObserver = Observer<Boolean> {
        loadingDataEventsList.add(it)
    }

    lateinit var cut: NewTaskViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        cut = NewTaskViewModel(createNewTaskUseCase)
        with(cut) {
            title.observeForever(titleObserver)
            description.observeForever(descriptionObserver)
            isFormValid.observeForever(isFormValidObserver)
            loadingData.observeForever(loadingDataObserver)
            loadingData.observeForever(loadingObserver2)
        }
    }

    @After
    fun tearDown() {
        with(cut) {
            title.removeObserver(titleObserver)
            description.removeObserver(descriptionObserver)
            isFormValid.removeObserver(isFormValidObserver)
            loadingData.removeObserver(loadingDataObserver)
        }
    }

    @Test
    fun `Initialised viewmodel doesnt do anything`() {
        assertTrue { titleChangesList.isEmpty() }
        assertTrue { descriptionChangesList.isEmpty() }
        assertFalse { isFormValidChangesList.isEmpty() } // false because we already observer and its a transformation
        assertEquals(cut.isFormValid.getOrAwaitValue(), false)
    }

    @Test
    fun `Given form is valid When creating task Then task is created`() {
        // Given
        val task = Task(title = "title", description = "description")
        cut.title.value = task.title
        cut.description.value = task.description
        every { createNewTaskUseCase(task) } returns Completable.complete()


        // When
        cut.createTask()

        // Then
        assertEquals(true, cut.isFormValid.value)
        verify(exactly = 1, verifyBlock = { createNewTaskUseCase(any()) })
        assertEquals(2, loadingDataEventsList.size)
        assertTrue { loadingDataEventsList[0] }
        assertFalse { loadingDataEventsList[1] }
        assertNotNull(cut.snackbarMessage.value)
        assertNotNull(cut.closeScreen.value)
        assertEquals(false, cut.loadingData.value)
        com.nhaarman.mockitokotlin2.verify(loadingObserver2).onChanged(false)
        com.nhaarman.mockitokotlin2.verify(loadingObserver2).onChanged(true)
    }

    @Test
    fun `Given form is invalid for title and description When creating task Then task is not created and snackbar message is shown`() {
        // Given
        val task = Task(title = "", description = "")
        cut.title.value = task.title
        cut.description.value = task.description

        // When
        cut.createTask()

        // Then
        assertEquals(false, cut.isFormValid.value)
        assertEquals(2, loadingDataEventsList.size)
        assertTrue { loadingDataEventsList[0] }
        assertFalse { loadingDataEventsList[1] }
        assertNotNull(cut.snackbarMessage.value)
        assertNull(cut.closeScreen.value)
        assertEquals(false, cut.loadingData.value)
        verify(exactly = 0, verifyBlock = { createNewTaskUseCase(any()) })
    }

    @Test
    fun `Given form is invalid for title When creating task Then task is not created and snackbar message is shown`() {
        // Given
        val task = Task(title = "", description = "NotEmpty")
        cut.title.value = task.title
        cut.description.value = task.description

        // When
        cut.createTask()

        // Then
        assertEquals(false, cut.isFormValid.value)
        assertEquals(2, loadingDataEventsList.size)
        assertTrue { loadingDataEventsList[0] }
        assertFalse { loadingDataEventsList[1] }
        assertNotNull(cut.snackbarMessage.value)
        assertNull(cut.closeScreen.value)
        assertEquals(false, cut.loadingData.value)
        verify(exactly = 0, verifyBlock = { createNewTaskUseCase(any()) })
    }

    @Test
    fun `Given form is invalid for description When creating task Then task is not created and snackbar message is shown`() {
        // Given
        val task = Task(title = "NotEmpty", description = "")
        cut.title.value = task.title
        cut.description.value = task.description

        // When
        cut.createTask()

        // Then
        assertEquals(false, cut.isFormValid.value)
        assertEquals(2, loadingDataEventsList.size)
        assertTrue { loadingDataEventsList[0] }
        assertFalse { loadingDataEventsList[1] }
        assertNotNull(cut.snackbarMessage.value)
        assertNull(cut.closeScreen.value)
        assertEquals(false, cut.loadingData.value)
        // mockk's verify, I prefer nharman tho, but we used mockk for mocking that use case
        verify(exactly = 0, verifyBlock = { createNewTaskUseCase(any()) })
    }

    @Test
    fun `Given form is valid and error When creating task Then task is created`() {
        // Given
        val error = mockk<Throwable>()
        val task = Task(title = "title", description = "description")
        cut.title.value = task.title
        cut.description.value = task.description
        every { createNewTaskUseCase(task) } returns Completable.error(error)

        // When
        cut.createTask()

        // Then
        assertEquals(true, cut.isFormValid.value)
        assertEquals(2, loadingDataEventsList.size)
        assertTrue { loadingDataEventsList[0] }
        assertFalse { loadingDataEventsList[1] }
        assertNotNull(cut.snackbarMessage.value)
        assertNull(cut.closeScreen.value)
        assertEquals(false, cut.loadingData.value)
        verify(exactly = 1, verifyBlock = { createNewTaskUseCase(any()) })
    }
}
