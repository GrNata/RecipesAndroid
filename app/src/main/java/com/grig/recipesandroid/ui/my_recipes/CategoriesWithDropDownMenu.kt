package com.grig.recipesandroid.ui.my_recipes

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesWIthDropDownMenu(viewModel: AddEditRecipeViewModel) {

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
//                onExpandedChange = { expanded = !expanded },
        onExpandedChange = { expanded = it },    // <- прямо передаем новое значение
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {

        TextField(
            value = viewModel.selectedCategory?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Категория") },
            trailingIcon = {
                TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor() // обязательно для работы dropdown
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
//                    Log.d("GET-CATEGORIES", "AddEditRecipeScreen categoriesAll size = ${viewModel.categoriesAll.size}")

            viewModel.categoriesAll.forEach { category ->
                DropdownMenuItem(
                    text = { Text(category.name) },
                    onClick = {
                        viewModel.onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}