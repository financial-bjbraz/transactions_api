package br.com.bjbraz.controller

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange

@Component
class RequestLogger {

    fun getRequestMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val method = request.method
        val path = request.uri.path
        val params = request.queryParams
        val acceptableMediaTypes = request.headers.accept
        val contentType = request.headers.contentType

        return ">>> $method $path ${HttpHeaders.ACCEPT}: $acceptableMediaTypes ${HttpHeaders.CONTENT_TYPE}: $contentType "+"params: $params"
    }

    fun getResponseMessage(exchange: ServerWebExchange): String {
        val request = exchange.request
        val response = exchange.response
        val method = request.method
        val path = request.uri.path
        val params = request.queryParams
        val statusCode = getStatus(response)
        val contentType = response.headers.contentType
        return "<<< $method $path HTTP${statusCode?.value()} ${statusCode?.reasonPhrase} ${HttpHeaders.CONTENT_TYPE}: $contentType"+"params: $params"
    }

    private fun getStatus(response: ServerHttpResponse): HttpStatus? =
            try {
                response.statusCode
            } catch (ex: Exception) {
                HttpStatus.CONTINUE
            }
}