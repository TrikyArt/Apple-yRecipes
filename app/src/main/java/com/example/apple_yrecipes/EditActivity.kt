package com.example.apple_yrecipes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    @OptIn(ExperimentalMaterial3Api::class)
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
                        modifier = Modifier.fillMaxSize(),
                        alpha = 0.8F
                    )

                    val scrollState = rememberScrollState()

                    Column(
                        Modifier.padding(
                            top = 80.dp,
                            start = 30.dp
                        )
                            .verticalScroll(scrollState),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Button(
                            modifier = Modifier.padding(start = 10.dp),
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

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 70.dp)
                        ) {


                            Box(
                                modifier = Modifier
                                    .padding(
                                        top = 23.dp,
                                        start = 55.dp
                                    )

                            ) {
                                Text(
                                    modifier = Modifier
                                        .background(
                                            color = colorResource(id = R.color.beige),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(
                                            top = 5.dp,
                                            start = 20.dp,
                                            end = 20.dp
                                        ),
                                    text = "Edit",
                                    fontFamily = Itim,
                                    fontSize = 25.sp,
                                    color = colorResource(id = R.color.darkRed)
                                )
                            }

                            Box {
                                Image(
                                    painter = painterResource(id = R.drawable.rolling_pin),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .width(150.dp)
                                        .padding(
                                            start = 50.dp
                                        )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        if (viewModel.currentRecipe != null) {
                            EditRecipeImage(
                                viewModel.currentRecipe!!.ImagePath,
                                { viewModel.changeImage(it) }
                            )
                        }

                        Spacer(modifier = Modifier.height(5.dp))

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 28.dp)
                        ) {
                            Box(
                                Modifier
                                    .width(286.dp)
                                    .height(3.dp)
                                    .background(
                                        color = colorResource(id = R.color.red),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                            )

                            TextField(
                                modifier = Modifier
                                    .padding(
                                        top = 3.dp,
                                        start = 3.dp
                                    )
                                    .background(color = colorResource(id = R.color.beige)),
                                value = viewModel.currentRecipe?.RecipeName ?: "",
                                onValueChange = { viewModel.renameRecipe(it) },
                                placeholder = {
                                    Text(
                                        fontFamily = Itim,
                                        fontSize = 20.sp,
                                        color = colorResource(id = R.color.darkRed),
                                        text = "Recipe name"
                                    )
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = colorResource(id = R.color.beige),
                                    focusedTextColor = colorResource(id = R.color.darkRed),
                                    unfocusedTextColor = colorResource(id = R.color.darkRed),
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent
                                )
                            )
                        }

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 28.dp)
                        ) {
                            Box(
                                Modifier
                                    .width(286.dp)
                                    .height(3.dp)
                                    .background(
                                        color = colorResource(id = R.color.red),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                            )

                            TextField(
                                value = viewModel.currentRecipe?.Ingredient ?: "",
                                onValueChange = { viewModel.changeIngredient(it) },
                                modifier = Modifier
                                    .padding(
                                        top = 3.dp,
                                        start = 3.dp
                                    )
                                    .background(color = colorResource(id = R.color.beige)),
                                placeholder = {
                                    Text(
                                        fontFamily = Itim,
                                        fontSize = 20.sp,
                                        color = colorResource(id = R.color.darkRed),
                                        text = "Ingredient"
                                    )
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = colorResource(id = R.color.beige),
                                    focusedTextColor = colorResource(id = R.color.darkRed),
                                    unfocusedTextColor = colorResource(id = R.color.darkRed),
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent
                                )
                            )
                        }

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(start = 28.dp)
                        ) {
                            Box(
                                Modifier
                                    .width(286.dp)
                                    .height(3.dp)
                                    .background(
                                        color = colorResource(id = R.color.red),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                            )

                            TextField(
                                modifier = Modifier
                                    .padding(
                                        top = 3.dp,
                                        start = 3.dp
                                    )
                                    .background(color = colorResource(id = R.color.beige)),
                                value = viewModel.currentRecipe?.Description ?: "",
                                onValueChange = { viewModel.changeDescription(it) },
                                placeholder = {
                                    Text(
                                        fontFamily = Itim,
                                        fontSize = 20.sp,
                                        color = colorResource(id = R.color.darkRed),
                                        text = "Description"
                                    )
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = colorResource(id = R.color.beige),
                                    focusedTextColor = colorResource(id = R.color.darkRed),
                                    unfocusedTextColor = colorResource(id = R.color.darkRed),
                                    unfocusedIndicatorColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent
                                )
                            )
                        }


                        Button(
                            modifier = Modifier.padding(start = 10.dp),
                            colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.red)
                            ),
                            shape = RoundedCornerShape(8.dp),
                            onClick = {
                            viewModel.saveChanges()
                            val navigate = Intent(this@EditActivity, MainActivity::class.java)
                            startActivity(navigate)
                        }) {
                            Text(
                                fontFamily = Itim,
                                fontSize = 20.sp,
                                text = "Save"
                            )
                        }

                    }
                }
            }
        }
    }
}

