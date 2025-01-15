package com.example.apple_yrecipes

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import coil3.compose.rememberAsyncImagePainter
import com.example.apple_yrecipes.ViewModel.CheckRecipeViewModel
import com.example.apple_yrecipes.ViewModel.Repository
import com.example.apple_yrecipes.db.AppDatabase
import com.example.apple_yrecipes.db.Recipe
import com.example.apple_yrecipes.ui.theme.AppleyRecipesTheme

class RecipeActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
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

                    var oneRecipe by remember {
                        mutableStateOf<Recipe?>(null)
                    }
                    viewModel.getRecipe().observe(this) {
                        oneRecipe = it
                    }

                    Image(
                        painter = painterResource(id = R.drawable.bg),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )

                    Column(
                        Modifier.padding(
                            top = 80.dp,
                        ),
                    ) {

                        Button(
                            modifier = Modifier.padding(start = 30.dp),
                            colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.red)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                            val navigate = Intent(this@RecipeActivity, MainActivity::class.java)
                            startActivity(navigate)
                        }) {
                            Text(text = "Go back")
                        }
                        if (oneRecipe != null) {

                            RecipeImage()

                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = 30.dp,
                                        start = 10.dp,
                                        end = 10.dp,
                                        bottom = 30.dp
                                    )
                            ) {
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(3.dp)
                                        .background(
                                            color = colorResource(id = R.color.red),
                                            shape = RoundedCornerShape(5.dp)
                                        )
                                )
                                Box(
                                    modifier = Modifier.padding(3.dp)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .background(
                                                color = colorResource(id = R.color.beige),
                                                shape = RoundedCornerShape(5.dp)
                                            )
                                            .padding(20.dp)
                                            .fillMaxWidth()
                                    ) {
                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "${oneRecipe!!.RecipeName}",
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 26.sp,
                                            color = colorResource(id = R.color.red)
                                        )

                                        Box() {  }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(text = "${oneRecipe!!.Ingredient}")

                                        Box() {  }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(text = "${oneRecipe!!.Description}")
                                    }
                                }

                            }

                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ){
                            Button(
                                modifier = Modifier.padding(start = 100.dp),
                                colors = ButtonDefaults.buttonColors(
                                    colorResource(id = R.color.red)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                onClick = {}) {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp),
                                    text = "Edit"
                                )
                            }

                            Button(
                                modifier = Modifier.padding(start = 20.dp),
                                shape = RoundedCornerShape(8.dp),
                               colors = ButtonDefaults.buttonColors(
                                   colorResource(id = R.color.red)
                               ),
                                onClick = {
                                    val navigate = Intent(this@RecipeActivity,MainActivity::class.java)
                                    viewModel.deleteRecipe(recipeItem)
                                    startActivity(navigate)
                                }
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 7.dp, end = 7.dp),
                                    text = "Delete"
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(15.dp))



                    }
                }
            }
        }
    }
}

@Composable
fun RecipeImage(){

    val imageUri = rememberSaveable {
        mutableStateOf("")
    }
    val painter = rememberAsyncImagePainter(
        imageUri.value.ifEmpty { R.drawable.ic_launcher_foreground }
    )

    Column(
        modifier = Modifier
            .padding(
                top = 20.dp,
                start = 80.dp,

            )
        ) {
        Image(painter = painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RectangleShape)
                .size(250.dp)
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.red),
                    shape = RectangleShape
                    )
        )
    }
}
