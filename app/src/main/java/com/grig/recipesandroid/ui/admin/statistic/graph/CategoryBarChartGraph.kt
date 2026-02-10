package com.grig.recipesandroid.ui.admin.statistic.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grig.recipesandroid.data.model.auth.CategoryStateValue
import okhttp3.internal.wait

@Composable
fun CategoryBarChartGraph(
    categories: List<CategoryStateValue>
) {
//     График №1 — популярные категории

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            "Популярные категории (количество рецептов)",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(10.dp)
        )

        Spacer(Modifier.height(8.dp))

        val max = categories.maxOfOrNull { it.recipeCount } ?: 1

        categories.forEach { category ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .padding(start = 10.dp)
            ) {

                Column(modifier = Modifier.weight(5f)) {
                    Row() {
                        //                ГРАФИК
//                        val lengthGraph =
                        Box(
                            modifier = Modifier
                                .height(18.dp)
//                                .width(
////                                    (120.dp * (category.recipeCount.toFloat() / (max * max)) )
//                                    (180.dp * (category.recipeCount.toFloat() / max) / 2)
//                                )
                                .background(
//                                    color = MaterialTheme.colorScheme.primary,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = MaterialTheme.shapes.small
                                )
                                .fillMaxWidth(category.recipeCount.toFloat() / (max * max))
                        )

                        Spacer(Modifier.width(12.dp))

//                Название категорий
                        Text(
                            category.categoryValueName,
                            modifier = Modifier.width(120.dp),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }

                }
//                Spacer(Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    //                Количество
                    Text(
                        category.recipeCount.toString(),
                        modifier = Modifier.padding(start = 12.dp),
                        color = MaterialTheme.colorScheme.surface
                    )

                }
            }
        }
    }
}