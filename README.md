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

