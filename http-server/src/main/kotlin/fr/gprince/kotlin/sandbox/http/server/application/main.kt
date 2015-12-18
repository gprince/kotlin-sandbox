package fr.gprince.kotlin.sandbox.http.server.application

import com.codahale.metrics.JmxReporter
import com.codahale.metrics.MetricRegistry
import fr.gprince.kotlin.sandbox.http.server.jackson.config.JacksonConfig
import fr.gprince.kotlin.sandbox.http.server.service.SimpleService
import io.undertow.Undertow

public val metricRegistry = MetricRegistry()
public val jmxReporter = JmxReporter.forRegistry(metricRegistry).build()

fun main(args: Array<String>) {

    System.setProperty("org.jboss.resteasy.port", 8080.toString())
    System.setProperty("org.jboss.resteasy.host", "localhost")

    server {

        val deployment = deployment {
            resources = arrayListOf<Any>(SimpleService())
            providerFactory = providerFactory {
                registerProvider(JacksonConfig::class.java)
            }
        }

        val deploymentInfo = undertowDeployment(deployment).
                setDeploymentName("").
                setContextPath("/").
                setClassLoader(Thread.currentThread().contextClassLoader)

        deploy(deploymentInfo)
    }.start(Undertow.builder().addHttpListener(8080, "localhost"))
}


