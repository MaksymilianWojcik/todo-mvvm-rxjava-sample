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
