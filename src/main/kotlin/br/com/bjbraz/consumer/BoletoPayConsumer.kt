package br.com.bjbraz.consumer

import br.com.bjbraz.dto.Boleto
import br.com.bjbraz.entity.boleto.BoletoEntity
import br.com.bjbraz.entity.card.BoletoStatus
import br.com.bjbraz.repository.BoletoRepository
import br.com.bjbraz.service.FinancialService
import br.com.bjbraz.service.GenerateAccount
import br.com.bjbraz.service.SqsMessageQueueService
import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.messaging.handler.annotation.Headers
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.scheduling.annotation.EnableAsync
import java.lang.NullPointerException


@Configuration
@EnableAsync
class BoletoPayConsumer (
    val generateAccount: GenerateAccount,
    val service: FinancialService,
    val repository: BoletoRepository,
    val queueService: SqsMessageQueueService,
    @Autowired val mail: JavaMailSender
) {

    private val logger = LoggerFactory.getLogger(BoletoPayConsumer::class.java)

//    @SqsListener(value = ["\${sqs.queue.boleto.request}"], deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
//    @JmsListener(destination = "\${sqs.queue.boleto.request}", containerFactory = "myFactory")
    fun listen(@Headers messageAttributes: Map<String, Object>, @Payload messageStr: String, messageId: String?)  {
        try {
            val message = ObjectMapper().readValue<Boleto>(messageStr, Boleto::class.java)
            logger.info("Queue One message received message : {}, messageId: {} ", message, messageId)
            receiveMessage(message!!)
        }catch(ex: Exception){
            logger.error("Error receiving message $ex")
            throw ex
        }
    }

    private fun receiveMessage(boletoDTO: Boleto) {
       logger.info("Received Boleto Message ${boletoDTO.idBoleto}")
        val optBoleto = repository.findById(boletoDTO.idBoleto!!)

//        if(!optBoleto.isPresent)
//            throw NullPointerException("Boleto Not Found")

        val boleto = optBoleto.block()

        try {
            requestBoletoPayment(boleto!!)

            boleto.status = BoletoStatus.REQUESTED
            repository.save(boleto)
        }catch(ex:Exception){
            logger.error("Error requesting Boleto Payment $ex")
        }
    }

    private fun requestBoletoPayment(boleto: BoletoEntity) {
        runBlocking {
            val boletoResponse = service.requestBoletoPayment(boleto)
        }
    }


}