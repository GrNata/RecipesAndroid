package com.grig.recipesandroid.ui.my_recipes

import android.R
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IngredientsWithDinamicList(viewModel: AddEditRecipeViewModel) {

    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Ингредиент",
                modifier = Modifier
                    .weight(2f)
                    .padding(start = 6.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Кол-во",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Ед.",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "  ",
                modifier = Modifier.weight(0.3f),
                textAlign = TextAlign.Center
            )

        }

        viewModel.ingredients.forEachIndexed { index, ingredient ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
                    .height(50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Dropdown для выбора ингредиента
                var ingExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = ingExpanded,
                    onExpandedChange = { ingExpanded = it },
                    modifier = Modifier.weight(2.2f)
                ) {
                    TextField(
                        value = viewModel.getIngredientName(index),
                        onValueChange = {},
                        readOnly = true,
//                        label = { Text("Ингредиент") },
//                        label = { Text("Ингр-нт") },
                        trailingIcon = { TrailingIcon(ingExpanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor(),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    ExposedDropdownMenu(
                        expanded = ingExpanded,
                        onDismissRequest = { ingExpanded = false }
                    ) {
                        viewModel.ingredientsAll.forEach { ing ->
                            DropdownMenuItem(
                                text = { Text(ing.name) },
                                onClick = {
                                    viewModel.onIngredientSelected(index, ing)
                                    ingExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // 2. Поле для количества
                TextField(
                    value = ingredient.amount ?: "",
                    onValueChange = { viewModel.onIngredientAmountChange(index, it) },
//                    label = { Text("Кол-во") },
//                    modifier = Modifier.width(80.dp)
                    modifier = Modifier.weight(1.1f),
                    textStyle = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(8.dp))

                // 3. Dropdown для единицы измерения
                var unitExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = unitExpanded,
                    onExpandedChange = { unitExpanded = it },
                    modifier = Modifier.weight(1.2f)
                ) {
                    TextField(
                        value = viewModel.getUnitName(index),
                        onValueChange = {},
                        readOnly = true,
//                        label = { Text("Ед.") },
                        trailingIcon = { TrailingIcon(unitExpanded) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                        modifier = Modifier.menuAnchor(),
                        textStyle = MaterialTheme.typography.bodyMedium
                    )

                    ExposedDropdownMenu(
                        expanded = unitExpanded,
                        onDismissRequest = { unitExpanded = false }
                    ) {
                        viewModel.unitsAll.forEach { unit ->
                            DropdownMenuItem(
                                text = { Text(unit.label) },
                                onClick = {
                                    viewModel.onUnitSelected(index, unit)
                                    unitExpanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))

                // 4. Кнопка удалить
                IconButton(
                    modifier = Modifier.weight(0.2f),
                    onClick = { viewModel.removeIngredient(index) }) {
                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                }
            }
        }

        // Кнопка добавить новый ингредиент
        Button(onClick = { viewModel.addIngredient() }, modifier = Modifier.padding(top = 8.dp)) {
            Text("Добавить ингредиент")
        }
    }

}

