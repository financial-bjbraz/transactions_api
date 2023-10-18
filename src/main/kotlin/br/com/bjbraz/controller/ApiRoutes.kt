package br.com.bjbraz.controller


import br.com.bjbraz.dto.Boleto
import br.com.bjbraz.dto.RequestReponse
import br.com.bjbraz.exception.CardNotFoundException
import br.com.bjbraz.exception.InvalidCardIdException
import br.com.bjbraz.exception.VndErrors
import br.com.bjbraz.handler.ApiHandler
import br.com.bjbraz.handler.FinancialHandler
import br.com.bjbraz.repository.RequestReponseRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springdoc.core.annotations.RouterOperation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.coRouter
import org.springframework.web.reactive.function.server.router
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*


@Configuration
class ApiRoutes(val requestRepository: RequestReponseRepository) {
    private val logger = LoggerFactory.getLogger(ApiRoutes::class.java)

//    @Bean
//    fun routerAccount(handler: ApiHandler)= router {
//        ("/").nest {
//            GET("") { _ ->
//                ServerResponse.temporaryRedirect(URI.create("/swagger-ui.html")).build()
//            }
//            POST("/message/{message}", handler::sendTestMessage)
//        }
//        ("/accounts" and accept(APPLICATION_JSON)).nest{
//            PUT("", handler::addAccount)
//            PATCH("", handler::addAccount)
//            POST("/block/{cpf}", handler::blockAccount)
//            POST("/unblock/{cpf}", handler::unblockAccount)
//            POST("/transfer", handler::transfer)
//        }
//    }.filter{request, next -> next.handle(request)}
//
//    @Bean
//    fun routerCard(handler: ApiHandler)= router {
//        ("/cards" and accept(APPLICATION_JSON)).nest{
//            POST("/{cpf}", handler::addCard)
//            POST("/reissue", handler::reissueCard)
//            POST("/charge/{cpf}/{value}", handler::chargeCard)
//            POST("/block/{externalCode}", handler::blockCard)
//            POST("/unblock/{externalCode}", handler::unblockCard)
//
//        }
//    }.filter{request, next -> next.handle(request)}

//    @Bean
//    fun routerBalanceAccountBalance(handler: ApiHandler)= coRouter {
//        ("/accounts" and accept(APPLICATION_JSON)).nest{
//            GET("/balance/{cpf}", handler::accountBalance)
//            GET("/{cpf}", handler::getAccount)
//        }
//    }.filter{request, next -> next.handle(request)}
//
//    @Bean
//    fun routerBalanceCardBalance(handler: ApiHandler)= coRouter {
//        ("/cards" and accept(APPLICATION_JSON)).nest{
//            POST("/balance/{externalCode}", handler::cardBalance)
//        }
//    }.filter{request, next -> next.handle(request)}

    @Bean
    @RouterOperation(
        operation = Operation(
            operationId = "Payment Credit", summary = "Register Payment Order Credit", tags = ["Payment"],
            parameters = [
                Parameter(
                    `in` = ParameterIn.HEADER,
                    name = "Authorization",
                    description = "Token Oauth 2.0",
                    required = true
                ),
                Parameter(
                    `in` = ParameterIn.HEADER,
                    name = "Chave-Idempotencia",
                    description = "Chave de Idempotencia",
                    required = true
                )
            ],
            requestBody = RequestBody(
                description = "Payment Credit Request", content = [Content(
                    schema = Schema(
                        implementation = Boleto::
                        class
                    ), mediaType = MediaType.APPLICATION_JSON_VALUE
                )], required = true
            ),
            responses = [
                ApiResponse(
                    responseCode = "200", description = "Successful operation", content = [Content(
                        schema = Schema(
                            implementation = Boleto::
                            class
                        ), mediaType = MediaType.APPLICATION_JSON_VALUE
                    )]
                ),
                ApiResponse(
                    responseCode = "400", description = "Bad Request", content = [Content(
                        schema = Schema(
                            implementation = VndErrors::
                            class
                        ), mediaType = MediaType.APPLICATION_JSON_VALUE
                    )]
                ),
                ApiResponse(
                    responseCode = "401", description = "Unauthorized", content = [Content(
                        schema = Schema(
                            implementation = VndErrors::
                            class
                        ), mediaType = MediaType.APPLICATION_JSON_VALUE
                    )]
                ),
                ApiResponse(
                    responseCode = "422", description = "Request Failed", content = [Content(
                        schema = Schema(
                            implementation = VndErrors::
                            class
                        ), mediaType = MediaType.APPLICATION_JSON_VALUE
                    )]
                )
            ]
        )
    )
    fun financialRouter(handler: FinancialHandler) = router {
        ("financial" and accept(APPLICATION_JSON)).nest {
            POST("/boleto", handler::boletoPayment)
            GET("/boleto/{externalId}", handler::findBoletoByExternalId)
            POST("/docted", handler::docted)
        }
    }.filter { request, next -> next.handle(request) }

    @Bean
    fun apiRouter(handler: ApiHandler) = coRouter {
        ("accounts" and accept(APPLICATION_JSON)).nest {
            GET("/all", handler::getAllAccounts)
            after { request, response -> logRequestResponse(request, response) }
        }

    }.filter { request, next ->
        next.handle(request).onErrorResume { ex ->
            when (ex) {
                is CardNotFoundException -> {
                    logger.warn("Not found: ${ex.message}")
                    returnNotFound(ex)
                }
                is InvalidCardIdException -> {
                    logger.warn("${ex.message}")
                    returnUnprocessable(ex)
                }
                else -> {
                    logger.error("Internal error: ${ex.message}", ex)
                    returnInternalError()
                }
            }
        }
    }

    private fun logRequestResponse(request: ServerRequest, response: ServerResponse): ServerResponse {
        logger.info("Request : $request")
        logger.info("Response : $response")

        val bodyData: JSONObject? = request.bodyToMono(JSONObject::class.java).toProcessor().peek()
        val dataHoje: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
        val reqValue = bodyData.toString()
        val respValue = (response as org.springframework.web.reactive.function.server.EntityResponse<Any>).entity()

        val list: List<*> = ArrayList<Any>(listOf(respValue))

        val respJson = JSONObject()
        respJson.put("payload", list)

        val statusCode = response.rawStatusCode()

        val userAgent = request.headers().asHttpHeaders()["User-Agent"]
        val accept = request.headers().asHttpHeaders()["Accept"]
        val host = request.headers().asHttpHeaders()["Host"]
        val acceptEncoding = request.headers().asHttpHeaders()["Accept-Encoding"]
        val connection = request.headers().asHttpHeaders()["Connection"]

        val headerValues =
            "User-Agent: $userAgent Accept: $accept Host: $host Accept-Encoding:$acceptEncoding Connection:$connection"

        val methodName = request.methodName()
        val uriValue = request.uri().toString()

        requestRepository.save(
            RequestReponse(
                UUID.randomUUID().toString(),
                reqValue,
                respJson?.toString(),
                statusCode,
                headerValues,
                methodName,
                uriValue,
                dataHoje
            )
        )

        return response
    }

    private fun logRequest(request: ServerRequest): ServerRequest {
        logger.info("Request : $request")
        return request
    }

    private fun logResponse(response: ServerResponse): ServerResponse {
        logger.info("Status Code: ${response.statusCode()}")
        logger.info("Response from Server: $response")
        return response
    }

}