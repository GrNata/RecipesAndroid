package com.grig.recipesandroid.ui.utilRecipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

//   Пометка обязательных полей на UI

@Composable
fun LabledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isRequired: Boolean = false,
    isError: Boolean = false
) {

    Column() {
        Text(
            text = if (isError) "$label *" else label,
            color = if (isError) Color.Red else Color.Black,
            style = MaterialTheme.typography.bodyMedium
        )
        TextField(
            value = value,
            onValueChange = onValueChange,
            isError = isError,
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF7EDE9),
                unfocusedContainerColor = Color(0xFFEEE2DC),
                focusedTextColor = Color(0xFF062444),
                unfocusedTextColor = Color(0xFF1E364F),
                cursorColor = Color(0xFF123C69),
                errorTextColor = Color.Red
            ),
            textStyle = MaterialTheme.typography.bodyMedium
        )
    }

}