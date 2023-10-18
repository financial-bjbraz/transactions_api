package br.com.bjbraz.dto

import java.io.Serializable

data class AccountTransactions(
        var _embedded : AccountTransactionsEmbedded? = null
) : Serializable