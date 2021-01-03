# TODO list app

This is an app with well known functionality of ToDo list. This example
purpose is to present best practices of writing and android app,
following the clean code, architecture pattern and android architecture
components (jetpack).

This follows MVVM architecture with Repository pattern in a reactive way
and uses:
- Firebase: Firestore, Auth, Analytics, Crashlytics
- RxJava2, RxAndroid
- Material Design
- Dagger Hilt
- Glide
- Timber
- Jetpack: LiveData, ViewModel, Navigation, Pagination, Room
- Unit tests
- buildSrc for dependencies and KTS

#### Future plans
- Migrate from firebase to prepared Ktor backend, for that use of
  Retrofit & Moshi, plus Room for caching
- Add UI test (to consider)
- Configure Github Actions
- Configure lint & detekt


## Notes

#### Firebase analytics

1. Handle user consent

We can disable tracking by default by adding these lines to manifest

```xml
    <meta-data
        android:name="firebase_analytics_collection_enabled"
        android:value="false" />

    <meta-data
        android:name="firebase_crashlytics_collection_enabled"
        android:value="false" />
```
And we can ask user for the consent:

```kotlin
    Firebase.analytics.setAnalyticsCollectionEnabled(true)
    FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)
```

#### Firebase firestore & auth with RxJava

To use firestore and/or auth with RxJava, you can follow this simple scheme that just uses `.create` operator:

```kotlin
    fun get(): [ObservableType]<[Model]> {
        return [ObservableType].create { emitter ->
            collection.get().addOnSuccessListener { result ->
                emitter.onSuccess(result.toObject([Model]::class.java))
                // or emitter.OnComplete() depending on Emitter type
            }.addOnFailureListener { e ->
                emitter.onError(e)
            }
        }
    }
```

e.g for Single:

```kotlin
    fun getSingle(): Single<Model> {
        return Single.create { emitter ->
            collection.get().addOnSuccessListener { result ->
                val document = result.toObject(Model::class.java) // for list: [.toObjects]
                emitter.onSuccess(tasksList)
            }.addOnFailureListener { e ->
                emitter.onError(e)
            }
        }
    }
```

The same scheme can be applied for authentication. There is a nice feature tho to observe auth state changes in real time:

```kotlin
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
```

#### SingleLiveEvent and/or Event

There is a problem with SingleLiveEvent class, that it accepts only one observer at a time. There is a fix for that, check `MultiSingleLiveEvent.kt`.
However, the `MultiSingleLiveEvent.kt` has leaking issue. This is a case, where Events would be useful and they would solve the problem. Here more about
[Events](https://medium.com/androiddevelopers/livedata-with-snackbar-navigation-and-other-events-the-singleliveevent-case-ac2622673150).

However in my opinion, the Event approach is also not ideal. It solves the issue with multiple observers, but still when the first observer receives the
event and calls `getContentIfNotHandled()` marking it to be read, all other events won't receive it as it was already handled. We could use here the `peekContet()`, but
than it has no idea that this Event was already handled anytime in the past. To fix that, we could refactor it a little bit and add e.g a Map that marks for which
observer the content has been handled, like Map of String (a key of a observer's class) and a boolean/int indicating if the content was handled.

#### Kotlin's operator overloading:
There is a very nice feature in kotlin - operators overloading. Ive added a simple example of overloading `invoke()` operator in the usecases, like so:

```kotlin
    class GetTasksUseCase @Inject constructor(
        private val repository: TasksRepository
    ) {    
        operator fun invoke(): Observable<List<Task>> = repository.observeTasks()
    }
```

Which than we can use like this:

```kotlin
    //  assume we have an instance like getTasksUseCase: GetTasksUseCase
    getTaskUseCase() // instead of getTaskUseCase().getTasks()
```
There are plenty of more operators available to overload, check the docs [here](https://kotlinlang.org/docs/reference/operator-overloading.html)

We can ofc pass args to the overloaded operator:

```kotlin
    class SomeUseCase @Inject constructor(
        private val repository: Repository
    ) {    
        operator fun invoke(
            arg1: Boolean = false,
            arg2: SomeArg
        ): Observable<List<Task>> = repository.observeTasks() {
            return repository.observerModel(arg1, arg2)
        }
    }
```
