package com.grig.recipesandroid.ui.admin.auditLogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grig.recipesandroid.data.model.auth.AdminAuditLogDto

@Composable
fun AuditLogRow(
    log: AdminAuditLogDto
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(
            "${log.actionType} ${log.entityType}",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge
            )

        Text(
            log.description,
//            style = MaterialTheme.typography.bodyMedium
            fontSize = 12.sp,
            )

        Text(
            "Admin: ${log.adminEmail}",
            color = Color.Gray,
            fontSize = 12.sp,
        )

        Text(
            log.createdAt,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }

}