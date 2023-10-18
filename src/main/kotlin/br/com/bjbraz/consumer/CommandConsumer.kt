package br.com.bjbraz.consumer

import br.com.bjbraz.entity.general.Command
import br.com.bjbraz.dto.queue.CommandMessage
import br.com.bjbraz.repository.AccountRepository
import br.com.bjbraz.repository.CardRepository
import br.com.bjbraz.repository.SQLAccountRepository
import br.com.bjbraz.repository.SQLCardRepository
import br.com.bjbraz.service.CardService
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload

@Configuration
class CommandConsumer(
    val cardRepository: CardRepository,
    val sqlCardRepository: SQLCardRepository,

    val accountRepository: AccountRepository,
    val sqlAccountRepository: SQLAccountRepository,

    val service: CardService
        ) {

    private val logger = LoggerFactory.getLogger(CommandConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.command-queue}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.command-queue}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?) {

        try {
            val message = ObjectMapper().readValue<CommandMessage>(messageStr, CommandMessage::class.java)
            logger.info("Queue One message received type : {}, identifier: {}, command: {} ", message.type, message.identifier, message.command)

            val met = when(message.type){
                Command.UNBLOCK_CARD.type -> service.unBlockCard(message.identifier!!)
                Command.BLOCK_CARD.type -> service.blockCard(message.identifier!!)
                Command.BLOCK_ACCOUNT.type -> service.blockAccount(message.identifier!!)
                Command.UNBLOCK_ACCOUNT.type -> service.unBlockAccount(message.identifier!!)
                else -> service.blockCard(message.identifier!!)
            }

        } catch (ex: Exception) {
            logger.error("Error receiving message $ex")
            throw ex
        }
    }
}