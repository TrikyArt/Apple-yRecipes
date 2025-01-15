package com.example.apple_yrecipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.apple_yrecipes.ViewModel.CheckRecipeViewModel
import com.example.apple_yrecipes.ViewModel.Repository
import com.example.apple_yrecipes.db.Database
import com.example.apple_yrecipes.db.Recipe
import com.example.apple_yrecipes.ui.theme.AppleyRecipesTheme

class RecipeActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            Database::class.java,
            name = "recipe.db"
        ).build()
    }
    private val viewModel by viewModels<CheckRecipeViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return CheckRecipeViewModel(
                        Repository(db),
                        this@RecipeActivity.intent.getIntExtra("RecipeId", 0)
                    ) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppleyRecipesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    lateinit var recipeItem: Recipe
                    viewModel.getRecipe().observe(this, Observer{recipe->
                        recipeItem = recipe
                        Log.e("test", recipeItem.toString())

                    })

                    //TODO:: Add recipe information to thse variables
                    var RecipeName by remember {
                        mutableStateOf("")
                    }
                    var Ingrediente by remember {
                        mutableStateOf("")
                    }
                    var Description by remember {
                        mutableStateOf("")
                    }
                    val recipe = Recipe(
                        RecipeName,
                        Ingrediente,
                        Description
                    )
                    var oneRecipe by remember {
                        mutableStateOf<Recipe?>(null)
                    }
                    viewModel.getRecipe().observe(this) {
                        oneRecipe = it
                    }

                    Column(
                        Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Button(onClick = {
                            val navigate = Intent(this@RecipeActivity, MainActivity::class.java)
                            startActivity(navigate)
                        }) {
                            Text(text = "Go back")
                        }
                        if (oneRecipe != null) {
                            Column {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "${oneRecipe!!.RecipeName}")
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "${oneRecipe!!.Ingredient}")
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "${oneRecipe!!.Description}")
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)){
                            Button(onClick = {}) {
                                Text(text = "Edit")
                            }

                            Button(
                                onClick = {
                                    val navigate = Intent(this@RecipeActivity,MainActivity::class.java)
                                    viewModel.deleteRecipe(recipeItem)
                                    startActivity(navigate)
                                }
                            ) {
                                Text(text = "Delete")
                            }
                        }
                    }
                }
            }
        }
    }
}
