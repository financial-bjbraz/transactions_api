package br.com.bjbraz.consumer

import br.com.bjbraz.dto.Charge
import br.com.bjbraz.service.AccountServiceImpl
import br.com.bjbraz.service.SqsMessageQueueService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@EnableAsync
class CardChargeConsumer (val queueService: SqsMessageQueueService,
                          val service: AccountServiceImpl,
                          @Autowired val mail: JavaMailSender) {

    private val logger = LoggerFactory.getLogger(CardChargeConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.charge}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.charge}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?)  {
        try {
            val message = ObjectMapper().readValue<Charge>(messageStr, Charge::class.java)
            logger.info("Queue Charge message received message : {}, messageId: {} ", message, messageId)
            receiveMessage(message!!)
        }catch(ex: Exception){
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

    fun receiveMessage(message: Charge) {
        try {
            logger.info("##############Charging##############")
            service.chargeCard(charge = message)
            logger.info("##############Charging########## $message")
        }catch(ex:Exception){
            logger.error("Error creating Account and Card $ex")
            throw ex
        }
    }

}