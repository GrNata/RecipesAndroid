package com.grig.recipesandroid.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//@Composable
//fun ChooseFilterUsers(
//    adminViewModel: AdminViewModel
//) {
//
//    val queryAdmin by adminViewModel.queryAdmin.collectAsState()
//
//    //        поиск / фильтрация
//    OutlinedTextField(
//        value = queryAdmin,
//        onValueChange = { newText ->
//            adminViewModel.setQueryAdmin(newText)
//        },
//        modifier = Modifier
//            .height(50.dp)
//            .background(MaterialTheme.colorScheme.secondaryContainer)
////                .weight(1f)
//        ,
//        placeholder = {
//            Text(
//                "Фильтрация…",
////                                color = MaterialTheme.colorScheme.onTertiary,
//                color = Color(0xFF663D4B),
//                style = MaterialTheme.typography.bodyMedium
//            )
//        },
//        singleLine = true,
//        colors = OutlinedTextFieldDefaults.colors(
//            focusedBorderColor = MaterialTheme.colorScheme.onTertiary,        // рамка при фокусе
//            unfocusedBorderColor = MaterialTheme.colorScheme.primary,     // рамка без фокуса
////                            errorBorderColor = Color.Red,         // рамка в состоянии ошибки
//            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,      // цвет лейбла при фокусе
//            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,      // цвет лейбла без фокуса
//            focusedTextColor = Color(0xFF663D4B)
//        )
//    )
//}
//
