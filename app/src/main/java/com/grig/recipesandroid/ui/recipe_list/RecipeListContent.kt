package com.grig.recipesandroid.ui.recipe_list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import com.grig.recipesandroid.domain.model.Recipe
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

//   разделяем UI и state - RecipeListScreen

@Composable
fun RecipeListContent(
    recipes: LazyPagingItems<Recipe>,
    onRecipeClick: (Recipe) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {

        items(count = recipes.itemCount) { index ->
            val recipe = recipes[index]
            recipe?.let {
                RecipeItem(recipe = it) {
                    onRecipeClick(it)
                }
            }
        }

        when (recipes.loadState.append) {
            is LoadState.Loading -> {
                item { CircularProgressIndicator() }
            }
            is LoadState.Error -> {
                item { Text("Ошибка загрузки") }
            }
            else -> Unit
        }
    }


//    LazyColumn(
//    modifier = Modifier.fillMaxSize()
//    ) {
//        items(recipes) { recipe ->
//            RecipeItem(recipe = recipe) {
//                onRecipeClick(recipe)
//            }
//        }
//    }

}