package br.com.bjbraz.dto

import lombok.Getter
import lombok.Setter
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Getter
@Setter
@Document(value = "request_response")
class RequestReponse(
        @Id
        var id:String? = null,
        var request: String?,
        var response: String?,
        var httpCode: Int?,
        var headers: String?,
        var method: String?,
        var uri: String?,
        var date: String?

)