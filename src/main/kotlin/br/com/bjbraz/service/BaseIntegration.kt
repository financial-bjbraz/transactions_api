package br.com.bjbraz.service

import br.com.bjbraz.repository.RequestReponseRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.reactive.function.client.WebClient

class BaseIntegration(@Value("\${ppi.account.url}") val ppiUrl: String,
                      val requestRepository: RequestReponseRepository,
                      val authenticateService: AuthImpl,
                      val webClient: WebClient) {

    private val logger = LoggerFactory.getLogger(BaseIntegration::class.java)

    fun getToken():String{
        logger.info("Getting Token ")
        var token: String = "bearer " + authenticateService.authenticate()
        logger.info("Using this token=$token")
        return token
    }
}