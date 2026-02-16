package com.grig.recipesandroid.ui.utilRecipe

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.grig.recipesandroid.ui.admin.main.AdminViewModel
import com.grig.recipesandroid.ui.recipe_list.RecipesViewModel

//@Composable
//fun ShowDialogDelete(
//    title: String,
//    showDeleteDialog: Boolean,
//    recipesViewModel: RecipesViewModel,
//    recipeId: Long?
//): Boolean {
//
//    AlertDialog(
//        onDismissRequest = { showDeleteDialog = false },
//        title = {
//            Text("Подтверждение удаления")
//        },
//        text = {
//            Text("Вы уверены, что хотите удалить этого пользователя?")
//        },
//        confirmButton = {
//            TextButton(
//                onClick = {
//                    showDeleteDialog = false
//                    recipesViewModel.deleteRecipe()
//                }
//            ) {
//                Text(text = "Удалить", color =  Color.Red)
//            }
//        },
//        dismissButton = {
//            TextButton(
//                onClick = { showDeleteDialog = false }
//            ) {
//                Text(text = "Отмена", color = Color.Blue)
//            }
//        }
//    )
//
//    return showDeleteDialog
//}