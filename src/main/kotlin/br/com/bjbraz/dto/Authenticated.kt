package br.com.bjbraz.dto

class Authenticated(
        val access_token: String,
        val token_type: String?,
        val expires_in: String?,
        val scope: String?,
        val jti: String?
)