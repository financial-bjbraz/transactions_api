package br.com.bjbraz.dto

import java.io.Serializable

data class AccountTransactionsList(
        val id:Int? = null,
        val transactionReference:Int? = null,
        val entryType:String? = null,
        val entryModeDescription:String? = null,
        val amount:Double? = null,
        val referenceDate:String? = null,
        val summary:String? = null,
        val createdAt:String? = null,
        val channel:AccountTransactionsChannel? = null
) : Serializable