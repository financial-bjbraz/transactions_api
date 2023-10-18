package br.com.bjbraz.consumer

import br.com.bjbraz.dto.DocTed
import br.com.bjbraz.dto.DocTedBankBeneficiary
import br.com.bjbraz.dto.DocTedRequest
import br.com.bjbraz.entity.docted.DocTedEntity
import br.com.bjbraz.repository.DocTedRepository
import br.com.bjbraz.repository.SQLAccountRepository
import br.com.bjbraz.service.FinancialService
import br.com.bjbraz.service.SqsMessageQueueService
import br.com.bjbraz.service.cleanDocument
import br.com.bjbraz.service.transformString
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.scheduling.annotation.EnableAsync

@Configuration
@EnableAsync
class DocTedConsumer(
    val queueService: SqsMessageQueueService,
    val service: FinancialService,
    val docTedRepository: DocTedRepository,
    val accountRepository: SQLAccountRepository,
    private val sqsMessage: SqsMessageQueueService,
    @Autowired val mail: JavaMailSender
    ) {
    private val logger = LoggerFactory.getLogger(DocTedConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.docted_request}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.docted_request}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?)  {
        try {
            val message = ObjectMapper().readValue<DocTed>(messageStr, DocTed::class.java)
            logger.info("Queue One message received message : {}, messageId: {} ", message, messageId)
            var docted = receiveMessage(message!!)
            sqsMessage.sendMessageDocTedCreation(docted)
        }catch(ex: Exception){
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

//    @SqsListener(value = ["\${sqs.queue.docted_send}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.docted_send}", containerFactory = "myFactory")
    fun listenSend(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?)  {
        try {
            val message = ObjectMapper().readValue<DocTed>(messageStr, DocTed::class.java)
            logger.info("Queue One message received message : {}, messageId: {} ", message, messageId)
            sendHubCreationDocTed(message)

        }catch(ex: Exception){
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

    private fun sendHubCreationDocTed(docTed: DocTed) {
        var doctedEntity: DocTedEntity? = null
        try{
            logger.info("Sending Message DocTed External Creation Message ${docTed.externalId}")

            var optDocTed = docTedRepository.findByExternalId(docTed.externalId!!)

            if(!optDocTed.isPresent)
                throw NullPointerException("Doc Ted not found")

            doctedEntity = optDocTed.get()

            var accountOpt = accountRepository.findByResponsibleIdentifierDocumentDocument(doctedEntity.document!!)

            var financialOperationKey:String?=accountOpt.get().financialOperationKey
            var externalId:String?= doctedEntity.externalId
            var amount:Double?=doctedEntity.amount
            var type:String?=doctedEntity.type.toString()
            var summary:String?=doctedEntity.summary
            var scheduleDate:String?= transformString(doctedEntity.scheduleDate!!)

            var account:String? = doctedEntity.beneficiaryEntity!!.account
            var accountDigit:String? = doctedEntity.beneficiaryEntity!!.accountDigit
            var number:String? = doctedEntity.bank!!.number!!.toString()
            var beneficiaryName:String? = doctedEntity.beneficiaryEntity!!.beneficiaryName
            var beneficiaryDocument:String? = cleanDocument(doctedEntity.beneficiaryEntity!!.beneficiaryDocument!!)
            var bankAccountType:String? = doctedEntity.bankAccountTypeEntity!!.name
            var name:String = doctedEntity.bank!!.name!!
            var branch = doctedEntity.beneficiaryEntity!!.branch

            var bankBeneficiary: DocTedBankBeneficiary? = DocTedBankBeneficiary(
                    name = name,
                    branch = branch,
                    account = account,
                    accountDigit = accountDigit,
                    number = number,
                    beneficiaryName = beneficiaryName,
                    beneficiaryDocument = beneficiaryDocument,
                    bankAccountType = bankAccountType
            )

            var request = DocTedRequest(
                    financialOperationKey = financialOperationKey,
                    externalId = externalId,
                    amount = amount,
                    type = type,
                    summary = summary,
                    scheduleDate = scheduleDate,
                    bank = bankBeneficiary
            )

            runBlocking {
                service.requestDocTed(request)
            }

            doctedEntity!!.status = "Issued"
            docTedRepository.save(doctedEntity)

        }catch(ex:Exception){
            logger.error("Error requesting DocTed Payment $ex")
            doctedEntity!!.status = "Error Registering"
            docTedRepository.save(doctedEntity)
        }
    }

    private fun receiveMessage(docTed: DocTed) : DocTed {
        try{
            logger.info("Received DocTed Message ${docTed.externalId}")
            service.persistDocTed(docTed)
            return docTed
        }catch(ex:Exception){
            logger.error("Error requesting DocTed Payment $ex")
            throw ex
        }
    }

}