package com.grig.recipesandroid.ui.admin.main

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownForFilterUser(
    adminViewModel: AdminViewModel
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }

    val optionsRole = listOf("Все", "USER", "ADMIN")
    val selectedRole by adminViewModel.roleFilter.collectAsState()

    val optionsBlocked = listOf("Все", "False", "True")
    val selectedBlocked by adminViewModel.blockedFilter.collectAsState()


    ExposedDropdownMenuBox(
        expanded = expanded,
//        onExpandedChange = { expanded  =!expanded },      //  закрывается само, после выбора
        onExpandedChange = { expanded  = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp)
    ) {
        TextField(
//            value = selectedOption,
            value = "Роль: ${selectedRole ?: "Все"};   Заблокирован: ${selectedBlocked ?: " Все"}",
            onValueChange = {},  //  Не меняем вручную — только через выбор из списка
            readOnly = true, // Запрещаем ручной ввод
            label = { Text("Выбор параметра для фильтрации") },
            trailingIcon = { TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor() // Связывает поле с меню
        )
        // Раскрывающийся список
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
                adminViewModel.loadUsers()
            }
        ) {

            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                optionsRole.forEach { option ->
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                            .selectable(
                                selected = (option == selectedRole),
                                onClick = {}
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = (option == (selectedRole ?: "Все")),
                            onClick = {
                                adminViewModel.setRoleFilter(if (option == "Все") null else option)
//                                adminViewModel.loadUsers()
                            },
//                                null, // Отключаем клик на самой кнопке (кликаем по Row)
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF58142B),      // цвет при выборе
                                unselectedColor = Color.LightGray // цвет когда не выбрана
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            option,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

            }

            Row(modifier = Modifier
                .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                optionsBlocked.forEach { option ->

                    val selectedBlockedValue = if (selectedBlocked == null) {
                        "Все"
                    } else if (selectedBlocked == true) {
                        "True"
                    } else {
                        "False"
                    }

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp)
                            .selectable(
                                selected = (option == selectedBlockedValue),
                                onClick = {}
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = (option == (selectedBlockedValue)),
                            onClick =
                                {
                                adminViewModel.setBlockedFilter(
                                        when (option) {
                                            "Все" -> null
                                            "True" -> true
                                            else -> false
                                        }
                                )
//                                adminViewModel.loadUsers()
                            },
//                                null, // Отключаем клик на самой кнопке (кликаем по Row)
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFF58142B),      // цвет при выборе
                                unselectedColor = Color.LightGray // цвет когда не выбрана
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            option,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            ComponentDropDownFilterEmail(adminViewModel)

            ComponentDropDownDataFilter(adminViewModel)

            Log.d("ADMIN", "DropDownForFilterUser: BLOCKED selectedBlocked = $selectedBlocked")
        }
    }
}