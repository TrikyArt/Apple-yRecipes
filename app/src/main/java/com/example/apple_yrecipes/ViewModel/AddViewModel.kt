package com.example.apple_yrecipes.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.apple_yrecipes.db.Recipe
import kotlinx.coroutines.launch

class AddViewModel(private val repository: Repository) : ViewModel() {
    fun getRecipes() = repository.getAllRecipes().asLiveData(viewModelScope.coroutineContext)

    var newRecipeName by mutableStateOf("Recipe name")
        private set

    var newIngredient by mutableStateOf("Ingredients")
        private set

    var newDescription by mutableStateOf("Description")
        private set

    var newImagePath by mutableStateOf("")
        private set

    fun setRecName(addName: String) {
        newRecipeName = addName
    }

    fun setIngredient(addIng: String) {
        newIngredient = addIng
    }

    fun setDescription(addDesc: String) {
        newDescription = addDesc
    }

    fun setImage(addImg: String) {
        newImagePath = addImg
    }


    fun upsertRecipe() {
        var recipe = Recipe(
            RecipeName = newRecipeName,
            Ingredient = newIngredient,
            Description = newDescription,
            ImagePath = newImagePath
        )
        viewModelScope.launch {
            repository.upsertRecipe(recipe)
        }
    }
}