package com.grig.recipesandroid.ui.admin.statistic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(16.dp)
        ) {
            Text(
                "Рецепты по категориям (value):",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.surface
            )

            Spacer(Modifier.height(10.dp))
            stats.popularCategoriesValue.forEach { (category, count) ->
                Row() {
                    Column(modifier = Modifier.weight(2f)) {
                        Text(
                            "$category",
                            color = MaterialTheme.colorScheme.surface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "$count",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }

            Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondary)
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(16.dp)
            ) {
                Text(
                    "Топ авторов (количество рецептов):",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.surface
                )
                Spacer(Modifier.height(10.dp))

                stats.topAuthors.forEach {
                    Row() {
                        Column(modifier = Modifier.weight(2f)) {
                            Text(
                                "${it.username}",
                                color = MaterialTheme.colorScheme.surface,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "${it.recipeCount}",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
//                        Column(modifier = Modifier.weight(1f)) {
//                            Text(
//                                " рецептов",
//                                color = MaterialTheme.colorScheme.surface,
//                                style = MaterialTheme.typography.bodyMedium
//                            )
//                        }
                    }
                }
            }
        }

    }
}