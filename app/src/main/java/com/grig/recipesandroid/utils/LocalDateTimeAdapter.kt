package com.grig.recipesandroid.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime

//object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
//    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)
//
//    override fun serialize(encoder: Encoder, value: LocalDateTime) {
//        encoder.encodeString(value.toString())
//    }
//
//    override fun deserialize(decoder: Decoder): LocalDateTime {
//        val string = decoder.decodeString()
//        return LocalDateTime.parse(string)
//    }
//}

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {

    override fun write(out: JsonWriter?, value: LocalDateTime?) {
        if (value == null) {
            out?.nullValue()
        } else {
            out?.value(value.toString())  // ISO-8601 формат
        }
    }

    override fun read(`in`: JsonReader?): LocalDateTime? {
        val string: String = `in`?.nextString() ?: return null
        return try {
            LocalDateTime.parse(string) // автоматически понимает ISO-8601
        } catch (e: Exception) {
            null    // если формат неверный
        }
    }
}
