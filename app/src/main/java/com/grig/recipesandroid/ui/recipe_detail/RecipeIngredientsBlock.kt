package com.grig.recipesandroid.ui.recipe_detail

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.data.model.ui.IngredientUi
import com.grig.recipesandroid.domain.model.RecipeIngredient
import kotlinx.coroutines.flow.StateFlow

@Composable
fun RecipeIngredientsBlock(
    viewModel: RecipeDetailViewModel,
//    ingredientsUi: List<IngredientUi>
) {
    var selectedIngredient by remember {
        mutableStateOf<IngredientUi?>(null)
    }

//    var ingredientsUiChange: MutableList<IngredientUi> = viewModel.ingredientsUi.value
    val ingredientsUiChange by viewModel.ingredientsUi.collectAsState()

    Log.d("INGREDIENT-UI", "RecipeIngredientsBlock: IngredientUi: ${ingredientsUiChange.size}")
    Log.d("INGREDIENT-UI", "RecipeIngredientsBlock: selectedIngredient-1: ${selectedIngredient}")

    Text(
        text = "-  +    Ингредиенты:",
        style = MaterialTheme.typography.bodyLarge,
        color = Color(0xFF1E364F),
//        color = Color(0xFF123D69),
        modifier = Modifier.padding(start = 5.dp)
    )
//    viewModel.ingredientUi.forEach { ingredientUi ->
    ingredientsUiChange.forEach { ingredientUi ->
        IngredientRow(
            ingredient = ingredientUi,
            onClick = { selectedIngredient = ingredientUi},
            viewModel = viewModel
        )
    }

    selectedIngredient?.let { ingredientUi ->
        AmountInputDialog(
            ingredient = ingredientUi,
            onDismiss = { selectedIngredient = null },
            onConfirm = { newAmount ->
                viewModel.recalculateFrom(
                    ingredientId = ingredientUi.id,
                    newAmount = newAmount,
//                    ingredientsUi2 = ingredientsUiChange
                )
                Log.d("INGREDIENT-UI", "RecipeIngredientsBlock: newAmount: ${newAmount}")
                Log.d("INGREDIENT-UI", "RecipeIngredientsBlock: ingredientsUiChange: ${ingredientsUiChange}")
                selectedIngredient = null
            }
        )
        Log.d("INGREDIENT-UI", "RecipeIngredientsBlock: selectedIngredient-2: ${selectedIngredient}")
    }
}