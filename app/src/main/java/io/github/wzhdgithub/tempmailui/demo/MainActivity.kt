package io.github.wzhdgithub.tempmailui.demo

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.core.view.WindowCompat
import io.github.wzhdgithub.tempmailui.glass.GlassBarItem
import io.github.wzhdgithub.tempmailui.glass.GlassBarSpace
import io.github.wzhdgithub.tempmailui.glass.GlassShell
import io.github.wzhdgithub.tempmailui.glass.isGlassBlurSupported
import io.github.wzhdgithub.tempmailui.theme.THEME_ANIM_MS
import io.github.wzhdgithub.tempmailui.theme.ThemeMode
import io.github.wzhdgithub.tempmailui.theme.ThemeStyle
import io.github.wzhdgithub.tempmailui.theme.TempMailTheme
import io.github.wzhdgithub.tempmailui.theme.ThemedCard
import io.github.wzhdgithub.tempmailui.theme.ThemedDivider
import io.github.wzhdgithub.tempmailui.theme.ThemedDropdownValue
import io.github.wzhdgithub.tempmailui.theme.ThemedIconButton
import io.github.wzhdgithub.tempmailui.theme.ThemedListRow
import io.github.wzhdgithub.tempmailui.theme.ThemedSegmentedTabs
import io.github.wzhdgithub.tempmailui.theme.ThemedSwitch
import io.github.wzhdgithub.tempmailui.theme.cornerRadiusOf
import io.github.wzhdgithub.tempmailui.theme.themedBarContainerColor
import io.github.wzhdgithub.tempmailui.theme.themedSurfaceColors
import io.github.wzhdgithub.tempmailui.theme.dynamic.DefaultMonetSeed
import io.github.wzhdgithub.tempmailui.theme.dynamic.DynamicStyle
import io.github.wzhdgithub.tempmailui.theme.dynamic.MonetPresets
import io.github.wzhdgithub.tempmailui.theme.dynamic.NoDynamicSeed
import io.github.wzhdgithub.tempmailui.theme.dynamic.extractSeedFromUri
import kotlinx.coroutines.launch

/**
 * TempMail-UI 演示 App。
 *
 * 把库里的 UI 实现完整跑起来，作为「UI 实现」的可运行示例：
 *   - 主题系统：Material3 / HyperOS(Miuix) 两套主题 + 莫奈取色（seed/风格/对比度）
 *   - 底栏三形态：悬浮 / 贴边 / 液态玻璃（真实模糊折射，API 33+ 自动启用）
 *   - 主题设置页：手机预览示意图、明暗三选一、强调色下拉、底栏开关组
 *
 * 与正式应用的区别：这里用一份精简的本地状态（DemoState）替代应用的 AppState，
 * 文案直接写中文（正式应用走 15 语言的 Strings i18n 系统），页面内容简化为示意卡片。
 */
private enum class DemoTab(val icon: ImageVector, val label: String) {
    Inbox(Icons.Default.Email, "收件箱"),
    History(Icons.Default.DateRange, "历史"),
    Settings(Icons.Default.Settings, "设置")
}

/**
 * 底栏形态，仅在 HyperOS 主题下可选：
 *   Edge       = 贴边底栏（液态玻璃不可用）
 *   Float      = 普通悬浮底栏
 *   LiquidGlass= 悬浮液态玻璃底栏
 */
private enum class BarStyle(val key: String) {
    Float("float"),
    LiquidGlass("liquid_glass"),
    Edge("edge");

    companion object {
        fun fromKey(key: String?): BarStyle = entries.find { it.key == key } ?: Float
    }
}

/** 演示用状态（正式应用为 AppState + SharedPreferences 持久化）。 */
private data class DemoState(
    val themeStyle: ThemeStyle = ThemeStyle.Material3,
    val themeMode: ThemeMode = ThemeMode.System,
    val dynamicSeed: Int = NoDynamicSeed,
    val dynamicStyle: DynamicStyle = DynamicStyle.TonalSpot,
    val dynamicContrast: Float = 0f,
    val barStyle: BarStyle = BarStyle.Float,
    val glassBlurEnabled: Boolean = true,
    val tab: DemoTab = DemoTab.Inbox,
    val themePageOpen: Boolean = false
)

