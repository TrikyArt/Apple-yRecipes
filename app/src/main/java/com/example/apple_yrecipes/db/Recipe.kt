package com.example.apple_yrecipes.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Recipe(
    val RecipeName: String,
    val Ingredient: String,
    val Description: String,
    @PrimaryKey(autoGenerate = true)
    val RecipeId : Int = 0
)
