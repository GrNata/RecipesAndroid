package com.grig.recipesandroid.ui.my_recipes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepsWithDinamicList(viewModel: AddEditRecipeViewModel) {

    Text(
        text = "Шаги приготовления",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )
    Column(modifier = Modifier.padding(top = 8.dp)) {
        viewModel.steps.forEachIndexed { index, step ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
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
                        modifier = Modifier.weight(1f),
                        label = { Text("Шаг ${index + 1}")}
                    )

                IconButton(onClick = { viewModel.removeStep(index) }) {
                    Icon(Icons.Default.Delete, "Удалить шаг")
                }


            }
        }
        // Кнопка добавить новый шаг
        Button(onClick = { viewModel.addStep(
            viewModel.steps.size, ""
        ) },
            modifier = Modifier.padding(top = 8.dp)) {
            Text("Добавить шаг")
        }
    }
}