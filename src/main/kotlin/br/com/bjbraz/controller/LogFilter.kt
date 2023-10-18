package br.com.bjbraz.controller

import br.com.bjbraz.dto.RequestReponse
import br.com.bjbraz.repository.RequestReponseRepository
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer

//@Component
class LogFilter (val requestLogger: RequestLogger, val requestRepository: RequestReponseRepository) : WebFilter {

    private val logger = LoggerFactory.getLogger(LogFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        try {
            val startTime = System.currentTimeMillis()
            val path = exchange.request.uri.path
            logger.info("Serving '{}'", path)

            return chain.filter(exchange).doAfterTerminate {
                exchange.response.headers.entries.forEach(Consumer<Map.Entry<String?, List<String?>?>> { e: Map.Entry<String?, List<String?>?> -> logger.info("Response header '{}': {}", e.key, e.value) })
                val time = System.currentTimeMillis() - startTime
                val info = ("Served '$path' as ${exchange.response.statusCode} in $time msec")

                logger.info(info)

                logRequestResponse(requestLogger.getRequestMessage(exchange), requestLogger.getResponseMessage(exchange), info, exchange.request.uri.path)

                exchange.response.beforeCommit {
                    Mono.empty()
                }

            }
        }catch (ex:Exception){
            logger.error("Error filtering $ex")
        }

        return chain.filter(exchange)
    }

    private fun logRequestResponse(request: String, response: String, method:String, uri:String) {
        try {
            logger.info("Request : $request")
            logger.info("Response : $response")

            val respJson = JSONObject()
            respJson.put("payload", request)

            val dataHoje: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())

            requestRepository.save(RequestReponse(UUID.randomUUID().toString(), request, response, 999, "already informed", method, uri, dataHoje))
        }catch(ex:Exception){
            logger.error("Error filtering $ex")
        }
    }


}