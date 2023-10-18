package br.com.bjbraz.exception

import br.com.bjbraz.handler.ApplicationErrorType
import br.com.bjbraz.handler.Logrefs
import com.fasterxml.jackson.annotation.JsonProperty

data class VndError(val logref: String = "INVALID", val message: String = "", val path: String? = "", val errorCode: String? = "")

data class Embedded(val errors: MutableList<VndError> = mutableListOf())

data class VndErrors(
    @JsonProperty("_embedded")
    val embedded: Embedded = Embedded(mutableListOf())
)

fun getResponse(logrefs: Logrefs, applicationErrorType: ApplicationErrorType, path: String) = VndError(
    logref = logrefs.name,
    message = applicationErrorType.errorMessage,
    path = path,
    errorCode = applicationErrorType.errorCode
)