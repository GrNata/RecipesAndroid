package com.grig.recipesandroid.ui.adminStatistic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.grig.recipesandroid.data.model.auth.AdminStatisticsDto

@Composable
fun StatsContent(
    stats: AdminStatisticsDto
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        StatsCard("Всего пользователей:", stats.totalUsers)
        StatsCard("Всего рецептов:", stats.totalRecipes)
        StatsCard("Всего ингредиентов:", stats.totalIngredients)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Рецепты по категориям (value):",
            fontWeight = FontWeight.Bold
        )

        stats.popularCategoriesValue.forEach { (category, count) ->
            Text(
                "$category: $count"
            )
        }
    }
}