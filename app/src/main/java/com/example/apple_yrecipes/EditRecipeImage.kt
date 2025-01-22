package com.example.apple_yrecipes

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
import coil3.compose.rememberAsyncImagePainter
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@Composable
fun EditRecipeImage(imagePath: String, onImageChanged: (String) -> Unit)
{
    val context = LocalContext.current
    val appImagesDir = File(context.filesDir, "appImages")
    if (!appImagesDir.exists()) {
        appImagesDir.mkdir()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        val inputStream: InputStream? = uri?.let { context.contentResolver.openInputStream(it) }
        if (inputStream === null) {
            Log.e("image", "no input stream")
        }
        val imageFileName = "image_${System.currentTimeMillis()}.jpg"
        val imageFile = File(appImagesDir, imageFileName)

        try {
            val outputStream: OutputStream = FileOutputStream(imageFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            onImageChanged(imageFile.absolutePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    val painter = if (imagePath.isEmpty()) {
        painterResource(R.drawable.photo_icon)
    } else {
        rememberAsyncImagePainter(imagePath)
    }

    Image(painter = painter,
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
            .padding(top = 30.dp)
    )
}