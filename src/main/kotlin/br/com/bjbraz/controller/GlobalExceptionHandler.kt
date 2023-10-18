package br.com.bjbraz.controller

import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

fun vndError(errors: List<Map<String, String?>>): Map<String, Any> {
    return mapOf(
            "_embedded" to mapOf(
                    "errors" to errors
            ),
            "total" to errors.size)
}

fun returnInternalError(): Mono<ServerResponse> {
    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .bodyValue(vndError(listOf(mapOf("logref" to "SERVER_ERROR", "message" to "Server error"))))
}

fun returnNotFound(it: Throwable): Mono<ServerResponse> {
    return ServerResponse.status(HttpStatus.NOT_FOUND)
            .bodyValue(vndError(listOf(mapOf("logref" to "NOT_FOUND", "message" to it.message))))
}

fun returnUnprocessable(it: Throwable): Mono<ServerResponse> {
    return ServerResponse.status(HttpStatus.UNPROCESSABLE_ENTITY)
            .bodyValue(vndError(listOf(mapOf("logref" to "INVALID", "message" to it.message))))
}
