package com.grig.recipesandroid.ui.recipe_detail

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
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

    RecipeDetailContent(
        recipe = recipe,
        loading = loading,
        error = error,
        onBack = onBack
    )

//    Scaffold(
//        topBar = {
////            TopAppBar(
//            CenterAlignedTopAppBar(
//                title = {
//                    Text(
//                        text = recipe?.name ?: "Рецепт",
//                        style = MaterialTheme.typography.titleLarge,
////                        color = Color(0xFF9A663B)
//                        color = Color(0xFF245C5C),
//
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = onBack) {
//                        Icon(
//                            imageVector = Icons.Default.ArrowBack,
//                            contentDescription = "Назад"
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFF245C5C)
//                )
//            )
//        }
//    ) { paddingValues ->
//
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//            .padding(paddingValues)
////            .background(Color(0xFFEEE2DC))
////            .background(Color(0xFFF6ECD7))
////            .background(Color(0xFFF6E5D7))
//                .background(Color(0xFFEEE2DC))
//        ) {
//            when {
//                loading -> {
//                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//                }
//                error != null -> {
//                    Text(
//                        text = error ?: "Ошибка",
//                        modifier = Modifier.align(Alignment.Center),
////                    color = Color(0xFFC40C4A)
//                        color = Color(0xFF9A3B3B)
//                    )
//                }
//                recipe != null -> {
//                    LazyColumn(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .padding(16.dp)
//                    ) {
//                        item {
//                            Text(
//                                requireNotNull(recipe).description ?: "",
//                                modifier = Modifier.padding(bottom = 8.dp),
////                            color = Color(0xFF701332),
//                                color = Color(0xFF9A3B3B),
//                                style = MaterialTheme.typography.titleLarge
//                            )
//                            Row(modifier = Modifier.padding(6.dp)) {
//
//                                requireNotNull(recipe).image?.let { imageUrl ->
//                                    AsyncImage(
//                                        model = imageUrl,
//                                        contentDescription = requireNotNull(recipe).name,
//                                        modifier = Modifier
////                                        .fillMaxWidth()
//                                            .height(180.dp)
//                                            .padding(bottom = 8.dp)
//                                            .clip(RoundedCornerShape(8.dp))
//                                    )
//                                }
//                                Column() {
//                                    Text(
//                                        text = "Категория: ${requireNotNull(recipe).categories.joinToString { it.name.lowercase() }}",
////                            color = Color(0xFF8B8789),
////                            color = Color(0xFFB29393),
//                                        color = Color(0xFF7E889F),
//                                        textAlign = TextAlign.Center,
//                                        style = MaterialTheme.typography.bodyLarge,
//                                        modifier = Modifier.padding(start = 16.dp)
//                                    )
//                                    Spacer(modifier = Modifier.height(12.dp))
//                                    Text(
//                                        text = "Ингредиенты:",
//                                        color = Color(0xFF656A77),
//                                        style = MaterialTheme.typography.bodyLarge,
//                                        modifier = Modifier.padding(start = 16.dp)
//                                    )
//                                    requireNotNull(recipe).ingredients.forEach { ing ->
//                                        Text(
//                                            text = "- ${ing.ingredient.name.lowercase()}: ${ing.amount ?: ""} ${ing.unit}",
//                                            color = Color(0xFF656A77),
//                                            style = MaterialTheme.typography.bodyMedium,
//                                            modifier = Modifier.padding(start = 16.dp)
//                                        )
//
//                                    }
//                                }
//
//                            }
//
//                            Spacer(modifier = Modifier.height(8.dp))
//                            Text(
//                                text = "Шаги приготовления:",
//                                color = Color(0xFF123C69),
//                                style = MaterialTheme.typography.bodyLarge,
//                                modifier = Modifier.padding(bottom = 8.dp)
//                            )
//                            requireNotNull(recipe).steps.forEach { step ->
//                                Text(
//                                    text = "- ${step}",
//                                    modifier = Modifier.padding(bottom = 4.dp),
//                                    color = Color(0xFF123C69),
//                                    style = MaterialTheme.typography.bodyMedium
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//
//    }

}