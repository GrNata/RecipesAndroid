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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.grig.recipesandroid.data.model.auth.AuthorStars

@Composable
fun AuthorBarChartGraph(
    authours: List<AuthorStars>
) {
//    График №2 — топ авторов

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            "Топ авторы",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.padding(10.dp)
        )

        Spacer(Modifier.height(8.dp))

        val max = authours.maxOfOrNull { it.recipeCount } ?: 1

        authours.forEachIndexed { index, author ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .padding(start = 10.dp)
            ) {

                Column(
                    modifier = Modifier.weight(5f)
                ) {
                    Row() {
                        Box(
                            modifier = Modifier
                                .height(16.dp)
                                .fillMaxWidth((author.recipeCount.toFloat() / max ) / 2)
                                .background(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    shape = MaterialTheme.shapes.small
                                )
                        )

                        Spacer(Modifier.width(12.dp))

                        Text(
                            "${index + 1}. ${author.username}",
                            modifier = Modifier.width(100.dp),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.surface
                        )
                    }

                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        author.recipeCount.toString(),
                        modifier = Modifier.padding(start = 8.dp),
                        color = MaterialTheme.colorScheme.surface
                    )
                }

            }
        }
    }
}