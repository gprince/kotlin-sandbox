package fr.gprince.kotlin.sandbox.http.server.domain

import java.time.LocalDateTime

/**
 * <h1>Sample API response that deliver a Hello message</h1>
 */
data class HelloMessage(
        val message: String,
        val time: LocalDateTime)