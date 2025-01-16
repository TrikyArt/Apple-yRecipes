package com.example.apple_yrecipes.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.apple_yrecipes.db.Recipe
import kotlinx.coroutines.flow.first
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EditViewModel(private val repository: Repository,val RecipeId:Int):ViewModel() {

    var currentRecipe by mutableStateOf<Recipe?>(null)
        private set

    init {
        viewModelScope.launch {
            currentRecipe = repository.getRecipe(RecipeId = RecipeId).first()
        }
    }

    fun renameRecipe(newName: String) {
        currentRecipe = currentRecipe?.copy(RecipeName = newName)
    }
    fun changeDescription(newDescription: String) {
        currentRecipe = currentRecipe?.copy(Description = newDescription)
    }
    fun changeIngredient(newIngredients: String) {
        currentRecipe = currentRecipe?.copy(Ingredient = newIngredients)
    }
    fun changeImage(newImage: String) {
        currentRecipe = currentRecipe?.copy(ImagePath = newImage)
    }

    fun saveChanges(){
        viewModelScope.launch{
            if(currentRecipe != null)
            repository.updateRecipe(currentRecipe!!)
        }
    }
}