package com.example.apple_yrecipes.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.apple_yrecipes.db.Recipe
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CheckRecipeViewModel(private val repository: Repository, val RecipeId:Int):ViewModel() {
    fun getRecipe() = repository.getRecipe(RecipeId).asLiveData(viewModelScope.coroutineContext)

    var itemUiState by mutableStateOf<Recipe?>(null)
        private set

    init {
        viewModelScope.launch {
            itemUiState = repository.getRecipe(RecipeId = RecipeId).first()
        }
    }
    fun deleteRecipe(item: Recipe){
        viewModelScope.launch {
            repository.deleteRecipe(item)
        }
    }

}