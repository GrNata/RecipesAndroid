package com.grig.recipesandroid.ui.admin.auditLogs

import android.util.Log
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.ui.admin.main.AdminViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAuditDropDown(
    auditViewModel: AdminAuditViewModel
) {
    var expanded by remember { mutableStateOf(false) }

//    val optionsActionType = listOf("Все", "USER", "ADMIN")
    val selectedActionType by auditViewModel.selectedActionType.collectAsState()
    val selectedEntityType by auditViewModel.selectedEntityType.collectAsState()
    val selectedFrom by auditViewModel.selectedFrom.collectAsState()
    val selectedTo by auditViewModel.selectedTo.collectAsState()

    val filter by auditViewModel.filter.collectAsState()

//    val optionsBlocked = listOf("Все", "False", "True")
//    val selectedBlocked by adminViewModel.blockedFilter.collectAsState()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded  =!expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp)
    ) {
        TextField(
//            value = selectedOption,
            value = "Действие: ${selectedActionType ?: "Все"};   Тип сущ.: ${selectedEntityType ?: " Все"}\n" +
                    "Дата: c ${selectedFrom} по ${selectedTo}",
            onValueChange = {},  //  Не меняем вручную — только через выбор из списка
            readOnly = true, // Запрещаем ручной ввод
            label = { Text("Выбор параметра для фильтрации") },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(), // Связывает поле с меню
            colors = TextFieldDefaults.colors(
                unfocusedTextColor = Color(0xFF019EA6), // когда не в фокусе
                focusedTextColor = Color(0xFF58142B),   // когда в фокусе
                unfocusedLabelColor = Color(0xFF019EA6),
                focusedLabelColor = Color(0xFF58142B),
                cursorColor = Color(0xFF019EA6),
//                focusedContainerColor =
//                unfocusedContainerColor =
            )
        )
        // Раскрывающийся список
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            AdminAuditFilter(auditViewModel)

//            Log.d("ADMIN", "DropDownForFIlterUser: BLOCKED selectedBlocked = $selectedBlocked")
        }
    }

}