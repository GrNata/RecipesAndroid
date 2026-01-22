package com.grig.recipesandroid.ui.utilRecipe

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.grig.recipesandroid.data.model.dto.CategoryTypeDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTypeDropDown(
    categoryTypes: List<CategoryTypeDto>,
    selectedId: Long,
    onSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = categoryTypes.find { it.id == selectedId }?.nameType ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Группировка") },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categoryTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.nameType) },
                    onClick = {
                        onSelected(type.id)
                        expanded = false
                    }
                )
            }
        }
    }

}