apply(from = rootProject.file("config/detekt/detekt.gradle"))

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    kotlin("android")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdkVersion(Versions.COMPILE_SDK)
    buildToolsVersion(Versions.BUILD_TOOLS)

    defaultConfig {
        applicationId = "com.mw.todo_mvvm_jetpack_reactive_sample"
        minSdkVersion(Versions.MIN_SDK)
        targetSdkVersion(Versions.TARGET_SDK)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        testOptions {
            unitTests.isReturnDefaultValues = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
    }

    lintOptions {
//        baselineFile = rootProject.file("lint-baseline.xml") // for whole project
        baselineFile = project.file("lint-baseline.xml") // for module
        isAbortOnError = true // true by default tho
        isWarningsAsErrors = true
        xmlReport = true
        htmlReport = false
        disable("VectorPath", "ObsoleteLintCustomCheck")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.4")

    implementation(Libs.MATERIAL)

    // firebase
    implementation(platform(Libs.FIREBASE_BOM))
    implementation(Libs.FIREBASE_ANALYTICS)
    implementation(Libs.FIREBASE_CRASHLYTICS)
    implementation(Libs.FIREBASE_FIRESTORE)
    implementation(Libs.FIREBASE_AUTH)
    implementation(Libs.FIREBASE_AUTH_UI)

    // architecture components
    implementation(Libs.LIFECYCLE_VIEWMODEL)
    implementation(Libs.LIFECYCLE_LIVEDATA)
    implementation(Libs.NAVIGATION_FRAGMENT)
    implementation(Libs.NAVIGATION_UI)
    implementation(Libs.REACTIVE_STREAMS)

    implementation(Libs.TIMBER)

    // dagger
    implementation(Libs.HILT)
    implementation(Libs.HILT_VIEWMODEL)
    kapt(Libs.HILT_COMPILER)
    kapt(Libs.HILT_VIEWMODEL_COMPILER)

    // rxjava
    implementation(Libs.RX_JAVA)
    implementation(Libs.RX_ANDROID)

    testImplementation("junit:junit:4.13.1")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    // unit tests
    testImplementation(TestLibs.MOCKK)
    testImplementation(TestLibs.MOCKITO_KOTLIN)
    testImplementation(TestLibs.JUNIT_KOTLIN)

}
