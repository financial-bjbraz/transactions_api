package br.com.bjbraz.handler

import br.com.bjbraz.dto.Boleto
import br.com.bjbraz.dto.DocTed
import br.com.bjbraz.service.FinancialService
import br.com.bjbraz.service.getString
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse.*
import java.net.URI

@Component
class FinancialHandler (
        val financialService: FinancialService
    ) {
    private val logger = LoggerFactory.getLogger(FinancialHandler::class.java)

    fun boletoPayment(request: ServerRequest) = request.bodyToMono(Boleto::class.java)
            .flatMap {
                financialService.boletoPayment(it)
            }
            .flatMap {item -> created(URI.create("/cards/$item")).build()
            }
            .doOnError { status(HttpStatus.INTERNAL_SERVER_ERROR).build() }

    fun docted(request: ServerRequest) = request.bodyToMono(DocTed::class.java)
            .flatMap {
                financialService.docted(it)
            }
            .flatMap {externalId -> created(URI.create("/docted/$externalId")).build()
            }

    fun findBoletoByExternalId(request: ServerRequest) = financialService.findByExternalId(externalId = getString("externalId", request))?.let { it ->
        ok().contentType(MediaType.APPLICATION_JSON).bodyValue(it)
    }?: notFound().build()

}