private val DemoStateSaver = listSaver<DemoState, Any>(
    save = {
        listOf(
            it.themeStyle.key, it.themeMode.key, it.dynamicSeed, it.dynamicStyle.key,
            it.dynamicContrast, it.barStyle.key, it.glassBlurEnabled, it.tab.name, it.themePageOpen
        )
    },
    restore = {
        DemoState(
            themeStyle = ThemeStyle.fromKey(it[0] as String),
            themeMode = ThemeMode.fromKey(it[1] as String),
            dynamicSeed = it[2] as Int,
            dynamicStyle = DynamicStyle.fromKey(it[3] as String),
            dynamicContrast = it[4] as Float,
            barStyle = BarStyle.fromKey(it[5] as String),
            glassBlurEnabled = it[6] as Boolean,
            tab = DemoTab.valueOf(it[7] as String),
            themePageOpen = it[8] as Boolean
        )
    }
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { DemoApp() }
    }
}

@Composable
private fun DemoApp() {
    var state by rememberSaveable(stateSaver = DemoStateSaver) { mutableStateOf(DemoState()) }
    val snackbar = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val darkTheme = state.themeMode.isDark(isSystemInDarkTheme())

    // 状态栏图标明暗跟随应用主题（而不是系统主题），避免"深色主题 + 深色图标"
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    TempMailTheme(
        darkTheme = darkTheme,
        themeStyle = state.themeStyle,
        // 未启用莫奈时传 null：两套主题的配色都与启用前逐像素一致
        dynamicSeed = state.dynamicSeed.takeIf { it != NoDynamicSeed },
        dynamicStyle = state.dynamicStyle,
        dynamicContrast = state.dynamicContrast
    ) {
        // 真·液态玻璃：HyperOS 主题 + 液态玻璃底栏 + 模糊开启 + API 33+（RuntimeShader）时启用
        val glassActive = state.themeStyle == ThemeStyle.HyperOS &&
            state.barStyle == BarStyle.LiquidGlass &&
            state.glassBlurEnabled &&
            isGlassBlurSupported()
        // 玻璃模式下内容铺满整屏，页面需在滚动内容末尾预留底栏高度
        val overlap = if (glassActive) GlassBarSpace else 0.dp

        LaunchedEffect(glassActive) {
            if (glassActive) snackbar.showSnackbar("长按底栏可左右拖动切换标签")
        }

        GlassShell(
            glass = glassActive,
            darkTheme = darkTheme,
            items = DemoTab.entries.map { GlassBarItem(it.icon, it.label) },
            selectedIndex = DemoTab.entries.indexOf(state.tab),
            onSelect = { state = state.copy(tab = DemoTab.entries[it], themePageOpen = false) },
            snackbarHost = { SnackbarHost(snackbar) },
            fallbackBar = { DemoFallbackBar(state) { state = it } }
        ) { contentPadding ->
            when (state.tab) {
                DemoTab.Inbox -> InboxPage(contentPadding, overlap)
                DemoTab.History -> HistoryPage(contentPadding, overlap)
                DemoTab.Settings -> SettingsPage(contentPadding, overlap, state, snackbar) { state = it }
            }
        }
    }
}

/**
 * 非玻璃模式下的底栏：Material3 主题走原始 NavigationBar，
 * HyperOS 主题按底栏形态三选一（贴边 / 悬浮 / 伪玻璃回退）。
 */
@Composable
private fun DemoFallbackBar(state: DemoState, onState: (DemoState) -> Unit) {
    val label: (DemoTab) -> String = { it.label }
    if (state.themeStyle == ThemeStyle.Material3) {
        // Material3：原始默认样式，底栏形态开关在此主题下不生效
        NavigationBar {
            DemoTab.entries.forEach { tab ->
                val selected = state.tab == tab
                NavigationBarItem(
                    selected = selected,
                    onClick = { onState(state.copy(tab = tab, themePageOpen = false)) },
                    icon = { Icon(tab.icon, label(tab)) },
                    label = { Text(label(tab)) }
                )
            }
        }
        return
    }
    val onSelect: (DemoTab) -> Unit = { onState(state.copy(tab = it, themePageOpen = false)) }
    when (state.barStyle) {
        BarStyle.Edge -> MiuixEdgeBottomBar(state.tab, label, onSelect)
        BarStyle.Float -> MiuixFloatingBottomBar(state.tab, label, onSelect)
        BarStyle.LiquidGlass -> LiquidGlassBottomBar(state.tab, label, onSelect)
    }
}

// ==================== 页面 ====================

@Composable
private fun InboxPage(contentPadding: PaddingValues, bottomOverlap: Dp) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text(
                "收件箱",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 6.dp)
            )
        }
        // 彩色卡片列表：滚动时可从浮起的玻璃底栏下方穿过，直观看到模糊与折射
        items(24) { index -> MailCard(index) }
        if (bottomOverlap > 0.dp) item { Spacer(Modifier.height(bottomOverlap)) }
    }
}

