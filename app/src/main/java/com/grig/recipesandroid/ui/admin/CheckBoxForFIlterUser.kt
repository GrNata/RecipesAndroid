package com.grig.recipesandroid.ui.admin

import android.util.Log
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//@Composable
//fun CheckBoxForRole(
//    isRole: Boolean
//) : Boolean {
//
////    var isRole by remember { mutableStateOf(false) }
////    val isChooseRole = false
//
//    Checkbox(
//        checked = isRole,
//        onCheckedChange = { isRole  },
////        onCheckedChange = { isRole = it },
//        modifier = Modifier.size(0.1.dp),
//        enabled = true,
//        colors = CheckboxDefaults.colors(
//            checkedColor = Color(0xFF883F58),
//            checkmarkColor = Color(0xFF883F58),
//            uncheckedColor = Color(0xFFCDA090)
//        )
//    )
//    Log.d("ADMIN", "CheckBoxForRole: isRole = $isRole")
//    return isRole
//}
//
//@Composable
//fun CheckBoxForBlocked(
//
//) {
//    var isBlocked by remember { mutableStateOf(false) }
//
//    Checkbox(
//        checked = isBlocked,
//        onCheckedChange = { isBlocked = it },
//        modifier = Modifier.size(0.1.dp),
//        enabled = true,
//        colors = CheckboxDefaults.colors(
//            checkedColor = Color(0xFF883F58),
//            checkmarkColor = Color(0xFF883F58),
//            uncheckedColor = Color(0xFFCDA090)
//        )
//    )
//}

