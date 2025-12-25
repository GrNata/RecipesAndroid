package com.grig.recipesandroid.ui.recipe_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun RecipeDetailScreen(
    recipeId: Long,
    viewModel: RecipeDetailViewModel = viewModel(),
    onBack : () -> Unit
) {
    val recipe by viewModel.recipe.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Загружаем рецепт при первом отображении
    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        when {
            loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            error != null -> {
                Text(
                    text = error ?: "Ошибка",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            recipe != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    item {
                        Text(requireNotNull(recipe).description ?: "", modifier = Modifier.padding(bottom = 8.dp))
                        requireNotNull(recipe).image?.let { imageUrl ->
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = requireNotNull(recipe).name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(bottom = 8.dp)
                            )
                        }
                        Text(requireNotNull(recipe).description ?: "", modifier = Modifier.padding(bottom = 8.dp))
                        Text("Категория: ${requireNotNull(recipe).categories.joinToString { it.name }}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Ингредиенты:")
                        requireNotNull(recipe).ingredients.forEach { ing ->
                            Text("- ${ing.ingredient.name}: ${ing.amount ?: ""} ${ing.unit}")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Шаги:")
                        requireNotNull(recipe).steps.forEach { step ->
                            Text("- ${step}", modifier = Modifier.padding(bottom = 4.dp))
                        }
                    }
                }
            }
        }
    }
}