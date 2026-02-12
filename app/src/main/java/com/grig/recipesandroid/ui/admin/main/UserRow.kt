package com.grig.recipesandroid.ui.admin.main

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.data.model.auth.BlockUserRequest
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleResponse
import com.grig.recipesandroid.data.model.auth.UserRequest
import com.grig.recipesandroid.ui.utilRecipe.DateTimeFormater


@Composable
fun UserRow(
    user: UserRequest,
    adminViewModel: AdminViewModel
) {

    var isExpanded by remember { mutableStateOf(false) }

//    var isCheckedAdmin by remember { mutableStateOf(false) }

//    val isCheckedAdmin = if (user.roles.contains("ADMIN")) true else false
    val hasAdminRole = "ADMIN" in user.roles
    val isBlocked = user.blocked


    Card(
        onClick = { isExpanded = true },
        modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(2.dp)),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(Color(0xFFFFFBFB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .background(Color(0xFFFFFBFB))
        ) {
            Row(modifier = Modifier.fillMaxWidth().padding(1.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Имя:",
                        color = Color(0xFF3C326B),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(modifier = Modifier.weight(3f)) {
                    Text(
                        "${user.username}",
                        color = Color(0xFF6F6AB8),
                        style = MaterialTheme.typography.bodyMedium
                        )
                }
            }
            Divider(
                color = Color(0xFF9D9598),
                thickness = 1.dp
            )

            Row(modifier = Modifier.fillMaxWidth().padding(1.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Email:",
                        color = Color(0xFF3C326B),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(modifier = Modifier.weight(3f)) {
                    Text(
                        "${user.email}",
                        color = Color(0xFF6F6AB8),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Divider(
                color = Color(0xFF9D9598),
                thickness = 1.dp
            )

            Row(modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp, start = 1.dp, bottom = 1.dp)
                .height(20.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        "Роли:",
                        color = Color(0xFF3C326B),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(modifier = Modifier.weight(2f)) {
                    Row() {
                        user.roles.forEach { role ->
                            if (role == "ADMIN") {
                                Text(
                                    text = "$role, ",
                                    color = Color(0xFF58142B),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            } else {
                                Text(
                                    text = "$role, ",
                                    color = Color(0xFF6F6AB8),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }

                        }
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
//                    if (!user.roles.contains("ADMIN")) {
                        Checkbox(
//                            checked = isCheckedAdmin,
                            checked = hasAdminRole,
                            onCheckedChange = { checked ->
                                // добавляем роль ADMIN
                                val roles = user.roles.toMutableSet()
                                roles.add("ADMIN")
                                val newRoles = UpdateUserRoleResponse(roles)
                                if (user.id != null) adminViewModel.updateRole(user.id, newRoles)
                            },
                            modifier = Modifier.size(0.1.dp),
                            // если уже есть ADMIN — игнорируем снятие галочки
//                            enabled = true,
                            enabled = !hasAdminRole, // запрещаем убрать ADMIN
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF883F58),
                                checkmarkColor = Color.White,
                                uncheckedColor = Color(0xFFCDA090)
                            )
                        )
                    }
//                else {
//                        Checkbox(
//                            checked = true,
//                            onCheckedChange = {
//                                if (user.id != null && !user.roles.contains("ADMIN")) {
//                                    val roles = user.roles.toMutableSet()
//                                    roles.add("ADMIN")
//                                    val newRoles = UpdateUserRoleResponse(roles)
//                                    adminViewModel.updateRole(user.id, newRoles)
//                                }
//                            },
//                            modifier = Modifier.size(0.1.dp),
//                            colors = CheckboxDefaults.colors(
//                                checkedColor = Color(0xFF883F58),
//                                checkmarkColor = Color.White,
//                                uncheckedColor = Color(0xFFCDA090)
//                            )
//                        )
//                    }
//                }
            }
            Divider(
                color = Color(0xFF9D9598),
                thickness = 1.dp
            )

            Row(modifier = Modifier.fillMaxWidth().padding(1.dp)) {
                Column(modifier = Modifier.weight(2f)) {
                    Text(
                        "Дата регистрации:",
                        color = Color(0xFF3C326B),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        user.registrationDate,
//                        "${user.registrationDate}",
                        color = Color(0xFF6F6AB8),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Divider(
                color = Color(0xFF9D9598),
                thickness = 1.dp
            )

            Log.d("DATE LOG", "UserRow: lastLoginAt: ${user.lastLoginAt}")
            Row(modifier = Modifier.fillMaxWidth().padding(2.dp)) {
                Column(modifier = Modifier.weight(2f)) {
                    Text(
                        "Дата послед. логир.:",
                        color = Color(0xFF3C326B),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        user.lastLoginAt,
//                        "${user.registrationDate}",
                        color = Color(0xFF6F6AB8),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
            Divider(
                color = Color(0xFF9D9598),
                thickness = 1.dp
            )

            Row(modifier = Modifier.fillMaxWidth()
                .padding(top = 10.dp, start = 1.dp, bottom = 1.dp)
                .height(20.dp)
            ) {
                Column(modifier = Modifier.weight(2f)) {
                    Text(
                        "Блокировка:",
                        color = Color(0xFF3C326B),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    if (user.blocked) {
                        Text(
                            "${user.blocked}",
                            color = Color(0xFF58142B),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        Text(
                            "${user.blocked}",
                            color = Color(0xFF6F6AB8),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Checkbox(
                        checked = isBlocked,
                        onCheckedChange = { newValue ->
                            val newIsBlocked = BlockUserRequest(newValue)
                            user.id?.let { adminViewModel.updateBlockedUser(it, newIsBlocked) }
                        },
                        modifier = Modifier.size(0.1.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFF883F58),   // закрашенная
                            checkmarkColor = Color.White,               // галочка белая
                            uncheckedColor = Color(0xFFCDA090)      // незакрашенная
                        )
                    )
                }
            }

        }
    }
}