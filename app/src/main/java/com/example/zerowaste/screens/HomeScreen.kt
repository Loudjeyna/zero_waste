package com.example.zerowaste.ui.screens
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Kitchen
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.zerowaste.data.local.FoodItem
import com.example.zerowaste.viewmodel.FoodItemViewModel
import java.time.LocalDate
import java.time.temporal.ChronoUnit

private enum class HomeTab { OVERVIEW, INVENTORY }

// 1. دالة ذكية لربط اسم الطعام برمز تعبيري خاص به
// دالة ذكية لربط اسم الطعام برمز تعبيري خاص به (عربي وإنجليزي)
fun getFoodEmoji(name: String): String {
    return when (name) {
        // خضروات
        "طماطم", "Tomato" -> "🍅"
        "خيار", "Cucumber" -> "🥒"
        "بصل", "Onion" -> "🧅"
        "بطاطس", "Potato" -> "🥔"
        "جزر", "Carrot" -> "🥕"
        "خس", "Lettuce" -> "🥬"
        // ألبان
        "حليب", "Milk" -> "🥛"
        "جبن", "Cheese" -> "🧀"
        "زبادي", "Yogurt" -> "🥣"
        "بيض", "Egg" -> "🥚"
        // لحوم
        "دجاج", "Chicken" -> "🍗"
        "لحم بقري", "Beef" -> "🥩"
        "سمك", "Fish" -> "🐟"
        // حبوب
        "أرز", "Rice" -> "🍚"
        "مكرونة", "Pasta" -> "🍝"
        "خبز", "Bread" -> "🍞"
        "شوفان", "Oats" -> "🥜"
        // فواكه
        "تفاح", "Apple" -> "🍎"
        "موز", "Banana" -> "🍌"
        "برتقال", "Orange" -> "🍊"
        "عنب", "Grapes" -> "🍇"
        // أخرى
        else -> "🥫"
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    isEnglish: Boolean,
    onToggleLanguage: () -> Unit,
    viewModel: FoodItemViewModel = hiltViewModel()
) {
    val foodItems by viewModel.allFoodItems.collectAsState()
    var showAddScreen by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(HomeTab.OVERVIEW) }

    // التعامل مع زر الرجوع في الهاتف
    BackHandler(enabled = showAddScreen) {
        showAddScreen = false // إغلاق شاشة الإضافة عند الرجوع
    }

    BackHandler(enabled = selectedTab == HomeTab.INVENTORY) {
        selectedTab = HomeTab.OVERVIEW // العودة للرئيسية عند الرجوع من مخزوني
    }

    if (showAddScreen) {
        AddItemScreen(
            isEnglish = isEnglish,
            onAddItem = { item ->
                viewModel.addFoodItem(item)
                showAddScreen = false
                selectedTab = HomeTab.OVERVIEW
            },
            onBack = { showAddScreen = false }
        )
        return
    }

    val today = LocalDate.now()

    // 2. فصل العناصر: العاجلة (3 أيام أو أقل) و البعيدة
    val urgentItems = foodItems.filter {
        ChronoUnit.DAYS.between(today, it.expiryDate) in 0..3
    }
    val safeItems = foodItems.filter {
        ChronoUnit.DAYS.between(today, it.expiryDate) > 3
    }
    val expiredCount = foodItems.count {
        ChronoUnit.DAYS.between(today, it.expiryDate) < 0
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ZeroWaste",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (selectedTab == HomeTab.OVERVIEW) {
                                if (isEnglish) "Consume consciously, waste less" else "نستهلك بوعي، ونهدر أقل"
                            } else {
                                if (isEnglish) "Everything in your fridge in one place" else "كل ما في ثلاجتك بمكان واحد"
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // زر تبديل اللغة
                    IconButton(onClick = onToggleLanguage) {
                        Icon(
                            imageVector = Icons.Filled.Language,
                            contentDescription = "Language",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    // زر تبديل الوضع الداكن
                    IconButton(onClick = onToggleTheme) {
                        Icon(
                            imageVector = if (isDarkMode) Icons.Filled.WbSunny else Icons.Filled.NightsStay,
                            contentDescription = "Theme",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = { selectedTab = HomeTab.INVENTORY }) {
                        Icon(Icons.Filled.NotificationsNone, contentDescription = "Notifications", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
                NavigationBarItem(
                    selected = selectedTab == HomeTab.OVERVIEW,
                    onClick = { selectedTab = HomeTab.OVERVIEW },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text(if (isEnglish) "Home" else "الرئيسية") }
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.INVENTORY,
                    onClick = { selectedTab = HomeTab.INVENTORY },
                    icon = { Icon(Icons.Filled.Kitchen, contentDescription = null) },
                    label = { Text(if (isEnglish) "Inventory" else "مخزوني") }
                )
            }
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showAddScreen = true },
                icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                text = { Text(if (isEnglish) "Add Food" else "إضافة طعام", fontWeight = FontWeight.SemiBold) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        when (selectedTab) {
            HomeTab.OVERVIEW -> OverviewContent(
                isEnglish = isEnglish,
                urgentItems = urgentItems,
                urgentCount = urgentItems.size,
                expiredCount = expiredCount,
                safeCount = safeItems.size,
                onDelete = { viewModel.deleteFoodItem(it) },
                onOpenInventory = { selectedTab = HomeTab.INVENTORY },
                modifier = Modifier.padding(innerPadding)
            )
            HomeTab.INVENTORY -> InventoryContent(
                isEnglish = isEnglish,
                foodItems = safeItems, // عرض العناصر البعيدة فقط هنا
                onDelete = { viewModel.deleteFoodItem(it) },
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun OverviewContent(
    isEnglish: Boolean,
    urgentItems: List<FoodItem>,
    urgentCount: Int,
    expiredCount: Int,
    safeCount: Int,
    onDelete: (FoodItem) -> Unit,
    onOpenInventory: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 12.dp, bottom = 104.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(30.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Column(Modifier.padding(22.dp)) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.14f)
                    ) {
                        Text(
                            if (isEnglish) "Small habits, big impact 🌱" else "عادات صغيرة، أثر كبير 🌱",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        if (isEnglish) "Welcome to a sustainable kitchen" else "أهلًا بك في مطبخ أكثر استدامة",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        if (isEnglish) "Track your food expiry and use it in time." else "تابع صلاحية أطعمتك واستخدمها في الوقت المناسب.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f)
                    )
                    Spacer(Modifier.height(22.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SummaryPill(if (isEnglish) "Expiring" else "قريب الانتهاء", urgentCount.toString(), Modifier.weight(1f))
                        SummaryPill(if (isEnglish) "Safe" else "آمنة", safeCount.toString(), Modifier.weight(1f))
                        SummaryPill(if (isEnglish) "Expired" else "منتهية", expiredCount.toString(), Modifier.weight(1f))
                    }
                }
            }
        }

        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    Text(
                        if (isEnglish) "Food needing immediate use" else "أطعمة تحتاج لاستخدام فوري",
                        style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold
                    )
                    Text(
                        if (isEnglish) "Watch what needs to be used first" else "راقب ما يحتاج إلى استخدام أولًا",
                        style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                androidx.compose.material3.TextButton(onClick = onOpenInventory) {
                    Text(if (isEnglish) "View Stock" else "عرض المخزون")
                }
            }
        }

        if (urgentItems.isEmpty()) {
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                        Box(
                            modifier = Modifier.size(52.dp).clip(RoundedCornerShape(17.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) { Text("✅", style = MaterialTheme.typography.headlineSmall) }
                        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                if (isEnglish) "No urgent food!" else "لا يوجد طعام عاجل!",
                                style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
                            )
                            Text(
                                if (isEnglish) "All your safe food is not expiring soon." else "كل أطعمتك الآمنة لا تنتهي صلاحيتها قريباً.",
                                style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        } else {
            items(urgentItems, key = { it.id }) { item ->
                FoodItemCard(isEnglish = isEnglish, item = item, onDelete = { onDelete(item) })
            }
        }
    }
}

@Composable
private fun InventoryContent(
    isEnglish: Boolean,
    foodItems: List<FoodItem>,
    onDelete: (FoodItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 16.dp, bottom = 104.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp), modifier = Modifier.padding(bottom = 4.dp)) {
                Text(
                    if (isEnglish) "Inventory" else "مخزوني",
                    style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold
                )
                Text(
                    if (isEnglish) "You have ${foodItems.size} safe long-term items" else "لديك ${foodItems.size} من الأطعمة الآمنة بعيدة المدى",
                    style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        if (foodItems.isEmpty()) {
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 36.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(84.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondaryContainer),
                            contentAlignment = Alignment.Center
                        ) { Text("🥬", style = MaterialTheme.typography.headlineLarge) }
                        Text(
                            if (isEnglish) "Long-term stock is empty!" else "المخزون البعيد فارغ!",
                            style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (isEnglish) "Add food and track its expiry to use it in time." else "أضف الأطعمة وتابع تواريخ صلاحيتها لتستخدمها في الوقت المناسب.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            items(foodItems, key = { it.id }) { item ->
                FoodItemCard(isEnglish = isEnglish, item = item, onDelete = { onDelete(item) })
            }
        }
    }
}

@Composable
private fun SummaryPill(title: String, value: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, shape = RoundedCornerShape(17.dp), color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.14f)) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary)
            Text(title, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun FoodItemCard(isEnglish: Boolean, item: FoodItem, onDelete: () -> Unit) {
    val daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), item.expiryDate)
    val statusText: String
    val statusColor: Color
    val statusBackground: Color

    when {
        daysLeft < 0 -> {
            statusText = if (isEnglish) "Expired" else "منتهي الصلاحية"
            statusColor = MaterialTheme.colorScheme.error
            statusBackground = MaterialTheme.colorScheme.errorContainer
        }
        daysLeft <= 3 -> {
            statusText = if (daysLeft == 0L) {
                if (isEnglish) "Expires today" else "تنتهي صلاحيته اليوم"
            } else {
                if (isEnglish) "$daysLeft days left" else "متبقي $daysLeft يوم"
            }
            statusColor = MaterialTheme.colorScheme.onTertiaryContainer
            statusBackground = MaterialTheme.colorScheme.tertiaryContainer
        }
        else -> {
            statusText = if (isEnglish) "$daysLeft days left" else "متبقي $daysLeft يوم"
            statusColor = MaterialTheme.colorScheme.primary
            statusBackground = MaterialTheme.colorScheme.primaryContainer
        }
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 14.dp, end = 6.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(50.dp).clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                // استخدام الدالة الذكية بدلاً من الفئة
                Text(getFoodEmoji(item.name), style = MaterialTheme.typography.titleLarge)
            }
            Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(item.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(
                    listOf(item.category, item.quantity.takeIf { it.isNotBlank() }).filterNotNull().joinToString(" • "),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Surface(shape = RoundedCornerShape(9.dp), color = statusBackground) {
                    Text(statusText, modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp), style = MaterialTheme.typography.labelMedium, color = statusColor, fontWeight = FontWeight.SemiBold)
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}