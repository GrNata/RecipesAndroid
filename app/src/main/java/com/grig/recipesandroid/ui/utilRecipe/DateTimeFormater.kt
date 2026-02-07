package com.grig.recipesandroid.ui.utilRecipe

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun DateTimeFormater(
    dateTime: LocalDateTime
) : String {
    val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm")

    val result = dateTime.format(formater)

    return result
}