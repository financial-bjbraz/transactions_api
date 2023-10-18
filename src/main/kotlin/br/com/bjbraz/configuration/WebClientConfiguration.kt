package br.com.bjbraz.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient
import java.util.concurrent.TimeUnit


@Configuration
class WebClientConfiguration {

    @Bean
    fun webClient(): WebClient {

        val tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .doOnConnected { connection: Connection ->
                    connection.addHandlerLast(ReadTimeoutHandler(15000, TimeUnit.MILLISECONDS))
                    connection.addHandlerLast(WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                }

        return WebClient
                .builder()
                .clientConnector(ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper().registerModule(KotlinModule())
    }
}
