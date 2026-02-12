package com.grig.recipesandroid.ui.admin.calendary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grig.recipesandroid.ui.utilRecipe.millisToDateString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    value: String?,
    onDateSelected: (String?) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    OutlinedTextField(
        value = value ?: "",
        onValueChange = {},
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.surface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedContainerColor = Color(0xFFFFFFFF),
            unfocusedContainerColor = Color(0xFFD7D7D7),
            focusedLabelColor = MaterialTheme.colorScheme.surface,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
        ),
        textStyle = TextStyle(
            fontSize = 15.sp,
//            color =
        ),
        label = { Text(
            label,
//            fontSize =
            ) },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            Row() {
                if (value != null) {
                    IconButton(onClick = { onDateSelected(null) }) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "Очистить",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onTertiary
                            )
                    }
                }
                IconButton(onClick = { showDialog = true }
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Календарь",
                        modifier = Modifier.size(20.dp),
                        tint = Color(0xFF473972)
                        )

                }
            }
        }
    )

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onDateSelected(millisToDateString(millis))
                        }
                        showDialog = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Отмена")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}