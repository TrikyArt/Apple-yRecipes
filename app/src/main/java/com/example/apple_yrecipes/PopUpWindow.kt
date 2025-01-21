package com.example.apple_yrecipes

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AlertDelete(onDismiss:()->Unit) {

    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        modifier = Modifier.height(250.dp),

        title = {
                Row(verticalAlignment = Alignment.CenterVertically){
                    Text(text = "Are you sure you want to delete this recipe?")
                }
        },
        text = {
            Row(modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically)
            {
                Button(onClick = {},
                    modifier = Modifier.width(100.dp).padding(10.dp)) {
                    Text(text = "No")
                }

                Button(onClick = {},
                    modifier = Modifier.width(100.dp).padding(10.dp)) {
                    Text(text = "Yes")
                }
            }
        }
    )

}