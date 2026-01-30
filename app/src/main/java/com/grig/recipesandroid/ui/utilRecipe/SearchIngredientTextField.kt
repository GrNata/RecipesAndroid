package com.grig.recipesandroid.ui.utilRecipe

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.data.model.dto.IngredientDto
import com.grig.recipesandroid.ui.search_by_ingredients.SearchByIngredientsViewModel

@Composable
fun SearchIngredientTextField(
    ingredientsAll: List<IngredientDto>,
    ingredientsViewModel: SearchByIngredientsViewModel
) {
    val selectedIngredients = ingredientsViewModel.selectedIngredients
    val searchQuery by ingredientsViewModel.searchQuery.collectAsState()
    val filteredList = remember { ingredientsViewModel.filteredIngredients }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
//        Строка поиска
        TextField(
            value = searchQuery,
            onValueChange = { ingredientsViewModel.setSearchQuery(it, ingredientsAll) },
            label = { Text("Поиск ингредиента") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
//                .background(Color(0xFFFFFFFF)),
//            textStyle = TextStyle(color = Color(0xFF123C69))
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color(0xFF0D0849),
                unfocusedTextColor = Color(0xFF123D69),
                cursorColor = Color(0xFF1E364F),
                focusedContainerColor = Color(0xFFEFEFEF),
                unfocusedContainerColor = Color(0xFFF7F7F7)
            )
        )

//        отфильтрованный список (показывается только при вводе)
        if (searchQuery.isNotEmpty() && filteredList.value.isNotEmpty()) {
            Divider()
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
                    .background(Color(0xFFEFEFEF))
            ) {
                items(filteredList.value) { ingredient ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { ingredientsViewModel.addIngredient(ingredient) }
                            .padding(12.dp)
                    ) {
                        Text(
                            ingredient.name,
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF123C69)
                            )
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Добавить",
                            tint = Color(0xFF123C69)
                        )
                    }
                }
            }
        }
//        Выбранные ингредиенты (динамический список)
        if (selectedIngredients.isNotEmpty()) {
            Text(
                "Выбранны:",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF123C69)
            )

            LazyColumn(
                modifier = Modifier.background(Color(0xFFEFEFEF))
            ) {
                items(selectedIngredients) { ingredient ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            ingredient.name,
                            modifier = Modifier.weight(1f),
                            color = Color(0xFF123C69)
                            )
                        IconButton(
                            onClick = { ingredientsViewModel.removeIngredient(ingredient) },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Удалить",
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}