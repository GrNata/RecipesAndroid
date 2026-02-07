package com.grig.recipesandroid.ui.colorScheme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
//    primary = Color(0xFF6750A4),    //  основной акцентный цвет (кнопки, активные элементы)
//    primary = Color(0xFF9D9598),    //  основной акцентный цвет (кнопки, активные элементы)
    primary = Color(0xFFD2C2C7),    //  основной акцентный цвет (кнопки, активные элементы)
    onPrimary = Color(0xFF585559),  //  цвет текста/иконок на основном акцентном цвете.
//    primaryContainer = Color(0xFF59595C),   //  фон для контейнеров с основным акцентом (например, карточки)
    primaryContainer = Color(0xFF767173),   //  фон для контейнеров с основным акцентом (например, карточки)
//    primaryContainer = Color(0xFF9C9AA5),   //  фон для контейнеров с основным акцентом (например, карточки)
    onPrimaryContainer = Color(0xFFCBCAD2), //  текст/иконки на primaryContainer

    secondary = Color(0xFFE8DFE2),    //  вторичный акцентный цвет (менее важные кнопки)
    onSecondary = Color(0xFF59595C),  //  текст/иконки на вторичном акценте.
    secondaryContainer = Color(0xFFF7F3EC),   //  фон для контейнеров со вторичным акцентом

    tertiaryContainer = Color(0xFFCCA590),  //
    tertiary = Color(0xFFAB886C), //  третичный акцент (редкие элементы, например, метки)
    onTertiary = Color(0xFF883F58),   //  текст на tertiaryContainer

    background = Color(0xFFfff5f5), //  основной цвет фона экрана
//    background = Color(0xFFE8DBE0), //  основной цвет фона экрана
//    onBackground = Color(), //  цвет основного текста на фоне

    surface = Color(0xFF3C326B),  //  цвет поверхностей (карточки, диалоги).
    onSurface = Color(0xFF127475),    //  текст/иконки на поверхностях
//    surfaceVariant = Color(),   //  альтернативный цвет поверхностей (например, разделители)

//    error = Color(),    //  цвет для ошибок (красные кнопки, сообщения)
//    onError = Color(0xFFf2542d),    //  текст/иконки на ошибке.
//    errorContainer = Color(),   //  фон для контейнеров с ошибками
//    onErrorContainer = Color(), //  текст на errorContainer

//    outline = Color(),  //  цвет контуров (например, границы полей ввода)

//    inversePrimary = Color(),   //  инвертированный основной цвет (для контраста)
//    inverseSurface = Color(),   //  инвертированный цвет поверхности.

//    surfaceTint = Color(),  //  оттенок для выделения поверхностей (например, при наведении).
//    scrim = Color(),    //  полупрозрачный слой (например, для оверлеев).



    // другие параметры
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB951),
    onPrimary = Color(0xFF452B00),
    primaryContainer = Color(0xFF633F00),
    onPrimaryContainer = Color(0xFFFFDDB3),
    // другие параметры
)

// Экспортируемые схемы
    val LightThemeColors = LightColorScheme
    val DarkThemeColors = DarkColorScheme