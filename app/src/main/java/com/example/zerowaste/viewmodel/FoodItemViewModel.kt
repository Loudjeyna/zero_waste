package com.example.zerowaste.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zerowaste.data.local.FoodItem
import com.example.zerowaste.data.local.FoodItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodItemViewModel @Inject constructor(
    private val foodItemDao: FoodItemDao
) : ViewModel() {

    val allFoodItems: StateFlow<List<FoodItem>> = foodItemDao.getAllFoodItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addFoodItem(foodItem: FoodItem) {
        viewModelScope.launch {
            foodItemDao.insertFoodItem(foodItem)
        }
    }

    fun deleteFoodItem(foodItem: FoodItem) {
        viewModelScope.launch {
            foodItemDao.deleteFoodItem(foodItem)
        }
    }
}