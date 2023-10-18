package br.com.bjbraz.consumer

import br.com.bjbraz.service.SqsMessageQueueService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.scheduling.annotation.EnableAsync


@Configuration
@EnableAsync
class TestConsumer (val queueService: SqsMessageQueueService) {

    private val logger = LoggerFactory.getLogger(TestConsumer::class.java)

    //    @SqsListener(value = ["\${sqs.queue.card.callback}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    // fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?) {


//    @JmsListener(destination = "teste", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?) {
        try {
            logger.info("Message received {} ", messageStr)
            logger.error("Message received {} ", messageStr)
            println("Message received {} $messageStr")
        } catch (ex: Exception) {
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

}