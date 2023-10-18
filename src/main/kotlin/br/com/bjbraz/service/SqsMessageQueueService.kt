package br.com.bjbraz.service

import br.com.bjbraz.dto.*
import br.com.bjbraz.dto.queue.*
import br.com.bjbraz.entity.boleto.BoletoEntity
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class SqsMessageQueueService(@Value("\${sqs.queue.account}") private val queueAccount: String,
                             @Value("\${sqs.queue.transfer.request}") private val queueTransferRequest: String,
                             @Value("\${sqs.queue.card.callback}") private val queueCallBack: String,
                             @Value("\${sqs.queue.card}") private val queueCard: String,
                             @Value("\${sqs.queue.card.reissue}") private val queueCardReissue: String,
                             @Value("\${sqs.queue.card.reissue.callback}") private val queueCallBackReissue: String,
                             @Value("\${sqs.queue.charge}") private val queueCardCharge: String,
                             @Value("\${sqs.queue.command-queue}") private val queueCommand: String,
                             @Value("\${sqs.queue.boleto.request}") private val queueBoletoPay: String,
                             @Value("\${sqs.queue.docted_request}") val queueDocTed: String,
                             @Value("\${sqs.queue.docted_send}") val queueDocTedHubRequest: String,
                             @Value("\${config.useJms}") val useJms: Boolean
) {

    private val logger = LoggerFactory.getLogger(SqsMessageQueueService::class.java)

    fun sendMessageAccountCreation(account: Account){
        logger.info("Puttin on queue "+account.idempotencyKey)
    }

    fun sendMessageAccountTransferRequest(message: AccountTransferBody){
    }

    fun sendMessageCardCreation(cpf: String, fok:String, orderNumber:String){
        var message = (QueueMessageCard(cpf = cpf, fok = fok, orderNumber = orderNumber))
        logger.info("Puttin on queue "+message.cpf)
    }

    fun sendCommandMessage(message: CommandMessage) {
        logger.info("Put in Command on queue "+message.type)
    }

    fun sendTestMessage(message: String) {
        logger.info("Put in Command on queue $message")
    }

    fun sendMessageCardCallBack(message: QueueMessageCardCallback) {
        logger.info("Put in on card callback queue "+message.callback!!.requestId)
    }

    fun sendMessageCardReissueCallBack(message: QueueMessageCardReissueCallback) {
        logger.info("Put in on reissue callback queue "+message.callback!!.requestId)
    }

    fun sendMessageCardReissue(message: Reissue) {
        logger.info("Puttin on reissue queue "+message.idempotencyKey)
    }

    fun sendMessageChargeCard(message : Charge) {
        logger.info("Puttin on charge queue "+message.transferCode)
    }

    fun sendMessageBoletoPay(message : BoletoEntity) {
        logger.info("Puttin on Boleto queue "+message.id)
    }

    fun sendMessageDocTed(message : DocTed) {
        logger.info("Puttin on DocTed queue "+message.externalId)
    }

    fun sendMessageDocTedCreation(message : DocTed) {
        logger.info("Puttin on DocTed queue "+message.externalId)
    }

}