package com.grig.recipesandroid.ui.admin.auditLogs

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun FilterRadioGroup(
    title: String,
    options: List<String>,
    selected: String?,
    onSelect: (String?) -> Unit
) {

    Column() {
        Text(
            title,
            style = MaterialTheme.typography.titleSmall
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .weight(1f)
//                        .fillMaxWidth()
                        .clickable { onSelect(option) }
                ) {
                    Column() {
                        RadioButton(
                            selected = selected == option,
                            onClick = { onSelect(option) }
                        )
                        Text(
                            option,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

            }

//            вариант Все
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onSelect(null) }
            ) {
                Row(
                    modifier = Modifier
//                        .fillMaxWidth()
                ) {
                    Column() {
                        RadioButton(
                            selected = selected == null,
                            onClick = { onSelect(null) }
                        )
                        Text(
                            "Все",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }

}