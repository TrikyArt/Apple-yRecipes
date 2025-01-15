package com.example.apple_yrecipes.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.apple_yrecipes.db.Recipe
import kotlinx.coroutines.launch

class RecipeViewModel(private val repository: Repository): ViewModel() {
    fun getRecipes() = repository.getAllRecipes().asLiveData(viewModelScope.coroutineContext)

    fun upsertRecipe(recipe: Recipe){
        viewModelScope.launch {
            repository.upsertRecipe(recipe)
        }
    }
}