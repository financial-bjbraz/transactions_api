package br.com.bjbraz.consumer

import br.com.bjbraz.dto.Account
import br.com.bjbraz.entity.account.*
import br.com.bjbraz.dto.queue.QueueMessage
import br.com.bjbraz.repository.AccountRepository
import br.com.bjbraz.repository.SQLAccountRepository
import br.com.bjbraz.service.GenerateAccount
import br.com.bjbraz.service.SqsMessageQueueService
import br.com.bjbraz.service.transformDate
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.*


@Configuration
@Component
class AccountCreatedConsumer(
    val generateAccount: GenerateAccount,
    val dynamoRepository: AccountRepository,
    val sqlRepository: SQLAccountRepository,
    val queueService: SqsMessageQueueService,
    @Autowired val mail: JavaMailSender
) {

    private val logger = LoggerFactory.getLogger(AccountCreatedConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.account}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.account}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?)  {
        try {
            val message = ObjectMapper().readValue<QueueMessage>(messageStr, QueueMessage::class.java)
            logger.info("Queue One message received message : {}, messageId: {} ", message, messageId)
            receiveMessage(message!!, messageId)
        }catch(ex: Exception){
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

    fun receiveMessage(message: QueueMessage, messageId: String?) {
        val account:Account = message!!.message!!
        try {
            logger.info("################################################")

            val hubAccount = generateAccount.createNewAccount(account!!)

            account.financialOperationKey = hubAccount!!.financialOperationKey

//            dynamoRepository.save(account)

            val ownerAddress: AddressEntity = AddressEntity(idAddress = null, street = account.owner!!.address!!.street!!, number = account.owner.address!!.number!!, district = account.owner.address!!.number!!, city = account.owner.address!!.district!!, state = account.owner.address!!.state!!, zipCode = account!!.owner!!.address!!.zipcode!!)
            val responsibleAddress: AddressEntity = AddressEntity(idAddress = null, street = account.responsible!!.address!!.street!!, number = account.responsible.address!!.number!!, district = account.responsible.address!!.number!!, city = account.responsible.address!!.district!!, state = account.responsible.address!!.state!!, zipCode = account!!.responsible!!.address!!.zipcode!!)

            val ownerIdentifierDocument: IdentifierDocumentEntity = IdentifierDocumentEntity(idDocument = null, document = account.owner!!.identifierDocument!!.document!!, type = account.owner.identifierDocument!!.type)
            val responsibleIdentifierDocument: IdentifierDocumentEntity = IdentifierDocumentEntity(idDocument = null, document = account.responsible!!.identifierDocument!!.document!!, type = account.responsible!!.identifierDocument!!.type)

            val ownerDocuments:List<DocumentEntity> = account.owner.documents!!.map { it -> DocumentEntity(idDocument = null, document = it.document!!, type = it.type) }
            val responsibleDocuments:List<DocumentEntity> = account.responsible.documents!!.map { it -> DocumentEntity(idDocument = null, document = it.document!!, type = it.type) }

            var birthDateOwner = transformDate(account.responsible.birthDate)
            var birthDateResponsible = transformDate(account.responsible.birthDate)

            if(account.owner.cardName == null)
                account.owner.cardName = account.owner.name

            if(account.responsible.cardName == null)
                account.responsible.cardName = account.owner.name


            val responsible : PeopleEntity = PeopleEntity(
                    birthDate = birthDateResponsible,
                    name = account.responsible!!.name!!,
                    cardName = account.responsible!!.cardName,
                    type = account.responsible!!.type!!,
                    companyName = account.responsible!!.companyName!!,
                    email = account.responsible!!.email!!,
                    businessPhone = account.responsible!!.businessPhone!!,
                    homePhone = account.responsible!!.homePhone!!,
                    mobilePhone = account.responsible!!.mobilePhone!!,
                    address = responsibleAddress,
                    identifierDocument = responsibleIdentifierDocument,
                    documents = responsibleDocuments,
                    gender = account.responsible!!.gender
            )

            var owner : PeopleEntity = PeopleEntity(
                    birthDate = birthDateOwner,
                    name = account.owner!!.name!!,
                    cardName = account.owner!!.cardName,
                    type = account.owner!!.type!!,
                    companyName = account.owner!!.companyName!!,
                    email = account.owner!!.email!!,
                    homePhone = account.owner!!.homePhone!!,
                    businessPhone = account.owner!!.businessPhone!!,
                    mobilePhone = account.responsible!!.mobilePhone!!,
                    address = ownerAddress,
                    identifierDocument = ownerIdentifierDocument,
                    documents = ownerDocuments,
                    gender = account.owner!!.gender
            )

            var entity: AccountEntity? = sqlRepository.findByFinancialOperationKey(account!!.financialOperationKey!!)!!.orElse(
                    AccountEntity(id = null, owner = owner, responsible = responsible, accountNumber = account?.financialOperationKey?.toLongOrNull(), dt_ref = Date(), firebaseId = null, financialOperationKey = account.financialOperationKey, lastUpdated = Date(), documentPrincipal = responsible!!.identifierDocument!!.document!!)
            )

            entity!!.requestCard = account.requestCard
            entity!!.owner       = owner
            entity!!.responsible = responsible
            entity!!.lastUpdated = Date()

            entity.also {
                sqlRepository.save(it)
            }

            sendEmail(account!!.financialOperationKey!!)

            requestCard(entity)

            logger.info("################################################ $account")
            println("################################################## $account")
        }catch(ex:Exception){
            logger.error("Error creating Account and Card $ex")
            throw ex
        }
    }

    private fun requestCard(account: AccountEntity) {
        if(account.requestCard){
            logger.info("Requesting new Card for FinancialOperationKey " + account.financialOperationKey)
            queueService.sendMessageCardCreation(fok =  account.financialOperationKey!!, cpf = account!!.owner!!.identifierDocument!!.document!!, orderNumber = UUID.randomUUID().toString())
        }
    }

    fun sendEmail(fok:String) {
        val msg = SimpleMailMessage()
        msg.setFrom("contato@bjbraz.com.br")
        msg.setTo("alexjavabraz@gmail.com")
        msg.setSubject("Account Created $fok")
        msg.setText("New Account Created \n Financial Services Works")
        mail.send(msg)
    }

}