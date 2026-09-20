import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// 签名凭据从根目录 keystore.properties（不纳入版本控制）读取，避免明文密码泄露。
// 也可通过环境变量 KEYSTORE_PASSWORD / KEY_PASSWORD 提供（CI 场景）；
// 两者都没有时 release 走未签名产物（不会中断构建）。
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystorePropertiesFile.inputStream().use { keystoreProperties.load(it) }
}
val releaseStorePassword = keystoreProperties.getProperty("storePassword")
    ?: System.getenv("KEYSTORE_PASSWORD")
val releaseKeyPassword = keystoreProperties.getProperty("keyPassword")
    ?: System.getenv("KEY_PASSWORD")
val hasReleaseKeystore = !releaseStorePassword.isNullOrBlank() && !releaseKeyPassword.isNullOrBlank()

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

    if (hasReleaseKeystore) {
        signingConfigs {
            create("release") {
                // storeFile 相对本模块目录解析
                storeFile = file(keystoreProperties.getProperty("storeFile", "keystore.jks"))
                storePassword = releaseStorePassword
                keyAlias = keystoreProperties.getProperty("keyAlias", "tempmailui")
                keyPassword = releaseKeyPassword
            }
        }
    }

    buildTypes {
        // debug 也用同一把钥匙：debug / release 可互相覆盖安装，避免"应用未安装"
        debug {
            if (hasReleaseKeystore) signingConfig = signingConfigs.getByName("release")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            if (hasReleaseKeystore) signingConfig = signingConfigs.getByName("release")
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