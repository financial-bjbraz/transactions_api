package br.com.bjbraz.service

import br.com.bjbraz.auth.SimpleUser
import br.com.bjbraz.dto.*
import br.com.bjbraz.entity.auth.AuthEntity
import br.com.bjbraz.entity.card.CardEntity
import br.com.bjbraz.exception.InvalidCardIdException
import br.com.bjbraz.repository.RequestReponseRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import java.text.SimpleDateFormat
import java.util.*

fun cleanDocument(cpf: String): String {
    return cpf.trim().replace(".", "").replace("-", "").replace("/", "")
}

fun transformString(date: Date): String {
    var dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.format(date)
}

fun transformDate(date: String?): Date {
    var dateFormat = SimpleDateFormat("yyyy-MM-dd")
    return dateFormat.parse(date)
}

fun getResponse(resp: ClientResponse): String {
    try{
        return resp.bodyToMono(String::class.java).block()!!
    }catch (ex:Exception){
        return "NOT INFORMED"
    }
}

fun convertEntity(auth : Authenticated): AuthEntity{
    val retorno : AuthEntity = AuthEntity()
    retorno.dtRef = SimpleDateFormat("dd/MM/yyyy").format(Date())
    retorno.expiresIn = auth.expires_in
    retorno.jti = auth.jti
    retorno.scope = auth.scope
    retorno.token = auth.access_token
    retorno.tokenType = auth.token_type

    return retorno;
}

fun convertEntity(cardEntity: CardEntity): Card {

    val holderObj = cardEntity.holder!!
    val addressObj   = cardEntity.holder.address!!
    val deliveryObj  = cardEntity.delivery!!
    val recipientObj = deliveryObj.recipient!!
    val addressObj2   = recipientObj.address!!
    val plasticObj = cardEntity.embossing!!.plastic!!
    val letterObj  = cardEntity.embossing!!.letter!!


    val holderAddress = Address(
            street = addressObj.street,
            number = addressObj.number,
            district = addressObj.district,
            city = addressObj.city,
            state = addressObj.state,
            zipcode = addressObj.zipCode
    )

    val recipientAddress = Address(
            street = addressObj2.street,
            number = addressObj2.number,
            district = addressObj2.district,
            city = addressObj2.city,
            state = addressObj2.state,
            zipcode = addressObj2.zipCode
    )

    val holder=  Holder(
            name = holderObj.name,
            document = holderObj.document,
            email = holderObj.email,
            mobilePhone = holderObj.mobilePhone,
            homePhone = holderObj.homePhone,
            birthDate = holderObj.birthDate,
            gender = holderObj.gender,
            address = holderAddress
    )

    val recipient = Recipient(
            name = recipientObj.name,
            address = recipientAddress
    )

    val delivery = Delivery(
            recipient = recipient
    )

    val plastic = Plastic(
            holder = plasticObj.holder,
            message = plasticObj.message,
            image = plasticObj.image,
            imageId = plasticObj.imageId
    )

    val letter = Letter(
            color = letterObj.color,
            title = letterObj.title,
            message = letterObj.message,
            signature = letterObj.signature
    )

    val embossing = Embossing(
            plastic = plastic,
            letter = letter
    )

    val card: Card = Card(
            externalCode = cardEntity.externalCode,
            financialOperationKey = cardEntity.financialOperationKey,
            virtualCard = cardEntity.virtualCard,
            holder = holder,
            delivery = delivery,
            embossing = embossing
    )

    return card
}


fun saveRequest(id:String, request: String, token:String, statusCode:Int, response:String, requestRepository: RequestReponseRepository, url:String){
    try {
        val headerValues = "Authorization $token"
        val dataHoje: String = SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Date())
        requestRepository.save(RequestReponse(id = id, request = request, response = response, httpCode = statusCode, headers = headerValues, method = null, uri = url, date = dataHoje))
    }catch(ex:Exception){

    }
}

fun getString(name:String, request: ServerRequest): String {
    return request.pathVariable(name)
}

fun getLong(name:String, request: ServerRequest): String {
    val cardId = request.pathVariable(name)
    validateLong(cardId)
    return cardId
}

fun getDouble(name:String, request: ServerRequest): Double {
    val cardId = request.pathVariable(name)
    validateDouble(cardId)
    return cardId.toDouble()
}

fun validateLong(cardId: String) {
    try {
        cardId.toLong()
    } catch (ex: NumberFormatException) {
        throw InvalidCardIdException(message = "Invalid Long Value: $cardId")
    }
}

fun validateDouble(cardId: String) {
    try {
        cardId.toDouble()
    } catch (ex: NumberFormatException) {
        throw InvalidCardIdException(message = "Invalid Long Value: $cardId")
    }
}

fun getUserMono() : Mono<SimpleUser>{
    val context : Mono<SecurityContext> = ReactiveSecurityContextHolder.getContext()

    var i = ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getCredentials)
            .cast(SimpleUser::class.java)

    return i
}

fun getUser() : SimpleUser{
    val context : Mono<SecurityContext> = ReactiveSecurityContextHolder.getContext()

    var i = ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getCredentials)
            .cast(SimpleUser::class.java)

    return i.block()!!
}