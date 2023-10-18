package br.com.bjbraz.service

import br.com.bjbraz.dto.*
import br.com.bjbraz.entity.account.AccountEntity
import br.com.bjbraz.entity.boleto.BoletoEntity
import br.com.bjbraz.entity.card.BoletoStatus
import br.com.bjbraz.entity.docted.BankAccountTypeEntity
import br.com.bjbraz.entity.docted.BankEntity
import br.com.bjbraz.entity.docted.BeneficiaryEntity
import br.com.bjbraz.entity.docted.DocTedEntity
import br.com.bjbraz.repository.*
import kotlinx.coroutines.reactive.collect
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import reactor.core.publisher.Mono
import java.util.*

@Service
class FinancialServiceImpl (
    val boletoRepository: BoletoRepository,
    @Value("\${ppi.boleto.url}") val boletoPaymentUrl: String,
    @Value("\${ppi.ted.url}") val docTedUrl: String,
    val requestRepository: RequestReponseRepository,
    val accountRepository: SQLAccountRepository,
    val bankRepository: BankRepository,
    val beneficiaryRepository: BeneficiaryRepository,
    val bankAccountTypeRepository: BankTypeRepository,
    val docTedRepository: DocTedRepository,
    val authenticateService: AuthImpl,
    val webClient: WebClient,
    private val sqsMessage: SqsMessageQueueService
                           ) : FinancialService {
    private val logger = LoggerFactory.getLogger(FinancialServiceImpl::class.java)

    override fun boletoPayment(boleto: Boleto): Mono<String> {
        logger.info("New Boleto Payment Requested")

        if(boleto.cpf.isNullOrEmpty())
            throw NullPointerException("CPF Not Found")

        var entity:BoletoEntity = BoletoEntity(
                id = null,
                account = null,
                dt_ref = Date(),
                status = BoletoStatus.CREATED,
                externalId = UUID.randomUUID().toString(),
                amount = boleto.amount,
                summary = boleto.summary,
                digitableLine = boleto.digitableLine,
                dueDate = transformDate(boleto.dueDate),
                scheduleDate = transformDate(boleto.scheduleDate),
                document = boleto.cpf
        )

        entity = persistEntities(boletoEntity = entity, cpf = boleto.cpf!!)
        sendBoletoMessage(entity)
        return Mono.justOrEmpty(entity.externalId)
    }

    override fun docted(docTed: DocTed): Mono<String> {

        if(docTed.amount == null)
            throw NullPointerException("Amount Invalid")

        if(docTed.beneficiary == null)
            throw NullPointerException("Beneficiary Invalid")

        if(docTed.cpf == null)
            throw NullPointerException("CPF Invalid")

        if(docTed.type == null)
            throw NullPointerException("DOC TED Type Invalid")

        docTed.externalId = UUID.randomUUID().toString()
        sendDocTedEntity(docTed)
        return Mono.justOrEmpty(docTed.externalId)
    }

    override fun persistDocTed(docTed: DocTed) : DocTedEntity{
        var optAccount: Optional<AccountEntity?> = accountRepository.findByResponsibleIdentifierDocumentDocument(docTed.cpf!!)

        if(!optAccount.isPresent)
            throw NullPointerException("Account Not Found")

        var bankTypeEntity: Mono<BankAccountTypeEntity> = bankAccountTypeRepository.findById(docTed.beneficiary!!)

//        if(!bankTypeEntity)
//            throw NullPointerException("Bank Type Not Found")

        var beneficiaryOpt: Mono<BeneficiaryEntity> = beneficiaryRepository.findById(docTed.beneficiary!!.toString())



        val beneficiary = beneficiaryOpt.block()
        var bank:BankEntity = bankRepository.findById(beneficiary!!.bank!!.number!!.toString()).block()!!

//        if(!bankTypeEntity.isPresent)
//            throw NullPointerException("Bank Type Not Found")

        var docTedEntity: DocTedEntity = DocTedEntity(
                externalId = docTed.externalId,
                amount = docTed.amount,
                type = docTed.type,
                summary = docTed.summary,
                scheduleDate = transformDate(docTed.scheduleDate),
                requestDate = Date(),
                bankAccountTypeEntity = bankTypeEntity.block(),
                bank = bank,
                beneficiaryEntity = beneficiary,
                status = "CREATED",
                document = docTed.cpf!!
        )

        docTedEntity.also { docTedRepository.save(it) }

        return docTedEntity
    }

    private fun sendDocTedEntity(docTed: DocTed) {
        logger.info("Sending DocTed Message")
        sqsMessage.sendMessageDocTed(docTed)
    }

    private fun persistEntities(boletoEntity: BoletoEntity, cpf:String):BoletoEntity{
        boletoEntity.account = accountRepository.findByResponsibleIdentifierDocumentDocument(cpf).get()
        return boletoEntity.also { boletoRepository.save(it) }
    }

    private fun sendBoletoMessage(boleto: BoletoEntity) {
        logger.info("Sending Boleto Message")
        sqsMessage.sendMessageBoletoPay(boleto)
    }

    override fun findByExternalId(externalId: String): Mono<BoletoEntity> {
        logger.info("Searching Boleto by $externalId")
        val optBoleto : Optional<BoletoEntity> = boletoRepository.findByExternalId(externalId)
        return Mono.justOrEmpty(optBoleto)
    }

    override suspend fun requestDocTed(docTed: DocTedRequest) : DocTedResponse{
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")
        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"

        try{

            myJson = JSONObject(docTed).toString()

            val resp : DocTedResponse = webClient.post()
                    .uri(docTedUrl, docTed.financialOperationKey)
                    .body(BodyInserters.fromValue(docTed))
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .retrieve()
                    .awaitBody<DocTedResponse>()

            statusCode = 200
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()
            saveRequest(docTed!!.externalId!! , myJson, token, statusCode, myJsonResp, requestRepository, boletoPaymentUrl)

            return resp

        }catch(ex:Exception){
            logger.error("Error calling $boletoPaymentUrl , exception received $ex, payload sended $myJson")
            saveRequest(docTed!!.externalId!! , myJson, token, 422, (myJsonResp + ex.message),requestRepository, boletoPaymentUrl)
            throw ex
        }

    }

    override suspend fun requestBoletoPayment(boletoEntity: BoletoEntity) : Boleto{
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")
        var statusCode = 202
        var myJson: String = ""
        var myJsonResp: String = "resp"
        var boleto:BoletoRequest? = null
        try{

            boleto = convertBoleto(boletoEntity)
            myJson = JSONObject(boleto).toString()

            val resp : Boleto = webClient.post()
                    .uri(boletoPaymentUrl, boleto.financialOperationKey)
                    .body(BodyInserters.fromValue(boleto))
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .retrieve()
                    .awaitBody<Boleto>()

            statusCode = 200
            val jsonObjectResp = JSONObject(resp)
            myJsonResp = jsonObjectResp.toString()
            saveRequest(boleto!!.externalId!! , myJson, token, statusCode, myJsonResp, requestRepository, boletoPaymentUrl)

            return resp

        }catch(ex:Exception){
            logger.error("Error calling $boletoPaymentUrl , exception received $ex, payload sended $myJson")
            saveRequest(boleto!!.externalId!! , myJson, token, 422, (myJsonResp + ex.message),requestRepository, boletoPaymentUrl)
            throw ex
        }
    }

    private fun convertBoleto(boletoEntity: BoletoEntity): BoletoRequest {

        val accountEntityOpt = accountRepository.findByResponsibleIdentifierDocumentDocument(boletoEntity.document!!)
        val accountEntity = accountEntityOpt.get()

        return BoletoRequest(
                digitableLine = boletoEntity.digitableLine!!,
                amount = boletoEntity.amount!!,
                summary = boletoEntity.summary,
                financialOperationKey = accountEntity.financialOperationKey,
                externalId = boletoEntity.externalId,
                scheduleDate = transformString(boletoEntity.scheduleDate!!),
                infoBoleto = BoletoRequestInfoBoleto(digitableLine = boletoEntity.digitableLine!!, dueDate = transformString(boletoEntity.dueDate!!)),
                cpf = ""
        )
    }

}