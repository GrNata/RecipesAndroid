package com.grig.recipesandroid.ui.admin

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleRequest
import com.grig.recipesandroid.data.model.auth.UpdateUserRoleResponse
import com.grig.recipesandroid.data.model.auth.UserRequest


@Composable
fun UserRow(
    user: UserRequest,
    onUpdateRole: (Long, UpdateUserRoleResponse) -> Unit
//    onUpdateRole: (Long, UpdateUserRoleRequest) -> Unit
) {

    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.padding(8.dp),
        onClick = { isExpanded = true }
    ) {
        Column(
//            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Имя: ${user.username}"
            )
            Text(
                "Email: ${user.email}"
            )
            Text(
                "Роли: ${user.roles}"
            )
            Text(
                "Дата: ${user.registrationDate}"
            )
            Text(
                "Блокировка: ${user.blocked}"
            )

            Log.d("ADMIN", "UserRow: isExpanded: ${isExpanded}")

//            if (isExpanded) {

                Row() {
                    if (user.id != null && !user.roles.contains("ADMIN")) {
                        val roles = user.roles.toMutableSet()
                        roles.add("ADMIN")
                        val newRoles = UpdateUserRoleResponse(roles)

                        Button(onClick = { onUpdateRole(user.id, newRoles) }) {
                            Text("Сделать админом")
                        }
                    }

            }
        }
    }
}