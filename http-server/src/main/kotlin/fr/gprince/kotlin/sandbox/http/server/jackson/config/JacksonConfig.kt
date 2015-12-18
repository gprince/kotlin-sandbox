package fr.gprince.kotlin.sandbox.http.server.jackson.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.gprince.kotlin.sandbox.http.server.jackson.serializer.LocalDateTimeJsonSerializer
import java.time.LocalDateTime
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.ext.ContextResolver
import javax.ws.rs.ext.Provider

/**
 * #
 */
@Provider
@Produces(MediaType.APPLICATION_JSON)
class JacksonConfig : ContextResolver<ObjectMapper> {

    override fun getContext(type: Class<*>?): ObjectMapper? {
        return jacksonObjectMapper().
                registerModule(
                        SimpleModule().
                                addSerializer(LocalDateTime::class.java, LocalDateTimeJsonSerializer()))
    }

}