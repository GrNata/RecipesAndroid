package com.grig.recipesandroid.ui.admin.auditLogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grig.recipesandroid.data.model.auth.AdminAuditLogDto
import com.grig.recipesandroid.ui.utilRecipe.DateTimeFormater
import java.time.format.DateTimeFormatter

@Composable
fun AuditLogRow(
    log: AdminAuditLogDto
) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {

            Row() {
                Column(modifier = Modifier.weight(1f)) {
                    Row() {
                        Text(
                            "${log.actionType}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (log.actionType == "DELETE") {
                                Color(0xFFA60400)
                            } else if (log.actionType == "UPDATE") {
                                Color(0xFF019EA6)
                            } else {
                                Color(0xFF00A60B)
                            }
                        )
                        Text(
                            "  ${log.entityType}",
//                        fontWeight = FontWeight.Normal,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Admin: ${log.adminEmail}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.surface
                    )
                }
            }

            Row() {
                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        " ${log.description}",
                        fontSize = 14.sp,
                        color = Color.Gray,
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        log.createdAt,
                        color = MaterialTheme.colorScheme.surface,
                        fontSize = 12.sp
                    )
                }
            }
//        }
        Divider(
            color = Color(0xFF9D9598),
            thickness = 0.3.dp
        )
    }

}