package com.example.apple_yrecipes.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Upsert
    suspend fun upsertRecipe(recipe: Recipe)
    @Delete
    suspend fun deleteRecipe(recipe: Recipe)
    @Update
    suspend fun updateRecipe(recipe: Recipe)

    @Query("SELECT * FROM Recipe WHERE RecipeId = :RecipeId")
    fun getRecipe(RecipeId:Int): Flow<Recipe>

    @Query("SELECT * FROM Recipe Order by RecipeName ASC")
    fun getAllRecipes(): Flow<List<Recipe>>
}