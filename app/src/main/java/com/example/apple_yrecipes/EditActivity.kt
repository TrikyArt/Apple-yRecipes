package com.example.apple_yrecipes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.apple_yrecipes.ViewModel.EditViewModel
import com.example.apple_yrecipes.ViewModel.Repository
import com.example.apple_yrecipes.db.AppDatabase
import com.example.apple_yrecipes.ui.theme.AppleyRecipesTheme
import com.example.apple_yrecipes.ui.theme.Itim

class EditActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            name = "recipe.db"
        ).build()
    }
    private val viewModel by viewModels<EditViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EditViewModel(
                        Repository(db),
                        this@EditActivity.intent.getIntExtra("RecipeId", 0)
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

                    Image(
                        painter = painterResource(id = R.drawable.bg),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Button(
                            modifier = Modifier.padding(start = 20.dp),
                            colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.red)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                            val navigate = Intent(this@EditActivity, MainActivity::class.java)
                            startActivity(navigate)
                        }) {
                            Text(
                                fontFamily = Itim,
                                fontSize = 20.sp,
                                text = "Go back"
                            )
                        }

                        if (viewModel.currentRecipe != null) {
                            EditRecipeImage(
                                viewModel.currentRecipe!!.ImagePath,
                                { viewModel.changeImage(it) }
                            )
                        }

                        TextField(value = viewModel.currentRecipe?.RecipeName ?: "",
                            onValueChange = { viewModel.renameRecipe(it) },
                            placeholder = { Text(text = "Recipe name") })

                        TextField(value = viewModel.currentRecipe?.Ingredient ?: "",
                            onValueChange = { viewModel.changeIngredient(it) },
                            placeholder = { Text(text = "Ingredient") })

                        TextField(value = viewModel.currentRecipe?.Description ?: "",
                            onValueChange = { viewModel.changeDescription(it) },
                            placeholder = { Text(text = "Description") })


                        Button(onClick = {
                            viewModel.saveChanges()
                            val navigate = Intent(this@EditActivity, MainActivity::class.java)
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

