package fr.gprince.kotlin.sandbox.http.server.domain

import java.time.LocalDateTime

/**
 * # Sample API response that deliver a Hello message
 * @property message The message delivered to the say endpoint
 * @property time
 */
data class HelloMessage(
        val message: String,
        val time: LocalDateTime)