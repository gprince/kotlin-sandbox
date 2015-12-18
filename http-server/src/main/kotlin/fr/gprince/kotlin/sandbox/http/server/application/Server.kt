package fr.gprince.kotlin.sandbox.http.server.application

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer
import org.jboss.resteasy.spi.ResteasyDeployment
import org.jboss.resteasy.spi.ResteasyProviderFactory


public inline fun deployment(config: ResteasyDeployment.() -> Unit): ResteasyDeployment {
    val deployment = ResteasyDeployment()
    deployment.config()
    return deployment
}

public inline fun providerFactory(config: ResteasyProviderFactory.() -> Unit): ResteasyProviderFactory {
    val provider = ResteasyProviderFactory()
    provider.config()
    return provider
}

public inline fun server(config: UndertowJaxrsServer.() -> Unit): UndertowJaxrsServer {
    val server = UndertowJaxrsServer()
    server.config()
    return server
}