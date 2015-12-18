package fr.gprince.kotlin.sandbox.http.server.service


import com.codahale.metrics.MetricRegistry.name
import com.codahale.metrics.Timer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.gprince.kotlin.sandbox.http.server.application.metricRegistry
import fr.gprince.kotlin.sandbox.http.server.domain.HelloMessage
import fr.gprince.kotlin.sandbox.http.server.jackson.serializer.LocalDateTimeSerializer
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import java.io.Closeable
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import javax.annotation.security.RolesAllowed
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ContextResolver
import javax.ws.rs.ext.Provider

public inline fun <T: Closeable, R> T.using(block: (T) -> R): R =
        try {
            block(this)
            throw UnsupportedOperationException()
        } finally {
            this.close()
        }

/**
 * # Sample service
 */
@Api(value = "Name", description = "the sample API")
@Path("/sample")
@RolesAllowed("admin")
class SimpleService {

    /**
     * # the say Endpoint
     */
    @GET
    @Path("/say/{message}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Says hello and repeat the given message",
            response = HelloMessage::class)
    @ApiResponses(ApiResponse(code = 500, message = "Internal server error"))
    fun say(@PathParam("message") message: String?): Response =
            metricRegistry.timer(name(SimpleService::class.java, "say-service")).
                    time().
                    using {
                        Response.
                                ok(HelloMessage("""Hello, you said "${message ?: "nothing!"}"""", now())).
                                build()
                    } ?: Response.serverError().build()

}

@Provider
@Produces(MediaType.APPLICATION_JSON)
class JacksonConfig : ContextResolver<ObjectMapper> {

    override fun getContext(type: Class<*>?): ObjectMapper? {
        return jacksonObjectMapper().
                registerModule(
                        SimpleModule().
                                addSerializer(LocalDateTime::class.java, LocalDateTimeSerializer()))
    }

}

