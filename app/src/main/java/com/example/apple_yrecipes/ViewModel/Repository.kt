package com.example.apple_yrecipes.ViewModel

import com.example.apple_yrecipes.db.Database
import com.example.apple_yrecipes.db.Recipe


class Repository(private val db : Database) {

    suspend fun upsertRecipe(recipe: Recipe){
        db.recipeDao().upsertRecipe(recipe)
    }

    suspend fun deleteRecipe(recipe: Recipe){
        db.recipeDao().deleteRecipe(recipe)
    }

    fun getAllRecipes() = db.recipeDao().getAllRecipes()

    fun getRecipe(RecipeId:Int) = db.recipeDao().getRecipe(RecipeId = RecipeId)
}