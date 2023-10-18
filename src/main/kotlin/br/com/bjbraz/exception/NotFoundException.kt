package br.com.bjbraz.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Item not found")
object NotFoundException : RuntimeException()