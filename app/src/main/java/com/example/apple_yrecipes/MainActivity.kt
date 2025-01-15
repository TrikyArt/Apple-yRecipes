package com.example.apple_yrecipes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.apple_yrecipes.ViewModel.RecipeViewModel
import com.example.apple_yrecipes.ViewModel.Repository
import com.example.apple_yrecipes.db.AppDatabase
import com.example.apple_yrecipes.db.Recipe
import com.example.apple_yrecipes.ui.theme.AppleyRecipesTheme

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            name = "recipe.db"
        ).fallbackToDestructiveMigration().build()
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

                    var recipeList by remember {
                        mutableStateOf(listOf<Recipe>())
                    }
                    viewModel.getRecipes().observe(this){
                        recipeList = it
                    }
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        Button(onClick = {
                            val navigate = Intent(this@MainActivity, AddActivity::class.java)
                            startActivity(navigate)
                        }) {
                            Text(text = "Add Recipe")
                        }

                        LazyColumn {
                             items(recipeList){ recipe ->
                                 Column(Modifier.clickable {
                                     val navigate = Intent(this@MainActivity, RecipeActivity::class.java)
                                     navigate.putExtra("RecipeId", recipe.RecipeId)
                                     startActivity(navigate)
                                 }) {
                                     Divider(Modifier.fillParentMaxWidth().padding(6.dp))
                                     Spacer(modifier = Modifier.height(6.dp))
                                     Text(text = "${recipe.RecipeName}")
                                 }

                             }
                        }
                    }
                }
            }
        }
    }
}
