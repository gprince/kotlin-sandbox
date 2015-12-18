package fr.gprince.kotlin.sandbox.http.server.application

import com.codahale.metrics.JmxReporter
import com.codahale.metrics.MetricRegistry
import fr.gprince.kotlin.sandbox.http.server.service.JacksonConfig
import fr.gprince.kotlin.sandbox.http.server.service.SimpleService
import io.undertow.Undertow
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer
import org.jboss.resteasy.spi.ResteasyDeployment
import org.jboss.resteasy.spi.ResteasyProviderFactory

fun deployment(config: ResteasyDeployment.() -> Unit = {}): ResteasyDeployment {
    val deployment = ResteasyDeployment()
    deployment.config()
    return deployment
}

fun providerFactory(config: ResteasyProviderFactory.() -> Unit = {}): ResteasyProviderFactory {
    val provider = ResteasyProviderFactory()
    provider.config()
    return provider
}

fun server(config: UndertowJaxrsServer.() -> Unit = {}): UndertowJaxrsServer {
    val server = UndertowJaxrsServer()
    server.config()
    return server
}

public val metricRegistry = MetricRegistry()

fun main(args: Array<String>) {

    val jmxReporter = JmxReporter.forRegistry(metricRegistry).build()

    System.setProperty("org.jboss.resteasy.port", 8080.toString())
    System.setProperty("org.jboss.resteasy.host", "localhost")

    val deployment = deployment {
        resources = arrayListOf<Any>(SimpleService())
        providerFactory = providerFactory {
            registerProvider(JacksonConfig::class.java)
        }
    }

    server {

        val deploymentInfo = undertowDeployment(deployment).
                setDeploymentName("").
                setContextPath("/").
                setClassLoader(Thread.currentThread().contextClassLoader)

        deploy(deploymentInfo)
    }.start(Undertow.builder().addHttpListener(8080, "localhost"))
}


