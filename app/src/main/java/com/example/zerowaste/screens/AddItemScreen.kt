package com.example.zerowaste.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.zerowaste.data.local.FoodItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.Column

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddItemScreen(
    isEnglish: Boolean,
    onAddItem: (FoodItem) -> Unit,
    onBack: () -> Unit
) {
    var category by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var quantityNumber by remember { mutableStateOf("") }
    var quantityUnit by remember { mutableStateOf("") }

    val categories = if (isEnglish) listOf("Vegetables", "Dairy", "Meat", "Grains", "Fruits", "Other")
    else listOf("خضروات", "ألبان", "لحوم", "حبوب", "فواكه", "أخرى")

    val itemsMap = if (isEnglish) mapOf(
        "Vegetables" to listOf("Tomato", "Cucumber", "Onion", "Potato", "Carrot", "Lettuce"),
        "Dairy" to listOf("Milk", "Cheese", "Yogurt", "Egg"),
        "Meat" to listOf("Chicken", "Beef", "Fish"),
        "Grains" to listOf("Rice", "Pasta", "Bread", "Oats"),
        "Fruits" to listOf("Apple", "Banana", "Orange", "Grapes"),
        "Other" to listOf("Misc")
    ) else mapOf(
        "خضروات" to listOf("طماطم", "خيار", "بصل", "بطاطس", "جزر", "خس"),
        "ألبان" to listOf("حليب", "جبن", "زبادي", "بيض"),
        "لحوم" to listOf("دجاج", "لحم بقري", "سمك"),
        "حبوب" to listOf("أرز", "مكرونة", "خبز", "شوفان"),
        "فواكه" to listOf("تفاح", "موز", "برتقال", "عنب"),
        "أخرى" to listOf("متنوع")
    )

    val shelfLifeMap = if (isEnglish) mapOf(
        "Tomato" to 7, "Cucumber" to 5, "Onion" to 30, "Potato" to 30, "Carrot" to 21, "Lettuce" to 5,
        "Milk" to 7, "Cheese" to 14, "Yogurt" to 10, "Egg" to 21,
        "Chicken" to 2, "Beef" to 3, "Fish" to 1,
        "Rice" to 365, "Pasta" to 365, "Bread" to 5, "Oats" to 365,
        "Apple" to 14, "Banana" to 5, "Orange" to 14, "Grapes" to 7,
        "Misc" to 7
    ) else mapOf(
        "طماطم" to 7, "خيار" to 5, "بصل" to 30, "بطاطس" to 30, "جزر" to 21, "خس" to 5,
        "حليب" to 7, "جبن" to 14, "زبادي" to 10, "بيض" to 21,
        "دجاج" to 2, "لحم بقري" to 3, "سمك" to 1,
        "أرز" to 365, "مكرونة" to 365, "خبز" to 5, "شوفان" to 365,
        "تفاح" to 14, "موز" to 5, "برتقال" to 14, "عنب" to 7,
        "متنوع" to 7
    )

    val numberOptions = listOf("1", "2", "3", "4", "5+")
    val units = if (isEnglish) listOf("Kg", "Bag", "Box", "Pieces", "Cup")
    else listOf("كيلوغرام", "كيس", "علبة", "حبات", "كوب")

    var selectedExpiryDays by remember { mutableStateOf<Int?>(null) }
    var showCustomDatePicker by remember { mutableStateOf(false) }
    var customDate by remember { mutableStateOf<LocalDate?>(null) }
    val datePickerState = rememberDatePickerState()

    val expiryOptions = if (isEnglish) listOf(3 to "3 Days", 7 to "Week", 14 to "2 Weeks", 30 to "Month")
    else listOf(3 to "3 أيام", 7 to "أسبوع", 14 to "أسبوعين", 30 to "شهر")

    val isFormValid = category.isNotBlank() &&
            name.isNotBlank() &&
            quantityNumber.isNotBlank() &&
            quantityUnit.isNotBlank() &&
            (selectedExpiryDays != null || customDate != null)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = if (isEnglish) "Back" else "رجوع")
            }
            Text(
                text = if (isEnglish) "Add New Food Item" else "إضافة عنصر طعام جديد",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }

        Text(text = if (isEnglish) "Select Category" else "اختر الفئة", style = MaterialTheme.typography.titleMedium)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            categories.forEach { selection ->
                FilterChip(
                    selected = category == selection,
                    onClick = {
                        category = selection
                        name = ""
                    },
                    label = { Text(selection) }
                )
            }
        }

        if (category.isNotBlank()) {
            Text(text = if (isEnglish) "Select Item" else "اختر الصنف", style = MaterialTheme.typography.titleMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                itemsMap[category]?.forEach { selection ->
                    FilterChip(
                        selected = name == selection,
                        onClick = {
                            name = selection
                            val recommendedDays = shelfLifeMap[selection] ?: 7
                            selectedExpiryDays = when {
                                recommendedDays <= 3 -> 3
                                recommendedDays <= 7 -> 7
                                recommendedDays <= 14 -> 14
                                else -> 30
                            }
                            customDate = null
                        },
                        label = { Text(selection) }
                    )
                }
            }
        }

        if (name.isNotBlank()) {
            Text(text = if (isEnglish) "Quantity" else "الكمية", style = MaterialTheme.typography.titleMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                numberOptions.forEach { selection ->
                    FilterChip(
                        selected = quantityNumber == selection,
                        onClick = { quantityNumber = selection },
                        label = { Text(selection) }
                    )
                }
            }
        }

        if (quantityNumber.isNotBlank()) {
            Text(text = if (isEnglish) "Unit" else "الوحدة", style = MaterialTheme.typography.titleMedium)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                units.forEach { selection ->
                    FilterChip(
                        selected = quantityUnit == selection,
                        onClick = { quantityUnit = selection },
                        label = { Text(selection) }
                    )
                }
            }
        }

        if (quantityUnit.isNotBlank()) {
            Text(
                text = if (isEnglish) "Approximate Expiry (Editable)" else "الصلاحية التقريبية (يمكنك تعديلها)",
                style = MaterialTheme.typography.titleMedium
            )
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                expiryOptions.forEach { (days, label) ->
                    FilterChip(
                        selected = selectedExpiryDays == days,
                        onClick = { selectedExpiryDays = days; customDate = null },
                        label = { Text(label) }
                    )
                }
                FilterChip(
                    selected = customDate != null,
                    onClick = { showCustomDatePicker = true; selectedExpiryDays = null },
                    label = { Text(if (isEnglish) "Other" else "أخرى") }
                )
            }

            if (customDate != null) {
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                Text(
                    text = if (isEnglish) "Selected Date: ${customDate!!.format(formatter)}"
                    else "التاريخ المختار: ${customDate!!.format(formatter)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (isFormValid) {
                    val finalDate = customDate ?: LocalDate.now().plusDays(selectedExpiryDays!!.toLong())
                    val newItem = FoodItem(
                        name = name,
                        category = category,
                        expiryDate = finalDate,
                        quantity = "$quantityNumber $quantityUnit"
                    )
                    onAddItem(newItem)
                }
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isEnglish) "Save" else "حفظ", fontSize = MaterialTheme.typography.titleMedium.fontSize)
        }
    }

    if (showCustomDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showCustomDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            customDate = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        }
                        showCustomDatePicker = false
                    }
                ) { Text(if (isEnglish) "OK" else "موافق") }
            },
            dismissButton = { TextButton(onClick = { showCustomDatePicker = false }) { Text(if (isEnglish) "Cancel" else "إلغاء") } }
        ) { DatePicker(state = datePickerState) }
    }
}