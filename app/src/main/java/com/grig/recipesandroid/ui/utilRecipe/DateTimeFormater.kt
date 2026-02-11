package com.grig.recipesandroid.ui.utilRecipe

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun DateTimeFormater(
    dateTime: LocalDateTime
) : String {
    val formater = DateTimeFormatter.ofPattern("dd-MM-yyyy, HH:mm")

    val result = dateTime.format(formater)

    return result
}

fun millisToDateString(millis: Long) : String =
    Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))