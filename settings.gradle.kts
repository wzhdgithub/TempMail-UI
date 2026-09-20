pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TempMail-UI"

// UI 实现库：主题系统（Miuix/HyperOS 桥接 + 莫奈取色）+ 液态玻璃底栏
include(":tempmailui")
// 可运行的演示 App：主题设置页 / 预览示意图 / 强调色下拉 / 三形态底栏
include(":app")