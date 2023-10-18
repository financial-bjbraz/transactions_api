package br.com.bjbraz.service

import br.com.bjbraz.dto.BalanceDTO
import br.com.bjbraz.dto.BlockUnBlockCard
import br.com.bjbraz.entity.account.AccountEntity
import br.com.bjbraz.entity.card.CardEntity
import br.com.bjbraz.entity.card.CardStatus
import br.com.bjbraz.exception.InvalidResponseException
import br.com.bjbraz.repository.AccountRepository
import br.com.bjbraz.repository.CardRepository
import br.com.bjbraz.repository.RequestReponseRepository
import br.com.bjbraz.repository.SQLAccountRepository
import br.com.bjbraz.repository.SQLCardRepository
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import java.util.*
import kotlin.NullPointerException


@Service
class CardServiceImpl(@Value("\${ppi.cardBalance.url}") val balanceUrl: String,
                      @Value("\${ppi.cardBlock.url}") val blockUrl: String,
                      @Value("\${ppi.cardUnBlock.url}") val unBlockUrl: String,
                      @Value("\${ppi.accountUnBlock.url}") val unBlockAccountUrl: String,
                      @Value("\${ppi.accountBlock.url}") val blockAccountUrl: String,
                      val requestRepository: RequestReponseRepository,
                      val authenticateService: AuthImpl,
                      val webClient: WebClient,

                      val cardRepository: CardRepository,
                      val sqlCardRepository: SQLCardRepository,

                      val accountRepository: AccountRepository,
                      val sqlAccountRepository: SQLAccountRepository

                      ) : CardService {

    private val logger = LoggerFactory.getLogger(CardServiceImpl::class.java)

    override suspend fun getBalance(externalCode: String): BalanceDTO {
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{
          val balance =  webClient.get()
                    .uri(balanceUrl, externalCode)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                     .retrieve()
                     .awaitBody<BalanceDTO>()

            statusCode = 200
            val jsonObjectResp = JSONObject(balance)
            myJsonResp = jsonObjectResp.toString()

            saveRequest(externalCode , myJson, token, statusCode, myJsonResp, requestRepository, balanceUrl)

            return balance

        }catch(ex:Exception){
            saveRequest(externalCode , myJson, token, 422, myJsonResp + ex.message,requestRepository, balanceUrl)
            throw ex
        }
    }

    override fun blockCard(externalCode: String):Boolean {
        try {
            val card: Optional<CardEntity> = sqlCardRepository.findByExternalCode(externalCode)

            if (!card.isPresent)
                throw NullPointerException("Invalid External Code")

            val cardEntity = card.get()

            val blockUnblock: BlockUnBlockCard = prepareObjectCard(cardEntity)

            requestBlock(externalCode)

            cardEntity.status = CardStatus.BLOCKED
            cardRepository.save(cardEntity)
            sqlCardRepository.save(cardEntity)

            return true
        }catch (ex:Exception){
            throw ex
        }
    }

    private fun requestBlock(externalCode:String) : Boolean {
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            val resp : ClientResponse = webClient.post()
                    .uri(blockUrl, externalCode)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .exchange().block()!!

            statusCode = resp.rawStatusCode()
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()

            if(statusCode != HttpStatus.OK.value())
                throw InvalidResponseException("Received invalid Response Status: $statusCode")

            saveRequest(externalCode!! , myJson, token, statusCode, myJsonResp, requestRepository, balanceUrl)
            return true
        }catch(ex:Exception){
            saveRequest(externalCode!! , myJson, token, 422, myJsonResp + ex.message,requestRepository, balanceUrl)
            throw ex
        }
    }

    override fun unBlockCard(externalCode: String):Boolean {
        val card : Optional<CardEntity> = sqlCardRepository.findByExternalCode(externalCode)

        if(!card.isPresent)
            throw NullPointerException("Invalid External Code")

        val cardEntity = card.get()

        val blockUnblock: BlockUnBlockCard = prepareObjectCard(cardEntity)

        requestUnblock(blockUnblock)

        cardEntity.status = CardStatus.UNBLOCKED
        cardRepository.save(cardEntity)
        sqlCardRepository.save(cardEntity)

        return true
    }

    override fun blockAccount(cpf: String): Boolean {
        try {


            return true
        }catch (ex:Exception){
            logger.error("Error blocking Account $ex")
            throw ex
        }

        return false
    }

    override fun unBlockAccount(cpf: String): Boolean {
        try {

            return true
        }catch(ex:Exception){
            logger.error("Error unBlocking Account $ex")
            throw ex
        }

        return false
    }

    private fun requestUnblock(blockUnblock: BlockUnBlockCard) {
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            val resp : ClientResponse = webClient.post()
                    .uri(unBlockUrl, blockUnblock.externalCode)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .exchange().block()!!

            statusCode = resp.rawStatusCode()
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()

            saveRequest(blockUnblock.externalCode!! , myJson, token, statusCode, myJsonResp, requestRepository, balanceUrl)

            if(statusCode != HttpStatus.OK.value())
                throw InvalidResponseException("Received invalid Response Status: $statusCode")

        }catch(ex:Exception){
            saveRequest(blockUnblock.externalCode!! , myJson, token, 422, myJsonResp + ex.message,requestRepository, balanceUrl)
            throw ex
        }
    }

    private fun requestBlockUnblockAccount(blockUnblock: BlockUnBlockCard, url:String) {
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 204
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            val resp : ClientResponse = webClient.post()
                    .uri(url , blockUnblock.financialOperationKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .exchange().block()!!

            statusCode = resp.rawStatusCode()
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()

            saveRequest(blockUnblock.externalCode!! , myJson, token, statusCode, myJsonResp, requestRepository, balanceUrl)

            if(statusCode != HttpStatus.NO_CONTENT.value())
                throw InvalidResponseException("Received invalid Response Status: $statusCode")

        }catch(ex:Exception){
            saveRequest(blockUnblock.externalCode!! , myJson, token, 422, myJsonResp + ex.message,requestRepository, balanceUrl)
            throw ex
        }
    }

    private fun requestBlockAccount(blockUnblock: BlockUnBlockCard) {
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            val resp : ClientResponse = webClient.post()
                    .uri(blockAccountUrl, blockUnblock.financialOperationKey)
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .exchange().block()!!

            statusCode = resp.rawStatusCode()
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()

            saveRequest(blockUnblock.externalCode!! , myJson, token, statusCode, myJsonResp, requestRepository, balanceUrl)

            if(statusCode != HttpStatus.OK.value())
                throw InvalidResponseException("Received invalid Response Status: $statusCode")

        }catch(ex:Exception){
            saveRequest(blockUnblock.externalCode!! , myJson, token, 422, myJsonResp + ex.message,requestRepository, balanceUrl)
            throw ex
        }
    }

    private fun prepareObjectCard(cardEntity:CardEntity): BlockUnBlockCard {
        val truncatedNumberLasFourDigits = cardEntity.truncatedNumber!!.substring(cardEntity.truncatedNumber!!.length -5, cardEntity.truncatedNumber!!.length-1)
        return BlockUnBlockCard(externalCode = cardEntity.externalCode, lastFourDigits = truncatedNumberLasFourDigits)
    }

    private fun prepareObjectAccount(account:AccountEntity): BlockUnBlockCard {
        return BlockUnBlockCard(financialOperationKey = account.financialOperationKey)
    }

}