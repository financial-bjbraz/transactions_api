package br.com.bjbraz.service

import br.com.bjbraz.dto.BalanceDTO

interface CardService {
    suspend fun getBalance(externalCode: String) : BalanceDTO

    fun blockCard(externalCode: String): Boolean

    fun unBlockCard(externalCode: String): Boolean

    fun blockAccount(cpf: String): Boolean

    fun unBlockAccount(cpf: String): Boolean
}