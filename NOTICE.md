# NOTICE / 第三方声明

本项目（TempMail-UI：tempmail 应用的 UI 实现）以 **GPL-3.0-or-later** 发布，
完整许可全文见根目录 [LICENSE](LICENSE)。

Copyright (C) 2026 wzhdgithub

下列第三方代码**实际用于**本项目的实现，特此保留其版权与许可声明。
仅「浏览研究过、最终没有采用」的项目不列入本文件。

---

## 1. tiann / KernelSU — 玻璃底栏的直接来源

- 仓库：https://github.com/tiann/KernelSU
- 作者：weishu（tiann）与 KernelSU 贡献者
- 许可：**GPL-3.0-or-later**
  （其根目录 LICENSE 为 GPLv3 全文；README 声明 `kernel/` 目录为 GPL-2.0-only，其余部分为 GPL-3.0-or-later）
- 本项目实际使用的内容：
  - `tempmailui/.../glass/InnerShadow.kt`
    —— 移植自其 `manager/app/src/main/java/me/weishu/kernelsu/ui/component/liquid/InnerShadow.kt`
  - `tempmailui/.../glass/GlassInteraction.kt`
    —— 移植自其 `manager/.../ui/component/miuix/animation/` 与
    `manager/.../ui/component/miuix/modifier/DragGestureInspector.kt`
    （`DampedDragAnimation`、`InteractiveHighlight`、`inspectDragGestures`）
  - `tempmailui/.../glass/GlassBottomBar.kt` 中的折射 / 色散 AGSL 着色器
    —— 移植自其 `manager/.../ui/component/liquid/Lens.kt`
  - 交互方案（按下放大镜、拖动切换 Tab、惯性吸附、越界橡皮筋、重力方向高光）参照其 FloatingBottomBar 实现路径
- 使用方式：**移植 / 修改后使用**——去掉了长按阈值与震动反馈，重写了手势层（顶层覆盖层）与全部手感参数，
  详见各文件头部注释与 `GlassBottomBar.kt` 顶部参数区

## 2. Kyant0 / AndroidLiquidGlass — 上游作者

- 仓库：https://github.com/Kyant0/AndroidLiquidGlass
- 作者：Kyant
- 许可：**Apache License 2.0**，`Copyright 2025 Kyant`
- 本项目实际使用的内容（经 KernelSU 镜像而来，故在此保留其版权声明）：
  - 圆角矩形折射与色散 AGSL 着色器
    （其 `backdrop/src/commonMain/kotlin/com/kyant/backdrop/internal/Shaders.kt` 中的
    `RoundedRectRefractionShaderString` / `RoundedRectRefractionWithDispersionShaderString`）
  - `DampedDragAnimation`、`InteractiveHighlight` 与 `LiquidBottomTabs` 交互设计

## 3. compose-miuix-ui / miuix（Miuix）

- 仓库：https://github.com/compose-miuix-ui/miuix
- 许可：**Apache License 2.0**（其 Maven POM 声明 "The Apache Software License, Version 2.0"）
- 本项目实际使用的内容：
  - **作为依赖使用**：`top.yukonga.miuix.kmp:miuix-ui-android:0.9.3`
    —— 提供 HyperOS 风格的 `Card` / `Switch` / `Button` / `TextButton` / `IconButton` / `HorizontalDivider` /
    `LinearProgressIndicator` / `BasicComponent` 等组件，以及 `MiuixTheme` / `Colors` / `darkColorScheme`，
    是「HyperOS 主题」分支的组件来源（见 `theme/MiuixComponents.kt`）
  - **作为依赖使用**：`top.yukonga.miuix.kmp:miuix-blur-android:0.9.3`
    —— 提供 `drawBackdrop` / `layerBackdrop` / `blur` / `colorControls` / `runtimeShaderEffect` /
    `Highlight`（BloomStroke）/ `rememberDeviceTilt` 等底层能力，是真实模糊与折射的基础
  - 其官方示例 `LiquidGlassNavigationBar` 为底栏交互的来源之一
    （KernelSU 的 `liquid/*` 文件头注明 "Mirrored from compose-miuix-ui example"）

## 4. Google Material Color Utilities（MCU）与 AndroidX Palette

- 仓库：https://github.com/material-foundation/material-color-utilities
  （本项目用其 Kotlin 移植：https://github.com/jordond/materialkolor ）
- 许可：**Apache License 2.0**
- 本项目实际使用的内容（作为依赖使用）：
  - `com.materialkolor:material-color-utilities:4.1.1`
    —— `SchemeTonalSpot` / `SchemeVibrant` / `SchemeExpressive` / `SchemeContent` 与 `Hct`，
    用于把种子色推导为完整的 Material 3 双套配色（见 `theme/dynamic/DynamicColorSchemes.kt`）
  - `androidx.palette:palette:1.0.0`
    —— 从图片中提取代表色作为种子色（见 `theme/dynamic/SeedExtractor.kt`）

---

## 关于本仓库自有代码

除上述明确标注的文件外，本仓库的其余部分（主题装配与过渡、Miuix 桥接层的组件设计、莫奈取色链路、
底栏三形态与伪玻璃回退、主题设置页与预览示意图、演示 App、公开 API 的整理与文档）为原创，
同样以 GPL-3.0-or-later 发布。

本仓库的玻璃底栏代码与 [LiquidGlassBar](https://github.com/wzhdgithub/LiquidGlassBar) **同源**：
后者是从 tempmail 应用抽出的通用库，本仓库保留的是与主题系统耦合的那一版
（主题感知的容器色、Snackbar 避让、三形态切换）。

## 依赖清单

| 组件 | 许可 |
|---|---|
| AndroidX / Jetpack Compose（BOM 2026.05.01） | Apache-2.0 |
| AndroidX Core KTX / Activity Compose | Apache-2.0 |
| AndroidX Palette 1.0.0 | Apache-2.0 |
| Miuix miuix-ui / miuix-blur 0.9.3 | Apache-2.0 |
| Material Color Utilities（Kotlin 移植 4.1.1） | Apache-2.0 |