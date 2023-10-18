package br.com.bjbraz.service

import br.com.bjbraz.dto.Boleto
import br.com.bjbraz.dto.DocTed
import br.com.bjbraz.dto.DocTedRequest
import br.com.bjbraz.dto.DocTedResponse
import br.com.bjbraz.entity.boleto.BoletoEntity
import br.com.bjbraz.entity.docted.DocTedEntity
import reactor.core.publisher.Mono

interface FinancialService {
    fun boletoPayment(boleto: Boleto) : Mono<String>
    fun docted(docTed: DocTed) : Mono<String>
    fun findByExternalId(externalId: String) : Mono<BoletoEntity>
    suspend fun requestBoletoPayment(boleto: BoletoEntity) : Boleto
    fun persistDocTed(docTed: DocTed) : DocTedEntity
    suspend fun requestDocTed(docTed: DocTedRequest) : DocTedResponse

}