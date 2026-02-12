package com.grig.recipesandroid.ui.admin.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.ui.admin.calendary.DatePickerField

@Composable
fun ComponentDropDownDataFilter(
    adminViewModel: AdminViewModel
) {
    val lastLoginFrom by adminViewModel.lastLoginFrom.collectAsState()
    val lastLoginTo by adminViewModel.lastLoginTo.collectAsState()

    Spacer(Modifier.height(4.dp))

    Row(
        Modifier.fillMaxWidth()
    ) {
        Column(Modifier.weight(1f)) {
            DatePickerField(
                label = "Вход с: ",
                value = lastLoginFrom,
                onDateSelected = {
                    adminViewModel.setLastLoginFrom(it)
                    adminViewModel.loadUsers()
                }
            )
        }
        Spacer(Modifier.width(8.dp))

        Column(Modifier.weight(1f)) {
            DatePickerField(
                label = "По: ",
                value = lastLoginTo,
                onDateSelected = {
                    adminViewModel.setLastLoginTo(it)
                    adminViewModel.loadUsers()
                }
            )
        }
    }

}