package com.grig.recipesandroid.ui.admin.statistic.graph

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.grig.recipesandroid.data.model.auth.AdminStatisticsDto

@Composable
fun StatsSummaryRow(
    stats: AdminStatisticsDto
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        StatsCard("Пользователи", stats.totalUsers)
        StatsCard("Рецепты", stats.totalRecipes)
        StatsCard("Ингредиенты", stats.totalIngredients)

    }
}

