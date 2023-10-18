package br.com.bjbraz.service

import br.com.bjbraz.dto.Authenticated
import br.com.bjbraz.entity.auth.AuthEntity
import br.com.bjbraz.repository.SQLAuthRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import java.text.SimpleDateFormat
import java.util.*


@Service("authImpl")
class AuthImpl(@Value("\${ppi.oauth.base.url}") val authUrl: String,
               @Autowired private val repository: SQLAuthRepository,
               private val webClient: WebClient)  {

    private val logger = LoggerFactory.getLogger(AuthImpl::class.java)

    private val template = RestTemplate()

    fun authenticate() : String? {
        return searchToken()
    }

    private fun searchToken() = findTodayToken()

    fun findTodayToken() : String?{
        val data = SimpleDateFormat("dd/MM/yyyy").format(Date())
        logger.info("Finding Token By Date $data")
        var auth = repository.findByDtRef(data).get()

        var token = auth.token

        if(token == null){
            logger.info("Generating new Token")
            val newToken = getNewToken()?.let {
                saveInfo(it)
                token = it.access_token
            }
        }

        logger.info("New Token Obtained $token")

        return token
    }

    private fun saveInfo(item: Authenticated?) {
        logger.info("Saving New Token $item")
        val auth : AuthEntity = convertEntity(item!!)
        repository.save(auth)
    }

    private fun getNewToken() : Authenticated {
        var authenticated : Authenticated = Authenticated("","","","","")
        try {
            var body : MultiValueMap<String, String>  = LinkedMultiValueMap<String, String>();
            body.add("grant_type", "client_credentials");

            val requestHeaders = HttpHeaders()
            requestHeaders.setBasicAuth("88insurtech", "88insurtech")

            var httpEntity : HttpEntity<String> = HttpEntity<String>(null, requestHeaders)

            authenticated = template.exchange(authUrl+"?grant_type=client_credentials", HttpMethod.POST, httpEntity, Authenticated::class.java, body).body!!
        }catch(ex:Exception){
            logger.error("Error getting Token $ex")
        }

        return authenticated
    }

}