@Composable
private fun MailCard(index: Int) {
    val accent = Color.hsv((index * 37) % 360f, 0.55f, 0.85f)
    ThemedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(accent)
            )
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text("sender$index@example.com", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(2.dp))
                Text(
                    "第 $index 封演示邮件：内容会从底栏下方滚过",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                "12:%02d".format(index),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun HistoryPage(contentPadding: PaddingValues, bottomOverlap: Dp) {
    val scroll = rememberScrollState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(scroll)
    ) {
        Text(
            "历史",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(20.dp)
        )
        ThemedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column {
                repeat(8) { index ->
                    if (index > 0) ThemedDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ThemedListRow(
                        title = "demo-$index@tempmail.local",
                        summary = if (index == 0) "当前使用中" else "已过期"
                    )
                }
            }
        }
        if (bottomOverlap > 0.dp) Spacer(Modifier.height(bottomOverlap))
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun SettingsPage(
    contentPadding: PaddingValues,
    bottomOverlap: Dp,
    state: DemoState,
    snackbar: SnackbarHostState,
    onState: (DemoState) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(contentPadding)
            .verticalScroll(scroll)
    ) {
        if (state.themePageOpen) {
            ThemeSettingsPage(state, snackbar, onState) { onState(state.copy(themePageOpen = false)) }
        } else {
            Text(
                "设置",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(20.dp)
            )
            ThemedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Column {
                    ThemedListRow(
                        title = "主题设置",
                        summary = "主题风格 / 明暗 / 莫奈取色 / 底栏形态",
                        onClick = { onState(state.copy(themePageOpen = true)) },
                        trailing = {
                            Icon(
                                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                    ThemedDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    ThemedListRow(
                        title = "关于本演示",
                        summary = "TempMail-UI：tempmail 应用的 UI 实现（GPL-3.0-or-later）",
                        onClick = {
                            scope.launch { snackbar.showSnackbar("截图与实现说明见仓库 README") }
                        }
                    )
                }
            }
        }
        if (bottomOverlap > 0.dp) Spacer(Modifier.height(bottomOverlap))
        Spacer(Modifier.height(24.dp))
    }
}

/**
 * 主题设置页：预览示意图 + 主题风格/明暗 + 莫奈取色（含强调色下拉）+ 底栏形态开关。
 *
 * 改动实时生效，返回只是离开本页（与正式应用一致）。
 */
@Composable
private fun ThemeSettingsPage(
    state: DemoState,
    snackbar: SnackbarHostState,
    onState: (DemoState) -> Unit,
    onBack: () -> Unit
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    var extracting by remember { mutableStateOf(false) }
    var accentMenuOpen by remember { mutableStateOf(false) }

    val monetOn = state.dynamicSeed != NoDynamicSeed
    val floatingOn = state.barStyle != BarStyle.Edge

    // 相册取色：Photo Picker 无需申请任何权限；解码与取色都在 SeedExtractor 内部切到 IO 线程
    val picker = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri == null) return@rememberLauncherForActivityResult
        scope.launch {
            extracting = true
            val seed = extractSeedFromUri(ctx, uri)
            extracting = false
            if (seed != null) onState(state.copy(dynamicSeed = seed))
            else snackbar.showSnackbar("取色失败，换一张图试试")
        }
    }

    // 标题栏：左返回 + 居中大标题
    Box(Modifier.fillMaxWidth()) {
        ThemedIconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
        }
        Text(
            "主题设置",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.align(Alignment.Center)
        )
    }
    Spacer(Modifier.height(20.dp))

    // 预览区域：与真实界面同源取色，实时反映明暗 / 种子色 / 底栏形态
    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        ThemePreviewMock(barStyle = state.barStyle, label = { it.label })
    }
    Spacer(Modifier.height(28.dp))

    // 主题风格：正式应用在各自的设置入口里切换，这里并为一行便于对比
    ThemedSegmentedTabs(
        tabs = listOf("Material3", "HyperOS"),
        selectedIndex = ThemeStyle.entries.indexOf(state.themeStyle),
        onSelect = { onState(state.copy(themeStyle = ThemeStyle.entries[it])) }
    )
    Spacer(Modifier.height(12.dp))

    // 明暗三选一：跟随系统 / 浅色 / 深色
    ThemedSegmentedTabs(
        tabs = listOf("跟随系统", "浅色", "深色"),
        selectedIndex = ThemeMode.entries.indexOf(state.themeMode),
        onSelect = { onState(state.copy(themeMode = ThemeMode.entries[it])) }
    )
    Spacer(Modifier.height(16.dp))

    // 卡片1：莫奈取色
    ThemedCard(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Column {
            ThemedListRow(
                title = "莫奈取色",
                icon = { ImageFrameIcon(MaterialTheme.colorScheme.onSurface) },
                trailing = {
                    ThemedSwitch(
                        checked = monetOn,
                        onCheckedChange = { on ->
                            // 开启时先落到「默认」强调色（应用主色蓝），随后可再选预设或图片
                            onState(
                                state.copy(
                                    dynamicSeed = if (on) DefaultMonetSeed else NoDynamicSeed
                                )
                            )
                        }
                    )
                }
            )
            ThemedDivider(modifier = Modifier.padding(horizontal = 16.dp))
            ThemedListRow(
                title = "强调色",
                icon = {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = { accentMenuOpen = true },
                trailing = {
                    ThemedDropdownValue(
                        text = if (monetOn && state.dynamicSeed != DefaultMonetSeed) "自定义"
                        else "默认",
                        onClick = { accentMenuOpen = true }
                    )
                }
            )
            // 零高度锚点：紧贴该行下方，弹出菜单以它为基准向下展开
            Box(Modifier.fillMaxWidth().height(0.dp)) {
                AccentDropdown(
                    expanded = accentMenuOpen,
                    selectedSeed = state.dynamicSeed,
                    extracting = extracting,
                    onDismiss = { accentMenuOpen = false },
                    onPick = { seed -> onState(state.copy(dynamicSeed = seed)) },
                    onPickImage = {
                        accentMenuOpen = false
                        picker.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    }
                )
            }
        }
    }

    // 配色风格 / 对比度：正式应用放在莫奈子页，这里紧跟在取色卡片之后
    if (monetOn) {
        Spacer(Modifier.height(16.dp))
        ThemedCard(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
            Column {
                DynamicStyle.entries.forEachIndexed { index, style ->
                    if (index > 0) ThemedDivider(modifier = Modifier.padding(horizontal = 16.dp))
                    // 风格名沿用 MCU 官方叫法（视为品牌名，不翻译）
                    OptionRow(style.label, state.dynamicStyle == style) {
                        onState(state.copy(dynamicStyle = style))
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        ThemedCard(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
            Column {
                OptionRow("默认对比度", state.dynamicContrast == 0f) {
                    onState(state.copy(dynamicContrast = 0f))
                }
                ThemedDivider(modifier = Modifier.padding(horizontal = 16.dp))
                OptionRow("高对比度", state.dynamicContrast == 0.5f) {
                    onState(state.copy(dynamicContrast = 0.5f))
                }
            }
        }
    }

    Spacer(Modifier.height(16.dp))

    // 卡片2：底栏相关（仅 HyperOS 主题生效，Material3 主题固定用 NavigationBar）
    ThemedCard(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.large) {
        Column {
            ThemedListRow(
                title = "悬浮底栏",
                summary = if (state.themeStyle == ThemeStyle.HyperOS)
                    "关闭后底栏贴边，液态玻璃同时不可用"
                else "当前为 Material3 主题，底栏固定用 NavigationBar",
                icon = { FloatingBarIcon(MaterialTheme.colorScheme.onSurface) },
                trailing = {
                    ThemedSwitch(
                        checked = floatingOn,
                        enabled = state.themeStyle == ThemeStyle.HyperOS,
                        onCheckedChange = { on ->
                            // 关掉悬浮时液态玻璃一并关闭（贴边底栏没有玻璃形态）
                            onState(
                                state.copy(barStyle = if (on) BarStyle.Float else BarStyle.Edge)
                            )
                        }
                    )
                }
            )
            ThemedDivider(modifier = Modifier.padding(horizontal = 16.dp))
            ThemedListRow(
                title = "液态玻璃底栏",
                summary = "API 33+ 时启用真实模糊与折射，否则走伪玻璃回退",
                icon = { GlassDropIcon(MaterialTheme.colorScheme.onSurface) },
                trailing = {
                    ThemedSwitch(
                        checked = state.barStyle == BarStyle.LiquidGlass,
                        enabled = floatingOn && state.themeStyle == ThemeStyle.HyperOS,
                        onCheckedChange = { on ->
                            onState(
                                state.copy(
                                    barStyle = if (on) BarStyle.LiquidGlass else BarStyle.Float
                                )
                            )
                        }
                    )
                }
            )
            ThemedDivider(modifier = Modifier.padding(horizontal = 16.dp))
            ThemedListRow(
                title = "模糊",
                summary = "液态玻璃的总开关，关闭后底栏不透出下方内容",
                icon = { BlurDotsIcon(MaterialTheme.colorScheme.onSurface) },
                trailing = {
                    // 模糊只作用于液态玻璃底栏：未选中玻璃时该项不可用，避免"无效组合"
                    ThemedSwitch(
                        checked = state.glassBlurEnabled,
                        enabled = state.barStyle == BarStyle.LiquidGlass,
                        onCheckedChange = { onState(state.copy(glassBlurEnabled = it)) }
                    )
                }
            )
        }
    }
    Spacer(Modifier.height(24.dp))
}

/**
 * 手机预览示意图（实时预览）：完整反映当前主题的各处变化——
 *   - 配色：页面底色 / 卡片 / 强调容器 / 强调色 / 文字色，全部取自 themedSurfaceColors()
 *     （HyperOS 下就是 Miuix 组件真正在用的那套颜色），并做颜色动画平滑过渡；
 *   - 底栏形态：悬浮（内缩 + 圆角 + 抬离底边）↔ 贴边（满宽 + 直角 + 紧贴底边）平滑位移/形变；
 *   - 尺寸与圆角：机身与色块的圆角取自当前主题的 shapes token，换主题风格时形状随之变化；
 *   - 字体：底栏项文字用当前主题的 labelSmall 渲染，字体样式变化同样可见。
 */
@Composable
private fun ThemePreviewMock(
    barStyle: BarStyle,
    label: (DemoTab) -> String,
    modifier: Modifier = Modifier
) {
    val colors = themedSurfaceColors()
    val shapeSpec = tween<Color>(THEME_ANIM_MS)
    val sizeSpec = tween<Dp>(THEME_ANIM_MS)

    val background by animateColorAsState(colors.background, shapeSpec, label = "previewBackground")
    val card by animateColorAsState(colors.card, shapeSpec, label = "previewCard")
    val accent by animateColorAsState(colors.primary, shapeSpec, label = "previewAccent")
    val accentContainer by animateColorAsState(colors.primaryContainer, shapeSpec, label = "previewAccentContainer")
    val onSurface by animateColorAsState(colors.onSurface, shapeSpec, label = "previewOnSurface")
    val outline by animateColorAsState(colors.outlineVariant, shapeSpec, label = "previewOutline")

    val shapes = MaterialTheme.shapes
    val bodyRadius by animateDpAsState(cornerRadiusOf(shapes.extraLarge), sizeSpec, label = "previewBodyRadius")
    val blockRadius by animateDpAsState(cornerRadiusOf(shapes.small), sizeSpec, label = "previewBlockRadius")

    // 底栏形态：悬浮 = 内缩 + 圆角 + 抬离底边；贴边 = 满宽 + 直角 + 紧贴底边
    val floating = barStyle != BarStyle.Edge
    val barInset by animateDpAsState(if (floating) 8.dp else 0.dp, sizeSpec, label = "previewBarInset")
    val barCorner by animateDpAsState(if (floating) blockRadius else 0.dp, sizeSpec, label = "previewBarCorner")
    val barLift by animateDpAsState(if (floating) 6.dp else 0.dp, sizeSpec, label = "previewBarLift")
    val barHeight by animateDpAsState(if (floating) 30.dp else 34.dp, sizeSpec, label = "previewBarHeight")

    Box(
        modifier = modifier
            .width(132.dp)
            .height(206.dp)
            .clip(RoundedCornerShape(bodyRadius))
            .background(background)
            .border(1.5.dp, outline, RoundedCornerShape(bodyRadius))
    ) {
        // 内容区（含底部为底栏预留的高度，避免与底栏重叠）
        Column(
            Modifier
                .fillMaxSize()
                .padding(start = 10.dp, top = 10.dp, end = 10.dp)
                .padding(bottom = barHeight + barLift + 6.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    Modifier.weight(1f).height(30.dp)
                        .clip(RoundedCornerShape(blockRadius)).background(accentContainer)
                )
                Box(
                    Modifier.weight(1f).height(30.dp)
                        .clip(RoundedCornerShape(blockRadius)).background(card)
                )
            }
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier.fillMaxWidth().weight(1f)
                    .clip(RoundedCornerShape(blockRadius)).background(card)
            )
        }

        // 底栏：与真实底栏同构（首项强调色），形态随 BarStyle 平滑变化
        Column(
            Modifier
                .align(Alignment.BottomCenter)
                .padding(start = barInset, end = barInset, bottom = barLift)
                .fillMaxWidth()
                .clip(RoundedCornerShape(barCorner))
                .background(card)
        ) {
            // 贴边形态用一条顶部分割线与内容分层（悬浮形态下分割线随圆角淡出）
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .graphicsLayer { alpha = if (floating) 0f else 1f }
                    .background(outline)
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .padding(horizontal = if (floating) 6.dp else 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DemoTab.entries.forEachIndexed { index, tab ->
                    val selected = index == 0
                    Column(
                        Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
                    ) {
                        Box(
                            Modifier
                                .size(12.dp)
                                .clip(RoundedCornerShape(3.dp))
                                .background(if (selected) accent else onSurface)
                        )
                        Text(
                            text = label(tab),
                            // 沿用主题 labelSmall 的字体族与字重，只把字号缩到示意用的尺寸
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 7.sp,
                                lineHeight = 8.sp
                            ),
                            color = if (selected) accent else onSurface,
                            maxLines = 1,
                            softWrap = false
                        )
                    }
                }
            }
        }
    }
}

/**
 * 「强调色」下拉菜单：默认（应用主色蓝）/ 预设色带 / 从图片取色。
 * 用 Popup 锚定在强调色行下方，带淡入 + 轻微缩放的过渡（与底栏放大镜的出现动效一致）。
 */
@Composable
private fun AccentDropdown(
    expanded: Boolean,
    selectedSeed: Int,
    extracting: Boolean,
    onDismiss: () -> Unit,
    onPick: (Int) -> Unit,
    onPickImage: () -> Unit
) {
    if (!expanded) return
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) { progress.animateTo(1f, tween(180)) }

    Popup(
        alignment = Alignment.TopEnd,
        offset = IntOffset(0, 6),
        onDismissRequest = onDismiss,
        properties = PopupProperties(focusable = true)
    ) {
        ThemedCard(
            modifier = Modifier
                .width(268.dp)
                .shadow(8.dp, RoundedCornerShape(20.dp))
                .graphicsLayer {
                    alpha = progress.value
                    val scale = 0.94f + 0.06f * progress.value
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(1f, 0f)
                },
            shape = MaterialTheme.shapes.large
        ) {
            Column {
                AccentMenuRow(
                    label = "默认",
                    selected = selectedSeed == DefaultMonetSeed,
                    swatch = Color(DefaultMonetSeed),
                    onClick = { onPick(DefaultMonetSeed); onDismiss() }
                )
                ThemedDivider(modifier = Modifier.padding(horizontal = 16.dp))
                Text(
                    "预设色",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 20.dp, top = 12.dp)
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MonetPresets.forEach { preset ->
                        Box(
                            Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(preset))
                                .border(
                                    width = if (selectedSeed == preset) 3.dp else 1.dp,
                                    color = if (selectedSeed == preset) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                )
                                .clickable { onPick(preset); onDismiss() }
                        )
                    }
                }
                ThemedDivider(modifier = Modifier.padding(horizontal = 16.dp))
                AccentMenuRow(
                    label = if (extracting) "取色中…" else "从图片取色",
                    selected = selectedSeed != DefaultMonetSeed && selectedSeed !in MonetPresets
                        && selectedSeed != NoDynamicSeed,
                    icon = {
                        Icon(
                            Icons.Default.Create,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    onClick = onPickImage
                )
            }
        }
    }
}

