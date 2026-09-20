plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "io.github.wzhdgithub.tempmailui.demo"
    compileSdk = 37

    defaultConfig {
        applicationId = "io.github.wzhdgithub.tempmailui.demo"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

dependencies {
    implementation(project(":tempmailui"))

    val composeBom = platform("androidx.compose:compose-bom:2026.05.01")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation")
    // material3 1.4.0 起不再传递依赖 material-icons-core，演示用的图标需显式声明
    implementation("androidx.compose.material:material-icons-core")

    implementation("androidx.core:core-ktx:1.12.0")
    // setContent / enableEdgeToEdge / PickVisualMedia（相册取色，无需权限）
    implementation("androidx.activity:activity-compose:1.8.2")

    debugImplementation("androidx.compose.ui:ui-tooling")
}