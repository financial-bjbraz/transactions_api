package br.com.bjbraz.service

import br.com.bjbraz.dto.*
import br.com.bjbraz.repository.RequestReponseRepository
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import org.springframework.web.reactive.function.client.awaitExchange


@Service("accountServiceImpl")
class AccountServiceImpl(
        @Value("\${ppi.accountBalance.url}") val balanceUrl: String,
        @Value("\${ppi.accountCharge.url}") val accountChargeUrl: String,
        @Value("\${ppi.chargeCard.url}") val cardChargeUrl: String,
        @Value("\${ppi.statements.url}") val statementsUrl: String,
        private val authenticateService: AuthImpl,
        private val requestRepository: RequestReponseRepository,
        private val webClient: WebClient)  {

    private val logger = LoggerFactory.getLogger(AccountServiceImpl::class.java)

    suspend fun getBalance(fok: String): BalanceDTO? {
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            val resp : BalanceDTO = webClient.get()
                    .uri(balanceUrl.replace("{financialOperationKey}", fok, true))
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .retrieve()
                    .awaitBody<BalanceDTO>()

            statusCode = 200
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()

            saveRequest(fok , myJson, token, statusCode, myJsonResp, requestRepository, balanceUrl)

            return resp

        }catch(ex:Exception){
            saveRequest(fok , myJson, token, 422, myJsonResp + ex.message,requestRepository, balanceUrl)
            throw ex
        }
    }

    fun chargeCard(charge: Charge) {
        requestCharge(charge)
    }

    fun requestCharge(charge: Charge){
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            val resp : ClientResponse = webClient.post()
                    .uri(cardChargeUrl, charge.externalCode)
                    .body(BodyInserters.fromValue(charge))
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .exchange().block()!!

            statusCode = 200
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()
            saveRequest(charge.fok , myJson, token, statusCode, myJsonResp, requestRepository, balanceUrl)
        }catch(ex:Exception){
            saveRequest(charge.fok , myJson, token, 422, (myJsonResp + ex.message),requestRepository, balanceUrl)
            throw ex
        }
    }

    fun requestAccountTransfer(message: AccountTransferBody){
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            val resp : ClientResponse = webClient.post()
                    .uri(accountChargeUrl, message.fromFinancialOperationKey)
                    .body(BodyInserters.fromValue(message))
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .exchange().block()!!

            statusCode = 200
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()
            saveRequest(message.idempotencyKey , myJson, token, statusCode, myJsonResp, requestRepository, balanceUrl)
        }catch(ex:Exception){
            saveRequest(message.idempotencyKey , myJson, token, 422, (myJsonResp + ex.message),requestRepository, balanceUrl)
            throw ex
        }
    }

    suspend fun requestStatements() : AccountTransactions{
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            val resp : AccountTransactions = webClient.get()
                    .uri("https://qacst-ppi.hubprepaid.com.br/partner-interface/accounts/4426646/statement?startDate=2020-04-01&endDate=2020-05-07")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .awaitExchange()
                    .awaitBody<AccountTransactions>()

            statusCode = 200
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()
            saveRequest("fok" , myJson, token, statusCode, myJsonResp, requestRepository, balanceUrl)
            return resp
        }catch(ex:Exception){
            saveRequest("fok" , myJson, token, 422, (myJsonResp + ex.message),requestRepository, balanceUrl)
            throw ex
        }
    }

    suspend fun requestTransactionsFallBack(): AccountTransactions {
        val id = 1
        val obj = webClient.post()
                .uri("http://localhost:8081/test")
                .accept(MediaType.APPLICATION_JSON)
                .awaitExchange()
                .awaitBody<AccountTransactions>()

        return obj
    }

    suspend fun requestLocalTransactions(): AccountTransactions {
        val id = 1
        val obj = webClient.post()
                .uri("http://localhost:8081/test1")
                .accept(MediaType.APPLICATION_JSON)
                .awaitExchange()
                .awaitBody<AccountTransactions>()
        return obj
    }

    fun getAccountInfo(cpf: String): AccountInfo {
        var accountInfo : AccountInfo = AccountInfo(
                id = 1,
                cpf = "2222",
                financialOperationKey = 123,
                mainAccountId = 1,
                balance = 10.90,
                name = "alex",
                temCartao = true
        )

        return accountInfo
    }

}