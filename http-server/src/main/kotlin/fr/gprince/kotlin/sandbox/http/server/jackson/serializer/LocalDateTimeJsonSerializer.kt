package fr.gprince.kotlin.sandbox.http.server.jackson.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * # A [JsonSerializer] that serialize a given [LocalDateTime] to String using the [DateTimeFormatter.ISO_DATE_TIME] formatter
 */
class LocalDateTimeJsonSerializer : JsonSerializer<LocalDateTime>() {

    override fun serialize(value: LocalDateTime?, gen: JsonGenerator?, serializers: SerializerProvider?) {
        gen?.writeObject(value?.format(DateTimeFormatter.ISO_DATE_TIME))
    }
}