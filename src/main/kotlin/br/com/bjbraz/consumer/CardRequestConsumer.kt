package br.com.bjbraz.consumer

import br.com.bjbraz.entity.account.AccountEntity
import br.com.bjbraz.entity.account.AddressEntity
import br.com.bjbraz.entity.card.*
import br.com.bjbraz.entity.general.ParameterEntity
import br.com.bjbraz.dto.queue.QueueMessageCard
import br.com.bjbraz.repository.AccountRepository
import br.com.bjbraz.repository.ParameterRepository
import br.com.bjbraz.repository.SQLAccountRepository
import br.com.bjbraz.repository.SQLCardRepository
import br.com.bjbraz.service.GenerateCard
import br.com.bjbraz.service.transformString
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import java.util.*

@Configuration
class CardRequestConsumer (
    val generateCard: GenerateCard,
    val dynamoRepository: AccountRepository,
    val sqlRepository: SQLAccountRepository,
    val cardRepository: SQLCardRepository,
    val paramRepository: ParameterRepository,
    @Autowired
    val mail: JavaMailSender
    ) {

    private val logger = LoggerFactory.getLogger(CardRequestConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.card}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.card}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?) {
        try {
            val message = ObjectMapper().readValue<QueueMessageCard>(messageStr, QueueMessageCard::class.java)
            val account: AccountEntity = sqlRepository.findByFinancialOperationKey(message!!.fok!!).orElse(AccountEntity())!!
            logger.info("Queue One message received accountFok : {}, cpf: {} ", account.financialOperationKey, account.owner?.identifierDocument?.document)

            if(message.cpf.equals(account.owner?.identifierDocument?.document)){
                receiveMessageAndRequestHubCard(account, message.orderNumber)
            }

        } catch (ex: Exception) {
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

    fun receiveMessageAndRequestHubCard(account: AccountEntity, orderNumber:String) {
        convertAndSend(persistCard(account, orderNumber))
    }

    private fun convertAndSend(card: CardEntity): CardEntity {
        card.status = CardStatus.REQUESTING
        card.lastUpdated = Date()
        generateCard.requestHubCard(card)
        card.status = CardStatus.REQUESTED
        return card.also { cardRepository.save(it) }
    }

    private fun persistCard(account: AccountEntity, orderNumber: String): CardEntity {
        var cardObj =  account.cards!!.stream().map { it }.
            filter{ it -> it.orderNumber.equals(orderNumber) }
                .findFirst()

        if(cardObj != null && cardObj.isPresent){
            return cardObj.get()
        }

        return generateNewCard(account, orderNumber)
    }

    private fun generateNewCard(account: AccountEntity, orderNumber: String): CardEntity {
        val owner = account!!.owner!!
        val externalCode:String?= UUID.randomUUID().toString()
        val financialOperationKey:String=account.financialOperationKey!!
        val virtualCard:Boolean=false
        val name:String=owner.cardName!!
        val document:String=owner.identifierDocument!!.document!!
        val email=owner.email
        val mobilePhone = owner.mobilePhone
        val homePhone = owner.homePhone
        var birthDate = transformString(owner.birthDate!!)
        val gender = owner.gender
        val street = owner.address!!.street
        val number = owner.address!!.number
        val district = owner.address!!.district
        val city = owner.address!!.city
        val state = owner.address!!.state
        val zipCode = owner.address!!.zipCode

        val message:String= paramRepository.findByName("message_card").orElse(ParameterEntity(value = "Mensagem"))!!.value!!
        val image:String  = paramRepository.findByName("image_card").orElse(ParameterEntity(value = "/image/9cbc8fe6d2684f069c02d01c1ee5694a/3998d"))!!.value!!
        val imageId:Int   = Integer.parseInt(paramRepository.findByName("imageid_card").orElse(ParameterEntity(value = "1"))!!.value!!)

        val color:String=paramRepository.findByName("letter_color").orElse(ParameterEntity(value = "blue"))!!.value!!
        val title:String=paramRepository.findByName("letter_title").orElse(ParameterEntity(value = "Cliente "))!!.value!!
        val letterMessage:String=paramRepository.findByName("letter_message").orElse(ParameterEntity(value = "Mensagem"))!!.value!!
        val signature:String=paramRepository.findByName("letter_signature").orElse(ParameterEntity(value = "signature"))!!.value!!

        val plastic = PlasticEntity(holder = name, message = message, image = image, imageId = imageId)

        val letter = LetterEntity(color = color, title = title, message = letterMessage, signature = signature)

        val address = AddressEntity(
                street = street,
                number = number,
                district = district,
                city = city,
                state = state,
                zipCode = zipCode
        )

        val recipient =  RecipientEntity(
                name = name,
                address = address
        )

        val holder: HolderEntity= HolderEntity(
                name = name,
                email = email,
                document = document,
                mobilePhone = mobilePhone,
                homePhone = homePhone,
                birthDate = birthDate,
                gender = gender,
                address = address
        )

        val delivery: DeliveryEntity = DeliveryEntity(recipient = recipient)
        val embossing: EmbossingEntity = EmbossingEntity(plastic = plastic, letter = letter)

        var cardEntity: CardEntity = CardEntity (
                orderNumber = orderNumber,
                externalCode = externalCode,
                financialOperationKey = financialOperationKey,
                virtualCard = virtualCard,
                holder = holder,
                delivery = delivery,
                embossing = embossing,
                account = account,
                lastUpdated = Date(),
                status = CardStatus.REQUESTING
        )

        account.cards!!.add(cardEntity)
        //sqlRepository.save(account).cards!!.stream().filter { it.orderNumber == orderNumber }.findFirst().get()

        return cardEntity
    }
}