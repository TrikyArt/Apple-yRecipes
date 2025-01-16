package com.example.apple_yrecipes

import android.annotation.SuppressLint
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import coil3.compose.rememberAsyncImagePainter
import com.example.apple_yrecipes.ViewModel.RecipeViewModel
import com.example.apple_yrecipes.ViewModel.Repository
import com.example.apple_yrecipes.db.AppDatabase
import com.example.apple_yrecipes.db.Recipe
import com.example.apple_yrecipes.ui.theme.AppleyRecipesTheme
import com.example.apple_yrecipes.ui.theme.Itim
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import kotlin.reflect.typeOf

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
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppleyRecipesTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background){

                    var ImagePath = rememberSaveable {
                        mutableStateOf("")
                    }

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
                        ImagePath = ImagePath.value
                    )
                    Image(
                        painter = painterResource(id = R.drawable.bg),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(
                        Modifier
                            .padding(
                                top = 80.dp,
                                start = 20.dp
                            ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {



                        Button(onClick = {
                            val navigate = Intent(this@AddActivity, MainActivity::class.java)
                            startActivity(navigate)
                        },
                            colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.red)),
                            shape = RoundedCornerShape(8.dp),
                            ) {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        start = 5.dp,
                                        end = 5.dp),
                                fontSize = 20.sp,
                                fontFamily = Itim,
                                text = "Go back"
                            )
                        }

                        AddRecipeImage(ImagePath)

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        Box(
                            Modifier
                                .fillMaxWidth()
                        ) {
                            Box(
                                Modifier
                                    .width(280.dp)
                                    .height(3.dp)
                                    .background(
                                        color = colorResource(id = R.color.red),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                            )

                            TextField(
                                modifier = Modifier
                                    .padding(
                                        top = 3.dp
                                    )
                                    .background(color = colorResource(id = R.color.beige)),
                                value = RecipeName,
                                onValueChange = {RecipeName = it},
                                placeholder = {
                                    Text(
                                        fontFamily = Itim,
                                        fontSize = 20.sp,
                                        color = colorResource(id = R.color.darkRed),
                                        text = "Recipe name"
                                    )
                                              },
                                colors = TextFieldDefaults.textFieldColors(
                                    containerColor = colorResource(id = R.color.beige)
                                )
                            )
                        }


                        Box(
                            Modifier
                                .fillMaxWidth()
                        ) {
                            Box(
                                Modifier
                                    .width(280.dp)
                                    .height(3.dp)
                                    .background(
                                        color = colorResource(id = R.color.red),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                            )
                            TextField(
                                modifier = Modifier
                                    .padding(
                                        top = 3.dp
                                    )
                                    .background(color = colorResource(id = R.color.beige)),
                                value = Ingredient,
                                onValueChange = {Ingredient = it},
                                placeholder = {
                                    Text(
                                        fontFamily = Itim,
                                        fontSize = 20.sp,
                                        color = colorResource(id = R.color.darkRed),
                                        text = "Ingredient"
                                    )
                                              },
                                colors = TextFieldDefaults.textFieldColors(
                                containerColor = colorResource(id = R.color.beige)
                                )
                            )
                        }


                        Box(
                            Modifier
                                .fillMaxWidth()
                        ) {
                            Box(
                                Modifier
                                    .width(280.dp)
                                    .height(3.dp)
                                    .background(
                                        color = colorResource(id = R.color.red),
                                        shape = RoundedCornerShape(5.dp)
                                    )
                            )

                            TextField(
                                modifier = Modifier
                                    .padding(
                                        top = 3.dp
                                    )
                                    .background(color = colorResource(id = R.color.beige)),
                                value = Description,
                                onValueChange = {Description = it},
                                placeholder = {
                                    Text(
                                        fontFamily = Itim,
                                        fontSize = 20.sp,
                                        color = colorResource(id = R.color.darkRed),
                                        text = "Description"
                                    )
                                              },
                                colors = TextFieldDefaults.textFieldColors(
                                containerColor = colorResource(id = R.color.beige)
                                )
                            )
                        }



                        Button(
                            onClick = {
                            Log.i("image", ImagePath.value)
                            viewModel.upsertRecipe(recipe)
                            val navigate = Intent(this@AddActivity, MainActivity::class.java)
                            startActivity(navigate)
                        },
                            colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.red)),
                            shape = RoundedCornerShape(8.dp),
                            ) {
                            Text(
                                modifier = Modifier
                                    .padding(
                                        start = 5.dp,
                                        end = 5.dp),
                                fontSize = 20.sp,
                                fontFamily = Itim,
                                text = "Save"
                            )
                        }


                    }
                }
            }
        }
    }
}

@Composable
fun AddRecipeImage(imagePath:MutableState<String>){
    val context = LocalContext.current
    val appImagesDir = File(context.filesDir, "appImages")
    if (!appImagesDir.exists()) {
        appImagesDir.mkdir()
    }

    //var imagePath by remember { mutableStateOf<String?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        //Log.i("image", uri.toString())
        val inputStream: InputStream? = uri?.let { context.contentResolver.openInputStream(it) }
        if (inputStream===null){
            Log.e("image", "no input stream")
        }
        val imageFileName = "image_${System.currentTimeMillis()}.jpg"
        val imageFile = File(appImagesDir, imageFileName)

        try {
            val outputStream: OutputStream = FileOutputStream(imageFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            imagePath.value = imageFile.absolutePath
            Log.i("image", imageFile.absolutePath)
        } catch (e: Exception){
            e.printStackTrace()
            Log.e("image", e.toString())
        }
    }
    val painter = if (imagePath.value.isEmpty()){
        painterResource(R.drawable.ic_launcher_foreground)
    } else {
        rememberAsyncImagePainter(imagePath.value)
    }

    Column(
        modifier = Modifier
            .padding(
                top = 10.dp,
                start = 50.dp,

                )
    ) {
        Image(painter =  painter,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .clip(RectangleShape)
                .size(250.dp)
                .border(
                    width = 1.dp,
                    color = colorResource(id = R.color.darkRed),
                    shape = RectangleShape
                )
                .clickable { launcher.launch("image/*") }
        )
    }
}