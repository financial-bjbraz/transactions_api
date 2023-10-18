package br.com.bjbraz.service

import br.com.bjbraz.dto.*
import br.com.bjbraz.entity.card.CardEntity
import br.com.bjbraz.repository.RequestReponseRepository
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import java.text.SimpleDateFormat
import java.util.*

@Service
class GenerateCardImpl (@Value("\${ppi.card.url}") val ppiUrl: String,
                        val requestRepository: RequestReponseRepository,
                        val authenticateService: AuthImpl,
                        val webClient: WebClient) : GenerateCard {

    private val logger = LoggerFactory.getLogger(GenerateCardImpl::class.java)

    override fun requestHubCard(cardEntity: CardEntity): Boolean {
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{
            val card : Card = convertEntity(cardEntity)
            val jsonObject = JSONObject(card)
            myJson = jsonObject.toString()
            logger.info(myJson)

            val resp : ClientResponse = webClient.post()
                    .uri(ppiUrl)
                    .body(BodyInserters.fromValue(card))
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .exchange().block()!!

            statusCode = resp.rawStatusCode()
            myJsonResp = getResponse(resp)

            saveRequest(cardEntity.externalCode!! , myJson, token, statusCode, myJsonResp, requestRepository, ppiUrl)

            return true

        }catch(ex:Exception){
            saveRequest(cardEntity.externalCode!! , myJson, token, 422, myJsonResp + ex.message,requestRepository, ppiUrl)
            throw ex
        }

    }

}