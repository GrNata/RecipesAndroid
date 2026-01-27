package com.grig.recipesandroid.ui.recipe_detail

import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import com.grig.recipesandroid.data.model.ui.IngredientUi

@Composable
fun AmountInputDialog(
    ingredient: IngredientUi,
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var value by remember {
        mutableStateOf(ingredient.amount?.toString() ?: "")
    }

    AlertDialog(
        onDismissRequest = onDismiss,

        title = {
            Text("Кол-во для ${ingredient.name}")
        },

        text = {
            OutlinedTextField(
                value = value,
                onValueChange = { value = it },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true
            )
        },

        confirmButton = {
            TextButton(
                onClick = {
                    Log.d("INGREDIENT-UI", "AmountInputDialog: value.toDoubleOrNull()?: ${value.toDoubleOrNull()}")
                    value.toDoubleOrNull()?.let {
                        onConfirm(it)
                    }
                }
            ) {
                Text("OK")
            }
        },

        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}