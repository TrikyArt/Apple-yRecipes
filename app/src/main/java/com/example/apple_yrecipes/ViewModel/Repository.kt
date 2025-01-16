package com.example.apple_yrecipes.ViewModel

import com.example.apple_yrecipes.db.AppDatabase
import com.example.apple_yrecipes.db.Recipe


class Repository(private val db : AppDatabase) {

    suspend fun upsertRecipe(recipe: Recipe){
        db.recipeDao().upsertRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: Recipe){
        db.recipeDao().deleteRecipe(recipe)
    }

    suspend fun updateRecipe(recipe: Recipe){
        db.recipeDao().updateRecipe(recipe)
    }

    fun getAllRecipes() = db.recipeDao().getAllRecipes()

    fun getRecipe(RecipeId:Int) = db.recipeDao().getRecipe(RecipeId = RecipeId)
}