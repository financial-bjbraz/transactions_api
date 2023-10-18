package br.com.bjbraz.exception

import lombok.ToString
import org.springframework.http.HttpStatus

@ToString
class DuplicatedBankException(
    val logref: String, override val message: String?, val path: String?,
    val httpStatus: HttpStatus = HttpStatus.CREATED
) : RuntimeException(message)