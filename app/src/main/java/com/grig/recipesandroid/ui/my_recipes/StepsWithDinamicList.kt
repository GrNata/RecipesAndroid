package com.grig.recipesandroid.ui.my_recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsWithDinamicList(viewModel: AddEditRecipeViewModel) {

    Text(
        text = "Шаги приготовления",
        style = MaterialTheme.typography.titleMedium,
//        modifier = Modifier.padding(top = 8.dp),
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = Color(0xFF123C69)
    )
    Column(modifier = Modifier.padding(top = 8.dp)) {
        viewModel.steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                    TextField(
                        value = step,
                        onValueChange = { viewModel.updateStep(index, it) },
//                        onValueChange = { newText ->
//                            viewModel.updateStep(index, newText)
//                        },
//                        readOnly = false,
//                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.weight(3f),
                        label = { Text("Шаг ${index + 1}")},
                        textStyle = MaterialTheme.typography.bodyMedium,
                        colors = TextFieldDefaults.colors(
                            // Фон поля
                            focusedContainerColor = Color( 0xFFF7EDE9),
                            unfocusedContainerColor = Color(0xFFEEE2DC),
                            disabledContainerColor = Color(0xBFFF6A00).copy(alpha = 0.5f), // полупрозрачный при отключении

                            // Цвет текста
                            focusedTextColor = Color(0xFF062444),
                            unfocusedTextColor = Color(0xFF1E364F),
                            disabledTextColor = Color.Gray,

                            // Дополнительные цвета (настройка по желанию)
                            cursorColor = Color(0xFF123C69),
                            errorTextColor = Color.Red
                        )
                    )

                IconButton(
                    modifier = Modifier.weight(0.13f),
                    onClick = { viewModel.removeStep(index) }
                ) {
                    Icon(Icons.Default.Delete, "Удалить шаг")
                }


            }
        }
        // Кнопка добавить новый шаг
        Button(
            modifier = Modifier.fillMaxWidth()
                .background(Color(0xFFEFEFEF)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF8E4253),
                contentColor = Color(0xFFEDE3E5)
            ),
            onClick = { viewModel.addStep(
            viewModel.steps.size, ""
        ) },
//            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Добавить шаг")
        }
    }
}