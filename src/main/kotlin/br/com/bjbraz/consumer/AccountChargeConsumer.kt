package br.com.bjbraz.consumer

import br.com.bjbraz.dto.AccountTransferBody
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
class AccountChargeConsumer (val queueService: SqsMessageQueueService,
                          val service: AccountServiceImpl,
                          @Autowired val mail: JavaMailSender) {

    private val logger = LoggerFactory.getLogger(CardCallbackConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.card.callback}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.card.callback}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?) {
        try {
            val message = ObjectMapper().readValue<AccountTransferBody>(messageStr, AccountTransferBody::class.java)
            logger.info("Callback received {} ", message.cpf)
            service.requestAccountTransfer(message)
        } catch (ex: Exception) {
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

}
