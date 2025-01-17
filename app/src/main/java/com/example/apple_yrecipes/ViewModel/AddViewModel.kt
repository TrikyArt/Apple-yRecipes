package com.example.apple_yrecipes.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.apple_yrecipes.db.Recipe
import kotlinx.coroutines.launch

class AddViewModel(private val repository: Repository): ViewModel() {
    fun getRecipes() = repository.getAllRecipes().asLiveData(viewModelScope.coroutineContext)

    var newRecipeName by mutableStateOf("Recipe name")
        private set

    fun setRecName(addName: String){
        newRecipeName = addName
    }
    fun setIngredient(addIng: String){
        newRecipeName = addIng
    }
    fun setDescription(addDesc: String){
        newRecipeName = addDesc
    }


    fun upsertRecipe(recipe: Recipe){
        viewModelScope.launch {
            repository.upsertRecipe(recipe)
        }
    }
}