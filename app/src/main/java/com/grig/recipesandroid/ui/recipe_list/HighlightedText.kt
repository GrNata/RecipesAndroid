package com.grig.recipesandroid.ui.recipe_list

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.withStyle

//•	Подсветка текста: если query != "", в названии рецепта можно выделить совпадение цветом через AnnotatedString.
//	•	Фильтрация Sticky headers: показывать только категории, где есть совпадающие рецепты.
@Composable
fun HighlightedText(
    text: String,
    query: String,
    style: TextStyle,
    color: Color
) {

    val annotatedString = buildAnnotatedString {

        if (query.isEmpty()) {
            append(text)
        } else {
            var startIndex = 0
            val lowerText = text.lowercase()
            val lowerQuery = query.lowercase()

            while (true) {
                val index = lowerText.indexOf(lowerQuery, startIndex)

                if (index == -1) {
                    // добавляем остаток текста
                    append(text.substring(startIndex))
                    break
                }
                // добавляем текст до совпадения
                append(text.substring(startIndex, index))
                // добавляем совпадение с подсветкой
                withStyle(SpanStyle(color = Color.Red)) {
                    append(text.substring(index, index + query.length))
                }
                startIndex = index + query.length
            }
        }
    }

    Text(
        text = annotatedString,
        style = style,
        color = color
    )

}