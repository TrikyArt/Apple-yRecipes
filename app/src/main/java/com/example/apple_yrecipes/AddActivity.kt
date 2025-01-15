package com.example.apple_yrecipes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import coil3.compose.rememberAsyncImagePainter
import com.example.apple_yrecipes.ViewModel.RecipeViewModel
import com.example.apple_yrecipes.ViewModel.Repository
import com.example.apple_yrecipes.db.AppDatabase
import com.example.apple_yrecipes.db.Recipe
import com.example.apple_yrecipes.ui.theme.AppleyRecipesTheme

class AddActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
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
                        Description,
                        //TODO: This needs to come frome an iumage picker
                        ImagePath = ""
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
                        AddRecipeImage()
                    }
                }
            }
        }
    }
}

@Composable
fun AddRecipeImage(){

    val imageUri = rememberSaveable {
        mutableStateOf("")
    }
    val painter = rememberAsyncImagePainter(
        imageUri.value.ifEmpty { R.drawable.ic_launcher_foreground }
    )
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RectangleShape)
                .size(250.dp)
                .border(
                    width = 1.dp,
                    color = Color.Blue,
                    shape = RectangleShape
                )
                .clickable { launcher.launch("image/*") }
        )
    }
}