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
import java.text.SimpleDateFormat
import java.util.*

@Service
class ReissueCardImpl(
        @Value("\${ppi.card_reissue.url}") val ppiUrl: String,
        val requestRepository: RequestReponseRepository,
        val authenticateService: AuthImpl,
        val webClient: WebClient) : ReissueCard{

    private val logger = LoggerFactory.getLogger(ReissueCardImpl::class.java)

    override fun requestReissueHubCard(cardEntity: CardEntity, idempotency: String): Boolean {
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            val card : RequestReissue = convertEntity(cardEntity, idempotency)

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
            myJsonResp = "NO RESPONSE"

            saveRequest(cardEntity.externalCode!! , myJson, token, statusCode, myJsonResp, requestRepository, ppiUrl)

            return true

        }catch(ex:Exception){
            saveRequest(cardEntity.externalCode!! , myJson, token, 422, myJsonResp + ex.message, requestRepository, ppiUrl)
            throw ex
        }
    }

    fun convertEntity(cardEntity: CardEntity, idempotency:String): RequestReissue{

        val holderObj = cardEntity.holder!!
        val addressObj   = cardEntity.holder.address!!
        val deliveryObj  = cardEntity.delivery!!
        val recipientObj = deliveryObj.recipient!!
        val addressObj2   = recipientObj.address!!
        val plasticObj = cardEntity.embossing!!.plastic!!
        val letterObj  = cardEntity.embossing!!.letter!!


        val holderAddress = Address(
                street = addressObj.street,
                number = addressObj.number,
                district = addressObj.district,
                city = addressObj.city,
                state = addressObj.state,
                zipcode = addressObj.zipCode
        )

        val recipientAddress = Address(
                street = addressObj2.street,
                number = addressObj2.number,
                district = addressObj2.district,
                city = addressObj2.city,
                state = addressObj2.state,
                zipcode = addressObj2.zipCode
        )

        val holder=  Holder(
                name = holderObj.name,
                document = holderObj.document,
                email = holderObj.email,
                mobilePhone = holderObj.mobilePhone,
                homePhone = holderObj.homePhone,
                birthDate = holderObj.birthDate,
                gender = holderObj.gender,
                address = holderAddress
        )

        val recipient = Recipient(
                name = recipientObj.name,
                address = recipientAddress
        )

        val delivery = Delivery(
                recipient = recipient
        )

        val plastic = Plastic(
                holder = plasticObj.holder,
                message = plasticObj.message,
                image = plasticObj.image,
                imageId = plasticObj.imageId
        )

        val letter = Letter(
                color = letterObj.color,
                title = letterObj.title,
                message = letterObj.message,
                signature = letterObj.signature
        )

        val embossing = Embossing(
                plastic = plastic,
                letter = letter
        )

        return RequestReissue(
                idempotencyKey = idempotency,
                externalCode = cardEntity.externalCode!!,
                reason = "LOSS",
                holder = holder,
                gender = cardEntity.holder!!.gender!!,
                delivery = delivery,
                embossing= embossing
        )
    }
}