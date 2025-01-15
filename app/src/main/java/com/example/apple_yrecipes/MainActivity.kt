package com.example.apple_yrecipes

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    Image(
                        painter = painterResource(id = R.drawable.bg),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(
                                modifier = Modifier.height(100.dp)
                            )

                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center

                            ) {
                                Box(
                                    modifier = Modifier
                                        .shadow(
                                            elevation = 8.dp,
                                            shape = RoundedCornerShape(16.dp),
                                            clip = false
                                        )
                                        .background(
                                            color = colorResource(id = R.color.beige),
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .padding(16.dp),
                                ) {
                                    Text(
                                        text = "Apple-y Recipes",
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 30.sp,
                                        color = colorResource(id = R.color.red)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(50.dp))

                            Button(onClick = {
                            val navigate = Intent(this@MainActivity, AddActivity::class.java)
                            startActivity(navigate)
                        },
                            colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.red)
                            ),
                                modifier = Modifier.shadow(
                                    elevation = 8.dp,
                                    shape = RoundedCornerShape(16.dp),
                                    clip = false
                                )

                            ) {
                            Text(text = "Add Recipe")
                        } }

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
