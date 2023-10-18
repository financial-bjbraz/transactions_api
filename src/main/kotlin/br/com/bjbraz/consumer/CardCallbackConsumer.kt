package br.com.bjbraz.consumer

import br.com.bjbraz.entity.card.CardEntity
import br.com.bjbraz.entity.card.CardStatus
import br.com.bjbraz.dto.queue.QueueMessageCardCallback
import br.com.bjbraz.repository.AccountRepository
import br.com.bjbraz.repository.CardRepository
import br.com.bjbraz.repository.SQLCardRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import java.lang.NullPointerException
import java.util.*

@Configuration
class CardCallbackConsumer (
    val dynamoRepository: AccountRepository,
    val sqlRepository: SQLCardRepository,
    val cardRepository: CardRepository,
    @Autowired
        val mail: JavaMailSender
){

    private val logger = LoggerFactory.getLogger(CardCallbackConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.card.callback}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.card.callback}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?) {
        try {
            val message = ObjectMapper().readValue<QueueMessageCardCallback>(messageStr, QueueMessageCardCallback::class.java)
            logger.info("Queue One message received fok : {}, externalCode: {} ", message.callback?.financialOperationKey, message.callback?.card?.externalCode)

            if(message.callback!!.success){
                persistSuccess(message)
            }else{
                persistError(message)
            }

        } catch (ex: Exception) {
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

    private fun persistError(message: QueueMessageCardCallback) {
        var card: CardEntity = sqlRepository.findByExternalCode(message!!.callback!!.card!!.externalCode!!).orElseThrow{NullPointerException("Card not found by ExternalCode")}
        card.status = CardStatus.CREATION_ERROR
        card.lastUpdated = Date()
        sqlRepository.save(card)
    }

    private fun persistSuccess(message: QueueMessageCardCallback) {
        var card: CardEntity = sqlRepository.findByExternalCode(message!!.callback!!.card!!.externalCode!!).orElseThrow{NullPointerException("Card not found by ExternalCode")}
        card.status = CardStatus.CREATED_BLOCKED
        card.externalCreatedAt = message.callback?.card?.createdAt
        card.cardId = (message.callback?.card?.cardId)?.toLongOrNull()
        card.truncatedNumber = message.callback?.card?.truncatedNumber
        card.requestId = message.callback?.requestId
        card.lastUpdated = Date()
//        cardRepository.save(sqlRepository.save(card))

        sendEmail(message!!.callback!!.card!!.externalCode!!)
    }

    fun sendEmail(externalCode:String) {
        val msg = SimpleMailMessage()
        msg.setFrom("contato@bjbraz.com.br")
        msg.setTo("alexjavabraz@gmail.com")
        msg.setSubject("Card Issued Sucessfull")
        msg.setText("Hi new Card was created sucessfull \n Financial Services Works fine")
        mail.send(msg)
    }

}