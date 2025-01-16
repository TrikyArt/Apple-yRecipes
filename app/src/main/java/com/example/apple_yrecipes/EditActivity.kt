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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import coil3.compose.rememberAsyncImagePainter
import com.example.apple_yrecipes.ViewModel.EditViewModel
import com.example.apple_yrecipes.ViewModel.Repository
import com.example.apple_yrecipes.db.AppDatabase
import com.example.apple_yrecipes.ui.theme.AppleyRecipesTheme
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class EditActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            name = "recipe.db"
        ).build()
    }
    private val viewModel by viewModels<EditViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory{
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return EditViewModel(Repository(db),this@EditActivity.intent.getIntExtra("RecipeId",0)) as T
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

                    var ImagePath = rememberSaveable {
                        mutableStateOf("")
                    }

                    var Ingredient by remember {
                        mutableStateOf("")
                    }
                    var Description by remember {
                        mutableStateOf("")
                    }

                    Image(
                        painter = painterResource(id = R.drawable.bg),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                        Button(onClick = {
                            val navigate = Intent(this@EditActivity, MainActivity::class.java)
                            startActivity(navigate)
                        }) {
                            Text(text = "Go back")
                        }

                        TextField(value = viewModel.currentRecipe?.RecipeName ?: "placerholder",
                            onValueChange = { viewModel.renameRecipe(it)},
                            placeholder = { Text(text = "Recipe name") })

                        TextField(value = Ingredient,
                            onValueChange = {Ingredient = it},
                            placeholder = { Text(text = "Ingredient") })

                        TextField(value = Description,
                            onValueChange = {Description = it},
                            placeholder = { Text(text = "Description") })

                        Button(onClick = {
                            viewModel.saveChanges()
                            val navigate = Intent(this@EditActivity, MainActivity::class.java)
                            startActivity(navigate)
                        }) {
                            Text(text = "Save")
                        }
                        EditRecipeImage(ImagePath)

                    }
                }
            }
        }
    }
}

@Composable
fun EditRecipeImage(imagePath:MutableState<String>){
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
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(painter =  painter,
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