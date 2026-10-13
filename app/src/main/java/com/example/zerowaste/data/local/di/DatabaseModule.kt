package com.example.zerowaste.data.di

import android.content.Context
import androidx.room.Room
import com.example.zerowaste.data.local.AppDatabase
import com.example.zerowaste.data.local.FoodItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "zero_waste_database"
        ).fallbackToDestructiveMigration().build() // تم إضافة هذا السطر
    }

    @Provides
    fun provideFoodItemDao(database: AppDatabase): FoodItemDao {
        return database.foodItemDao()
    }
}