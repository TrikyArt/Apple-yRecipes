package com.example.apple_yrecipes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.apple_yrecipes.ViewModel.RecipeViewModel
import com.example.apple_yrecipes.ViewModel.Repository
import com.example.apple_yrecipes.db.Database
import com.example.apple_yrecipes.db.Recipe
import com.example.apple_yrecipes.ui.theme.AppleyRecipesTheme

class AddActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            name = "recipe.db"
        ).build()
    }
    private val viewModel by viewModels<RecipeViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RecipeViewModel(Repository(db)) as T
                }
            }
        }
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppleyRecipesTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background){

                    var RecipeName by remember {
                        mutableStateOf("")
                    }
                    var Ingredient by remember {
                        mutableStateOf("")
                    }
                    var Description by remember {
                        mutableStateOf("")
                    }
                    val recipe = Recipe(
                        RecipeName,
                        Ingredient,
                        Description
                    )
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        TextField(value = RecipeName,
                            onValueChange = {RecipeName = it},
                            placeholder = { Text(text = "Recipe name") })

                        TextField(value = Ingredient,
                            onValueChange = {Ingredient = it},
                            placeholder = { Text(text = "Ingredient") })

                        TextField(value = Description,
                            onValueChange = {Description = it},
                            placeholder = { Text(text = "Description") })

                        Button(onClick = {
                            viewModel.upsertRecipe(recipe)
                            val navigate = Intent(this@AddActivity, MainActivity::class.java)
                            startActivity(navigate)
                        }) {
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}
