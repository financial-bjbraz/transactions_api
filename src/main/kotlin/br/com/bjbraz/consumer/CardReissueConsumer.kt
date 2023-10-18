package br.com.bjbraz.consumer

import br.com.bjbraz.dto.Reissue
import br.com.bjbraz.entity.card.CardEntity
import br.com.bjbraz.entity.card.CardStatus
import br.com.bjbraz.repository.CardRepository
import br.com.bjbraz.repository.ParameterRepository
import br.com.bjbraz.repository.SQLCardRepository
import br.com.bjbraz.service.ReissueCard
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import java.util.*

@Configuration
class CardReissueConsumer (
    val cardRepository: CardRepository,
    val sqlCardRepository: SQLCardRepository,
    val paramRepository: ParameterRepository,
    val reisseCardService: ReissueCard
){

    private val logger = LoggerFactory.getLogger(CardReissueConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.card.reissue}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.card.reissue}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?) {
        try {
            val message = ObjectMapper().readValue<Reissue>(messageStr, Reissue::class.java)
            val card: CardEntity = sqlCardRepository.findByExternalCode(message!!.externalCode!!).orElse(CardEntity())!!
            logger.info("Queue One message received external Code : {}, cpf: {} ", card.externalCode, card.holder?.document)

            if(card != null){
                receiveMessageAndRequestHubCard(card, message.idempotencyKey!!)
            }

        } catch (ex: Exception) {
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

    fun receiveMessageAndRequestHubCard(card: CardEntity, idempotency:String) {
        card.status = CardStatus.REISSUE_REQUESTING
        card.lastUpdated = Date()
        sqlCardRepository.save(card)
        cardRepository.save(card)
        reisseCardService.requestReissueHubCard(card, idempotency)
        card.status = CardStatus.REISSUE_REQUESTED
        card.lastUpdated = Date()
        cardRepository.save(card)
        sqlCardRepository.save(card)
    }

}