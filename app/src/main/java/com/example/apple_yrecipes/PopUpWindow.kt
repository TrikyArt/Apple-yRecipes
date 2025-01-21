package com.example.apple_yrecipes

import android.content.Context
import android.content.Intent
import android.widget.PopupWindow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.apple_yrecipes.ViewModel.CheckRecipeViewModel
import com.example.apple_yrecipes.db.Recipe

@Composable
fun AlertDelete(viewModel: CheckRecipeViewModel, recipe: Recipe, showDialog: MutableState<Boolean>,context: Context) {

    AlertDialog(
        onDismissRequest = {},
        modifier = Modifier.height(250.dp),
        confirmButton ={
            Button(
                onClick = {
                    viewModel.deleteRecipe(recipe)
                    val navigation =
                        Intent(context,MainActivity::class.java)
                    context.startActivity(navigation)
                }
            ) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = {showDialog.value = false}
            ) {
                Text("No")
            }
        },


        title = {
                Column(verticalArrangement = Arrangement.Center){
                    Text(text = "Are you sure you want to delete this recipe?")
                }
        }
    )

}