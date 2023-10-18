package br.com.bjbraz.handler

enum class ApplicationErrorType(val errorCode: String, val errorMessage: String) {
    //Request Errors
    UNAUTHORIZED("0005", "Não Autorizado"),
    ACCESS_DENIED("0006","Acesso negado nesse recurso"),
    INVALID_PAYLOAD("0010", "Requisição inválida."),
    //Business Errors
    INSUFFICIENT_FUNDS("0301", "Saldo insuficiente."),
    IDEMPOTENCY_KEY_INVALID("0302", "Chave de Idempotencia inválida"),
    //Internal Errors
    ACCOUNT_NOT_FOUND("00101", "Conta não encontrada"),
    INVALID_ACCOUNT_INSTANCE_ID("0102", "Erro interno"),
    //Server Errors
    SESSION_EXPIRED("0910", "Token expirado"),
    RESOURCE_NOT_FOUND("0990", "Not Found"),
    SERVICE_UNAVAILABLE("0991", "Erro interno"),
    INTERNAL_ERROR("0999", "Erro interno")
}

enum class Logrefs {
    INVALID,
    NOT_FOUND,
    REQUIRED,
    DENIED,
    INTERNAL_ERROR
}
