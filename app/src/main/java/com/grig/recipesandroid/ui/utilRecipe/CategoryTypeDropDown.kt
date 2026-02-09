package com.grig.recipesandroid.ui.utilRecipe

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.data.model.dto.CategoryTypeDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTypeDropDown(
    categoryTypes: List<CategoryTypeDto>,
    selectedId: Long,
    onSelected: (Long) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Log.d("ADMIN", "CategoryTypeDropDown: 1 after присваивания categoryTypes: $categoryTypes")

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = categoryTypes.find { it.id == selectedId }?.nameType ?: "",
            onValueChange = {  },
            readOnly = true,
            label = { Text("Группировка по категориям") },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            colors = TextFieldDefaults.colors(  // <-- Настройка цветов TextField
                // ЦВЕТА В ФОКУСЕ (когда меню открыто/поле активно)
                focusedTextColor = Color(0xFF3E0F41),           // текст при фокусе
                focusedLabelColor = Color(0xFF3C326B),       // лейбл при фокусе
//                focusedLabelColor = Color(0xFF612F65),       // лейбл при фокусе
                focusedContainerColor = Color(0xFFEEE8DC),     // фон при фокусе
//                focusedContainerColor = Color(0xFFD8D7CF),     // фон при фокусе
//                focusedContainerColor = Color(0xFFF7F2E9),     // фон при фокусе
                cursorColor = Color(0xFF062444),             // курсор
                // ЦВЕТА БЕЗ ФОКУСА (когда меню закрыто/поле неактивно)
                unfocusedTextColor = Color(0xFF663D4B),       // текст без фокуса
//                unfocusedLabelColor = Color(0xFF1E364F),     // лейбл без фокуса
                unfocusedLabelColor = Color(0xFF883F58),     // лейбл без фокуса
                unfocusedContainerColor = Color(0xFFF7F3EC),  // фон без фокуса ← ВАЖНО!
                // ДОП. ЦВЕТА
                unfocusedTrailingIconColor = Color(0xFF883F58),
                focusedTrailingIconColor = Color(0xFFC3758F)
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = Color(0xFFF7EDE9)  //
        ) {
            categoryTypes.forEach { type ->
                DropdownMenuItem(
                    modifier = Modifier
                        .padding(end = 120.dp)
                        .background(Color(0xFFEEE8DC)),
//                        .background(Color(0xFFCBCAD2)),
//                        .background(Color(0xFFD8D7D2)),
                    text = { Text(
                        text = type.nameType,
                        color = Color(0xFF3C326B),
                        modifier = Modifier.padding(start = 16.dp)
                    ) },
                    onClick = {
                        onSelected(type.id)
                        expanded = false
                    }
                )
            }
        }
    }

}