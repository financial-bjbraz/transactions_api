package br.com.bjbraz.handler

import br.com.bjbraz.repository.AccountRepository
import br.com.bjbraz.repository.CardRepository
import br.com.bjbraz.service.AccountServiceImpl
import br.com.bjbraz.service.CardService
import br.com.bjbraz.service.GenerateAccount
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.json


@Component
class ApiHandler(
        val getCardInfoOnPremise: GenerateAccount,
        val accountRepository: AccountRepository,
        val cardRepository: CardRepository,
        val cardService: CardService,
        val accountService: AccountServiceImpl,
        val webClient: WebClient
        ) {
    private val logger = LoggerFactory.getLogger(ApiHandler::class.java)

    suspend fun getAllAccounts(request: ServerRequest):ServerResponse{
        return accountRepository.findAll()!!.let { ok().json().bodyValueAndAwait(it) }
    }

//    suspend fun getAccount(request: ServerRequest) = ok().json().bodyValueAndAwait(accountRepository.findByCpf(getString("cpf", request))!!.toJSON())

//    fun addCard(request: ServerRequest) = accountRepository.requestDefaultCard(cpf = getString("cpf", request))?.let { ok().build() }?: notFound().build()
//
//    fun blockCard(request: ServerRequest) = accountRepository.putCommandOnQueue(identifier = getString("externalCode", request), command = Command.BLOCK_CARD)?.let{ ok().contentType(MediaType.APPLICATION_JSON).build() }?: notFound().build()
//
//    fun unblockCard(request: ServerRequest) = accountRepository.putCommandOnQueue(identifier = getString("externalCode", request), command = Command.UNBLOCK_CARD)?.let{ ok().contentType(MediaType.APPLICATION_JSON).build() }?: notFound().build()
//
//    fun blockAccount(request: ServerRequest) = accountRepository.putCommandOnQueue(identifier = getString("cpf", request), command = Command.BLOCK_ACCOUNT)?.let{ ok().contentType(MediaType.APPLICATION_JSON).build() }?: notFound().build()
//
//    fun sendTestMessage(request: ServerRequest) = accountRepository.putCommandOnTestQueue(getString("message", request))?.let{ ok().contentType(MediaType.APPLICATION_JSON).build() }?: notFound().build()
//
//    fun unblockAccount(request: ServerRequest) = accountRepository.putCommandOnQueue(identifier = getString("cpf", request), command = Command.UNBLOCK_ACCOUNT)?.let{ ok().contentType(MediaType.APPLICATION_JSON).build() }?: notFound().build()
//
//    suspend fun cardBalance(request: ServerRequest):ServerResponse = cardService.getBalance(externalCode = getString("externalCode", request))?.let { it ->
//       ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(it)
//    }?: notFound().buildAndAwait()
//
//
//    suspend fun accountBalance(request: ServerRequest): ServerResponse= accountService.getBalance(financialOperationKey = accountRepository.cpfToFinancialOperationKey(getString("cpf", request))!! )?.let {
//        ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(it)
//    } ?: notFound().buildAndAwait()
//
//    fun transfer(request: ServerRequest) = request.bodyToMono(AccountTransferRequest::class.java)
//            .flatMap {
//                accountRepository.requestAccountTransfer(it)
//            }
//            .flatMap {item -> created(URI.create("/cards/$item")).build()
//            }
//
//    fun reissueCard(request: ServerRequest) = request.bodyToMono(Reissue::class.java)
//            .flatMap {
//                cardRepository.reissueCard(it)
//            }
//            .flatMap {item -> created(URI.create("/cards/$item")).build()
//            }
//
//    fun addAccount(request: ServerRequest) = request.bodyToMono(Account::class.java)
//            .flatMap {
//                this.accountRepository.saveAndSend(it)
//            }
//            .flatMap { item -> created(URI.create("/accounts/$item")).build() }
//
//    suspend fun getAccountInfo(request: ServerRequest):ServerResponse = accountService.getAccountInfo(cpf = getString("cpf", request))?.let { it ->
//        ok().contentType(MediaType.APPLICATION_JSON).bodyValueAndAwait(it)
//    }?: notFound().buildAndAwait()
//
//    fun chargeCard(request: ServerRequest) = cardRepository.chargeCardAssync(cpf = getString("cpf", request), valor = getDouble("value", request))?.let { ok().build() }?: notFound().build()

}

