plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.serialization)
    alias(libs.plugins.com.squareup.wire)
    alias(libs.plugins.com.google.devtools.ksp)
    id("maven-publish")
}

android {
    namespace = "com.m3u.extension.api"
    compileSdk = 35
    defaultConfig {
        minSdk = 26
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
        aidl = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

wire {
    kotlin {
    }
    protoPath {
        srcDir("src/main/proto")
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = "com.m3u.extension"
                artifactId = "api"
                version = libs.versions.extension.get()

                from(components["release"])
            }
        }
    }
}

dependencies {
    implementation(project(":annotation"))
    ksp(project(":processor"))
    // kotlinx-coroutines
    implementation(libs.kotlinx.coroutines.core)
    // wire
    implementation(libs.wire.runtime)
    // reflect
    api(libs.kotlin.reflect)
}
