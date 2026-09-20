plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "io.github.wzhdgithub.tempmailui"
    // Miuix 0.9.3 要求 compileSdk >= 37（见根目录 gradle.properties 说明）
    compileSdk = 37

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
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
    // 公开 API 中出现了 Color / ColorScheme / ImageVector 等 Compose 类型，故用 api 暴露
    val composeBom = platform("androidx.compose:compose-bom:2026.05.01")
    api(composeBom)
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-graphics")
    api("androidx.compose.foundation:foundation")
    api("androidx.compose.material3:material3")
    // material3 1.4.0 起不再传递依赖 material-icons-core
    api("androidx.compose.material:material-icons-core")

    // 动态配色（莫奈取色 → Material 3 配色）：
    //  - palette：官方取色库（从图片提取主色，得到 seed）
    //  - material-color-utilities：Google MCU 的 Kotlin 移植，seed → 完整 M3 ColorScheme
    //    版本刻意与 miuix-ui 传递依赖的 4.1.1 对齐：该库是 KMP 多模块发布，
    //    若声明 -android 5.x 会把 Miuix 依赖的那份一起升级，从而让 Miuix 运行在
    //    它构建时未针对的版本上；同版本则只有一份、零风险。
    implementation("androidx.palette:palette:1.0.0")
    implementation("com.materialkolor:material-color-utilities:4.1.1")

    // Miuix（KernelSU 同款 UI 框架）：miuix-ui 提供 HyperOS 组件，
    // miuix-blur 提供 RuntimeShader 液态玻璃模糊（真实模糊仅 API 33+，低版本自动降级）
    val miuixVersion = "0.9.3"
    implementation("top.yukonga.miuix.kmp:miuix-ui-android:$miuixVersion")
    implementation("top.yukonga.miuix.kmp:miuix-blur-android:$miuixVersion")
}