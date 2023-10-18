package br.com.bjbraz.consumer

import br.com.bjbraz.dto.CardReissueCallbackItem
import br.com.bjbraz.entity.card.CardEntity
import br.com.bjbraz.entity.card.CardStatus
import br.com.bjbraz.dto.queue.QueueMessageCardReissueCallback
import br.com.bjbraz.repository.CardRepository
import br.com.bjbraz.repository.SQLCardRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import java.util.*

@Configuration
class CardReissueCallbackConsumer(
        val cardRepository: CardRepository,
        val sqlCardRepository: SQLCardRepository
) {
    private val logger = LoggerFactory.getLogger(CardReissueCallbackConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.card.reissue.callback}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.card.reissue.callback}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?) {
        try {
            val message = ObjectMapper().readValue<QueueMessageCardReissueCallback>(messageStr, QueueMessageCardReissueCallback::class.java)
            val card: CardEntity = sqlCardRepository.findByExternalCode(message!!.callback!!.originalCard!!.externalCode!!).orElse(CardEntity())!!
            logger.info("Queue One message received external Code : {}, cpf: {} ", card.externalCode, card.holder?.document)

            if(card != null){
                receiveMessageAndRequestHubCard(card, message!!.callback!!.reissuedCard!!)
            }

        } catch (ex: Exception) {
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

    fun receiveMessageAndRequestHubCard(card: CardEntity, reissuedCard: CardReissueCallbackItem) {
        card.status = CardStatus.REISSUED
        card.lastUpdated = Date()
        card.truncatedNumber = reissuedCard.truncatedNumber
        card.cardId = reissuedCard.cardId
        card.requestId = (reissuedCard.orderId).toString()

        sqlCardRepository.save(card)
        cardRepository.save(card)

    }

}