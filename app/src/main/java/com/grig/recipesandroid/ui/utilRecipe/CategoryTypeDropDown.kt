package com.grig.recipesandroid.ui.utilRecipe

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
            label = { Text("Группировка по категориям") },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            colors = TextFieldDefaults.colors(  // <-- Настройка цветов TextField
                // ЦВЕТА В ФОКУСЕ (когда меню открыто/поле активно)
                focusedTextColor = Color(0xFF02172C),           // текст при фокусе
                focusedLabelColor = Color(0xFF062444),       // лейбл при фокусе
                focusedContainerColor = Color(0xFFEEE2DC),     // фон при фокусе
                cursorColor = Color(0xFF062444),             // курсор
                // ЦВЕТА БЕЗ ФОКУСА (когда меню закрыто/поле неактивно)
                unfocusedTextColor = Color(0xFF1E364F),       // текст без фокуса
                unfocusedLabelColor = Color(0xFF1E364F),     // лейбл без фокуса
                unfocusedContainerColor = Color(0xFFEEE2DC),  // фон без фокуса ← ВАЖНО!
                // ДОП. ЦВЕТА
//                disabledTextColor = Color.LightGray,
//                disabledContainerColor = Color(0xFFEEEEEE),
//                trailingIconColor = Color(0xFF1E364F)       // цвет иконки-стрелки
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color(0xFFF7EDE9)  //
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