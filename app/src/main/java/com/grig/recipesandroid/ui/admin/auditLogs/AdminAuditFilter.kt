package com.grig.recipesandroid.ui.admin.auditLogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.ui.admin.calendary.DatePickerField

@Composable
fun AdminAuditFilter(
    adminAuditViewModel: AdminAuditViewModel
) {

    val filter by adminAuditViewModel.filter.collectAsState()

//    State для фильтра
    val selectedActionType by remember { mutableStateOf<String?>(null) }
    val selectedEntityType by remember { mutableStateOf<String?>(null) }
    val fromDate by remember { mutableStateOf("") }
    val toDate by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.padding(10.dp)
    ) {
        // Radio buttons actionType
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            FilterRadioGroup(
                title = "Действие",
                options = listOf("CREATE", "UPDATE", "DELETE"),
                selected = filter.actionType,
                onSelect = adminAuditViewModel::setActionType
            )
        }
        Divider(
            color = Color(0xFF9D9598),
            thickness = 0.3.dp
        )
        // Radio buttons entityType
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            FilterRadioGroup(
                title = "Тип сущности",
                options = listOf("INGREDIENT", "CATEGORY\nVALUE", "CATEGORY\nTYPE", "USER"),
                selected = filter.entityType,
                onSelect = adminAuditViewModel::setEntityType
            )
        }
        Divider(
            color = Color(0xFF9D9598),
            thickness = 0.3.dp
        )

        Spacer(Modifier.height(10.dp))

// Дата от и до (String в формате "dd-MM-yyyy")
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(modifier = Modifier
                .weight(1f)) {
                DatePickerField(
                    label = "C",
                    value = filter.from,
                    onDateSelected = adminAuditViewModel::setFrom
                )
            }
            Spacer(Modifier.height(8.dp))

            Column(modifier = Modifier
                .weight(1f)) {
                DatePickerField(
                    label = "По",
                    value = filter.to,
                    onDateSelected = adminAuditViewModel::setTo
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Divider(
            color = Color(0xFF9D9598),
            thickness = 0.3.dp
        )

        Row(modifier = Modifier
            .fillMaxWidth()
        ) {
//                    Кнопка применить фильтр
            Button(
                onClick = {
                    adminAuditViewModel.applyFilter()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Применить фильтр"
                )
            }
        }

        Divider(
            color = Color(0xFF9D9598),
            thickness = 0.3.dp
        )
    }
}