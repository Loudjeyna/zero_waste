package com.example.zerowaste.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "food_items")
data class FoodItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val category: String,
    val expiryDate: LocalDate,
    val quantity: String = "", // تم التغيير هنا إلى String لقبول (1 كجم، كيس، إلخ)
    val addedDate: LocalDate = LocalDate.now()
)