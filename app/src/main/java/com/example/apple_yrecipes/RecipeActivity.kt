package com.example.apple_yrecipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.apple_yrecipes.ui.theme.Itim

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
                            start = 10.dp
                        ),
                    ) {

                        Button(
                            modifier = Modifier.padding(start = 20.dp),
                            colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.red)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                            val navigate = Intent(this@RecipeActivity, MainActivity::class.java)
                            startActivity(navigate)
                        }) {
                            Text(
                                fontFamily = Itim,
                                fontSize = 20.sp,
                                text = "go back"
                            )
                        }
                        if (oneRecipe != null) {

                            val painter = if (oneRecipe!!.ImagePath.isEmpty()){
                                painterResource(R.drawable.ic_launcher_foreground)
                            } else {
                                rememberAsyncImagePainter(oneRecipe!!.ImagePath)
                            }
                            Log.i("image", oneRecipe.toString())

                            Image(
                                painter = painter,
                                contentDescription = "Image Path",
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

                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = 30.dp,
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
                                            modifier = Modifier
                                                .padding(
                                                    bottom = 8.dp
                                                ),
                                            text = "${oneRecipe!!.RecipeName}",
                                            fontFamily = Itim,
                                            fontSize = 30.sp,
                                            color = colorResource(id = R.color.red)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Box(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(3.dp)
                                                .background(
                                                    color = colorResource(id = R.color.red),
                                                    shape = RoundedCornerShape(5.dp)
                                                )
                                        ) {  }

                                        Text(
                                            modifier = Modifier
                                                .padding(10.dp),
                                            text = "${oneRecipe!!.Ingredient}",
                                            fontFamily = Itim,
                                            fontSize = 20.sp,
                                            color = colorResource(id = R.color.darkRed)
                                        )

                                        Box(
                                            Modifier
                                                .fillMaxWidth()
                                                .height(3.dp)
                                                .background(
                                                    color = colorResource(id = R.color.red),
                                                    shape = RoundedCornerShape(5.dp)
                                                )
                                        ) {  }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            modifier = Modifier
                                            .padding(
                                                top = 10.dp,
                                                start = 8.dp
                                            ),
                                            text = "Recipe Description: ",
                                            fontFamily = Itim,
                                            color = colorResource(id = R.color.red),
                                            fontSize = 23.sp,
                                            fontWeight = FontWeight.Medium
                                        )

                                        Text(
                                            modifier = Modifier
                                                .padding(8.dp),
                                            text = "${oneRecipe!!.Description}",
                                            color = colorResource(id = R.color.darkRed),
                                            fontSize = 20.sp,
                                            fontFamily = Itim
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))
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
                                modifier = Modifier.padding(start = 80.dp),
                                colors = ButtonDefaults.buttonColors(
                                    colorResource(id = R.color.red)
                                ),
                                shape = RoundedCornerShape(8.dp),
                                onClick = {}) {
                                Text(
                                    modifier = Modifier
                                        .padding(start = 10.dp, end = 10.dp),
                                    fontSize = 20.sp,
                                    text = "Edit",
                                    fontFamily = Itim,
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
                                    fontSize = 20.sp,
                                    text = "Delete",
                                    fontFamily = Itim,
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
