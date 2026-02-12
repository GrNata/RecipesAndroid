package com.grig.recipesandroid.ui.admin.main

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ComponentDropDownFilterEmail(
    adminViewModel: AdminViewModel
) {
    val emailFilter by adminViewModel.emailFilter.collectAsState()
    val emailError by adminViewModel.emailError.collectAsState()

    Spacer(Modifier.height(6.dp))

    Row(modifier = Modifier.fillMaxWidth().padding(start = 10.dp)) {
        Column(Modifier.weight(4f)) {
            Text(
                text = "Email:",
                modifier = Modifier.padding(start = 12.dp)
            )

            TextField(
                value = emailFilter.orEmpty(),
                onValueChange = {
                    adminViewModel.setEmailFilter(it)
                },
                placeholder = { Text("Пример: email@mail.ru") },
                singleLine = true,
                isError = emailError != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            emailError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .padding(start = 16.dp, top = 2.dp)
                )
            }
        }
        Column(Modifier.weight(1f)) {
            Text(
                text = "",
            )

            IconButton(
                onClick = {
                    Log.d("ADMIN", "ComponentDropDown: emailError: ${emailError}" )
                    adminViewModel.loadUsers()
                          },
//                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "найти пользователя по Email",
                    tint = MaterialTheme.colorScheme.surface
//                                        tint = Color(0xFF123C69)
                )
            }
        }
    }
}