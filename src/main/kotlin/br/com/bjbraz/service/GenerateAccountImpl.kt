package br.com.bjbraz.service

import br.com.bjbraz.dto.Account
import br.com.bjbraz.dto.GeneratedAccountInfo
import br.com.bjbraz.dto.MainAccountInfo
import br.com.bjbraz.dto.RequestReponse
import br.com.bjbraz.repository.RequestReponseRepository
import org.json.JSONObject
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import java.text.SimpleDateFormat
import java.util.*

@Service
class GenerateAccountImpl(@Value("\${ppi.account.url}") val ppiUrl: String,
                          val requestRepository: RequestReponseRepository,
                          val authenticateService: AuthImpl,
                          val webClient: WebClient) : GenerateAccount {

    private val logger = LoggerFactory.getLogger(GenerateAccountImpl::class.java)

    override fun createNewAccount(account: Account): GeneratedAccountInfo {
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")

        var statusCode = 200
        var myJson: String = ""
        var myJsonResp: String = ""
        try{

            account.owner?.birthDate = "1982-05-01";
            account.responsible?.birthDate = "1982-05-01"
            account.responsible?.address?.zipcode = "04428010"
            account.owner?.address?.zipcode = "04428010"

            val jsonObject = JSONObject(account)
            myJson = jsonObject.toString()
            logger.info(myJson)

            var accountHub: GeneratedAccountInfo = webClient.post()
                    .uri(ppiUrl)
                    .body(BodyInserters.fromValue(account))
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", token)
                    .retrieve()
                    .bodyToMono(GeneratedAccountInfo::class.java).block()!!

            val jsonObjectResp = JSONObject(accountHub)
            myJsonResp = jsonObjectResp.toString()
            saveRequest(account.idempotencyKey!! , myJson, token, statusCode, myJsonResp, requestRepository, ppiUrl)
            return accountHub

        }catch(ex:Exception){
            myJsonResp += ex.message
            statusCode = 422
            saveRequest(account.idempotencyKey!! , myJson, token, statusCode, myJsonResp + ex.message, requestRepository, ppiUrl)
            throw ex
        }
    }

//    fun saveRequest(id:String, request: String, token:String, statusCode:Int, response:String){
//        val headerValues = "Authorization $token"
//        val dataHoje : String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
//        requestRepository.save(RequestReponse(idempotency_key= id, request = request, response = response, httpCode = statusCode, headers =  headerValues ,method =  null, uri=ppiUrl,date =  dataHoje ))
//    }

//            .let {

//               it.bodyToMono(MainAccountInfo::class.java)
//                        .flatMap { item -> saveInfo(item) }
//
//                if (it.statusCode() == HttpStatus.NOT_FOUND) {
//                    throw CardNotFoundException("Not found main account info $cpf")
//                }
//                if (it.statusCode() >= HttpStatus.INTERNAL_SERVER_ERROR) {
//                    throw RuntimeException("Error on get main account info ")
//                }
//                it.bodyToMono()<MainAccountInfo>()
//            }

    private fun saveInfo(item: MainAccountInfo?) {

    }

}