/** 下拉菜单中的一行：可选色点 + 文案（选中时显示对勾）。 */
@Composable
private fun AccentMenuRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    swatch: Color? = null,
    icon: (@Composable () -> Unit)? = null
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (swatch != null) {
            Box(Modifier.size(22.dp).clip(CircleShape).background(swatch))
            Spacer(Modifier.width(14.dp))
        } else if (icon != null) {
            icon()
            Spacer(Modifier.width(14.dp))
        }
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        if (selected) {
            Icon(
                Icons.Default.Check, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp)
            )
        }
    }
}

/** 单选项行：选中时右侧显示对勾（用于配色风格 / 对比度这类互斥选项）。 */
@Composable
private fun OptionRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        if (selected) {
            Icon(
                Icons.Default.Check, contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/** 色点：预设色可点击；用于展示当前配色的一组不可点击。 */
@Composable
private fun ColorDot(color: Color, selected: Boolean, onClick: (() -> Unit)? = null) {
    Box(
        Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .border(
                width = if (selected) 3.dp else 1.dp,
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outlineVariant,
                shape = CircleShape
            )
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
    )
}

// ==================== 底栏三形态（HyperOS 主题） ====================

/**
 * Miuix 悬浮底栏：内缩 + 圆角 + 投影的浮起形态。
 */
@Composable
private fun MiuixFloatingBottomBar(
    current: DemoTab,
    label: (DemoTab) -> String,
    onSelect: (DemoTab) -> Unit
) {
    // 指示器必须用不透明色：M3 绘制时以 .copy(alpha = animationProgress) 覆盖该色的 alpha，
    // 传入带透明度的颜色会被静默还原成实心色。故按 12% 比例预先合成到容器色上。
    // 容器色走主题桥接：开启莫奈后取带色调的 Miuix 容器色，不再是固定白色
    val barColor = themedBarContainerColor()
    val indicatorColor = MaterialTheme.colorScheme.primary
        .copy(alpha = 0.12f)
        .compositeOver(barColor)
    NavigationBar(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 18.dp)
            .padding(bottom = 12.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(28.dp), clip = true),
        containerColor = barColor,
        tonalElevation = 0.dp,
        windowInsets = WindowInsets(0.dp),
        content = {
            DemoTab.entries.forEach { tab ->
                val selected = current == tab
                NavigationBarItem(
                    selected = selected,
                    onClick = { onSelect(tab) },
                    icon = {
                        Icon(
                            tab.icon,
                            label(tab),
                            tint = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    label = {
                        Text(
                            label(tab),
                            color = if (selected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(indicatorColor = indicatorColor)
                )
            }
        }
    )
}

/**
 * 贴边底栏：悬浮关闭时的形态。与悬浮底栏同一套色板与选中态规则，但**不浮起**——
 * 铺满整宽、不留横向边距、不投影，靠顶部一条细分隔线与内容分层，背景一直延伸到屏幕底边。
 */
@Composable
private fun MiuixEdgeBottomBar(
    current: DemoTab,
    label: (DemoTab) -> String,
    onSelect: (DemoTab) -> Unit
) {
    val barColor = themedBarContainerColor()
    Column(
        Modifier
            .fillMaxWidth()
            .background(barColor)
    ) {
        ThemedDivider()
        Row(
            Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .height(56.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            DemoTab.entries.forEach { tab ->
                val selected = current == tab
                val contentColor = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
                Column(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .selectable(
                            selected = selected,
                            interactionSource = null,
                            indication = null,
                            role = Role.Tab,
                            onClick = { onSelect(tab) }
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
                ) {
                    Icon(tab.icon, label(tab), tint = contentColor, modifier = Modifier.size(24.dp))
                    Text(label(tab), style = MaterialTheme.typography.labelSmall, color = contentColor)
                }
            }
        }
    }
}

/**
 * 液态玻璃底栏的伪玻璃回退实现（API 33 以下或模糊不可用，以及用户关闭「模糊」时）：
 * 玻璃本体 + 高光描边 + 外层柔和阴影 + 滑动指示器 + 按压缩放回弹。
 *
 * 真实模糊/折射由库里的 GlassBottomBar.kt 在 API 33+ 提供，本组件仅以渐变 + 描边 + 阴影
 * 模拟玻璃质感，保证低版本观感一致性与稳定性。
 */
@Composable
private fun LiquidGlassBottomBar(
    current: DemoTab,
    label: (DemoTab) -> String,
    onSelect: (DemoTab) -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    val tabs = DemoTab.entries
    val glassShape = RoundedCornerShape(28.dp)
    // 玻璃层次：不透明基底之上叠一层极淡的纵向明暗（顶部受光、底部压暗），形成厚度。
    // 基底必须不透明 —— 否则 10dp 阴影会从半透明本体下方透出，把底栏越往底部压得越灰。
    val bodyBrush = Brush.verticalGradient(
        listOf(
            Color.White.copy(alpha = 0.10f),
            Color.Transparent,
            Color.Black.copy(alpha = 0.05f)
        )
    )
    val edgeBrush = Brush.linearGradient(
        listOf(
            Color.White.copy(alpha = 0.70f),
            Color.White.copy(alpha = 0.10f),
            scheme.primary.copy(alpha = 0.22f)
        )
    )
    // 不透明基底色：走主题桥接，开启莫奈后为带色调的 Miuix 容器色（未开启时与原来的 surface 同值）
    val baseColor = themedBarContainerColor()
    val indicatorColor = scheme.primary.copy(alpha = 0.16f).compositeOver(baseColor)
    Box(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(horizontal = 18.dp)
            .padding(bottom = 12.dp)
            .fillMaxWidth()
            .height(64.dp)
    ) {
        // 阴影层：不透明底色承载投影，使阴影只出现在圆角轮廓之外
        Box(
            Modifier
                .matchParentSize()
                .shadow(elevation = 10.dp, shape = glassShape, clip = true)
                .background(baseColor, glassShape)
        )
        BoxWithConstraints(
            Modifier
                .matchParentSize()
                .background(bodyBrush, glassShape)
                .border(1.dp, edgeBrush, glassShape)
        ) {
            val itemWidth = maxWidth / tabs.size
            val indicatorWidth = minOf(64.dp, itemWidth - 8.dp)
            val index = tabs.indexOf(current).coerceAtLeast(0)
            val rtl = LocalLayoutDirection.current == LayoutDirection.Rtl
            val slot = if (rtl) tabs.size - 1 - index else index
            val indicatorX by animateDpAsState(
                targetValue = itemWidth * slot + (itemWidth - indicatorWidth) / 2,
                animationSpec = spring(dampingRatio = 0.78f, stiffness = Spring.StiffnessMediumLow),
                label = "glassIndicatorX"
            )
            // 滑动指示器：绘制在图标之下
            Box(
                Modifier
                    .offset(x = indicatorX, y = 8.dp)
                    .size(indicatorWidth, 32.dp)
                    .background(indicatorColor, RoundedCornerShape(percent = 50))
            )
            Row(
                Modifier.fillMaxSize().selectableGroup(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                tabs.forEach { tab ->
                    GlassTabItem(
                        modifier = Modifier.weight(1f),
                        tab = tab,
                        selected = current == tab,
                        label = label(tab),
                        onClick = { onSelect(tab) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GlassTabItem(
    modifier: Modifier,
    tab: DemoTab,
    selected: Boolean,
    label: String,
    onClick: () -> Unit
) {
    val scheme = MaterialTheme.colorScheme
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    // 按压回弹：按下收缩、松开弹回，模拟玻璃被按压的液体反馈
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.86f else 1f,
        animationSpec = spring(dampingRatio = 0.42f, stiffness = Spring.StiffnessMedium),
        label = "glassPressScale"
    )
    Column(
        modifier = modifier
            .fillMaxHeight()
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .selectable(
                selected = selected,
                interactionSource = interaction,
                indication = null,
                role = Role.Tab,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            tab.icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = if (selected) scheme.primary else scheme.onSurfaceVariant
        )
        Spacer(Modifier.height(2.dp))
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) scheme.primary else scheme.onSurfaceVariant
        )
    }
}

// ==================== 手绘图标（Material 图标集中没有对应图形） ====================

/** 图片取色图标：圆角画框 + 太阳 + 山形。 */
@Composable
private fun ImageFrameIcon(tint: Color) {
    Canvas(Modifier.size(24.dp)) {
        val stroke = size.width * 0.085f
        drawRoundRect(
            color = tint,
            topLeft = Offset(stroke / 2f, stroke / 2f),
            size = Size(size.width - stroke, size.height - stroke),
            cornerRadius = CornerRadius(size.width * 0.2f),
            style = Stroke(width = stroke)
        )
        drawCircle(
            color = tint, radius = size.width * 0.075f,
            center = Offset(size.width * 0.33f, size.height * 0.35f)
        )
        val hill = Path().apply {
            moveTo(size.width * 0.20f, size.height * 0.76f)
            lineTo(size.width * 0.43f, size.height * 0.50f)
            lineTo(size.width * 0.60f, size.height * 0.68f)
            lineTo(size.width * 0.70f, size.height * 0.58f)
            lineTo(size.width * 0.80f, size.height * 0.76f)
            close()
        }
        drawPath(hill, tint)
    }
}

/** 模糊图标：半调网点（右下角逐渐变淡变小，形成"虚化"观感）。 */
@Composable
private fun BlurDotsIcon(tint: Color) {
    Canvas(Modifier.size(24.dp)) {
        val step = size.width / 4f
        repeat(4) { row ->
            repeat(4) { col ->
                val fade = 1f - (row + col) / 7f
                drawCircle(
                    color = tint.copy(alpha = 0.35f + 0.65f * fade),
                    radius = step * 0.17f * (0.6f + 0.4f * fade),
                    center = Offset(step * (col + 0.5f), step * (row + 0.5f))
                )
            }
        }
    }
}

/** 悬浮底栏图标：圆角矩形机身 + 底部的实心条。 */
@Composable
private fun FloatingBarIcon(tint: Color) {
    Canvas(Modifier.size(24.dp)) {
        val stroke = size.width * 0.085f
        drawRoundRect(
            color = tint,
            topLeft = Offset(stroke / 2f, stroke / 2f),
            size = Size(size.width - stroke, size.height - stroke),
            cornerRadius = CornerRadius(size.width * 0.2f),
            style = Stroke(width = stroke)
        )
        val barHeight = size.height * 0.22f
        drawRoundRect(
            color = tint,
            topLeft = Offset(size.width * 0.24f, size.height * 0.62f),
            size = Size(size.width * 0.52f, barHeight),
            cornerRadius = CornerRadius(barHeight / 2f)
        )
    }
}

/** 液态玻璃图标：水滴。 */
@Composable
private fun GlassDropIcon(tint: Color) {
    Canvas(Modifier.size(24.dp)) {
        val r = size.width * 0.29f
        val center = Offset(size.width / 2f, size.height * 0.64f)
        drawCircle(color = tint, radius = r, center = center)
        val path = Path().apply {
            moveTo(size.width / 2f, size.height * 0.06f)
            lineTo(center.x + r * 0.82f, center.y - r * 0.5f)
            lineTo(center.x - r * 0.82f, center.y - r * 0.5f)
            close()
        }
        drawPath(path, tint)
    }
}