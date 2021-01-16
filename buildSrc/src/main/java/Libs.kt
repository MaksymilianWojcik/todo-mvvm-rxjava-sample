object Libs {
    const val FIREBASE_BOM = "com.google.firebase:firebase-bom:${Versions.FIREBASE_BOM}"
    const val FIREBASE_ANALYTICS = "com.google.firebase:firebase-analytics-ktx"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase:firebase-crashlytics-ktx"
    const val FIREBASE_FIRESTORE = "com.google.firebase:firebase-firestore-ktx"
    const val FIREBASE_AUTH = "com.google.firebase:firebase-auth-ktx"
    const val FIREBASE_AUTH_UI = "com.firebaseui:firebase-ui-auth:${Versions.FIREBASE_AUTH_UI}"

    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"

    // Architecture Components
    const val LIFECYCLE_VIEWMODEL =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.LIFECYCLE}"
    const val LIFECYCLE_LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.LIFECYCLE}"
    const val REACTIVE_STREAMS =
        "androidx.lifecycle:lifecycle-reactivestreams-ktx:${Versions.LIFECYCLE}"
    const val NAVIGATION_FRAGMENT =
        "androidx.navigation:navigation-fragment-ktx:${Versions.NAVIGATION}"
    const val NAVIGATION_UI = "androidx.navigation:navigation-ui-ktx:${Versions.NAVIGATION}"

    const val TIMBER = "com.jakewharton.timber:timber:${Versions.TIMBER}"

    // Dagger
    const val HILT = "com.google.dagger:hilt-android:${Versions.HILT}"
    const val HILT_COMPILER = "com.google.dagger:hilt-android-compiler:${Versions.HILT}"
    const val HILT_VIEWMODEL = "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.HILT_VIEWMODEL}"
    const val HILT_VIEWMODEL_COMPILER = "androidx.hilt:hilt-compiler:${Versions.HILT_VIEWMODEL}"

    const val RX_JAVA = "io.reactivex.rxjava2:rxjava:${Versions.RX_JAVA}"
    const val RX_ANDROID = "io.reactivex.rxjava2:rxandroid:${Versions.RX_ANDROID}"
}

object TestLibs {
    // a useful mocker with a lot of additional functions, like relaxed that returns simple values on mocked object calls
    const val MOCKK = "io.mockk:mockk:${Versions.MOCKK}"

    // some kotlin helper for Mockito. I like this part: verify(mock).sometFunction(any())
    const val MOCKITO_KOTLIN =
        "com.nhaarman.mockitokotlin2:mockito-kotlin:${Versions.MOCKITO_KOTLIN}"

    const val JUNIT_KOTLIN = "org.jetbrains.kotlin:kotlin-test-junit:${Versions.KOTLIN}"

    // For making assertions more intuitive and readable - note the androidx
    const val TRUTH = "androidx.test.ext:truth:1.0.0"

}
