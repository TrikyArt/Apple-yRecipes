package com.example.apple_yrecipes.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Recipe::class],
    version = 3
)
abstract class Database: RoomDatabase() {
    abstract fun recipeDao() : Dao
}