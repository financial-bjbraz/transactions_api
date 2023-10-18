package br.com.bjbraz.dto

import java.io.Serializable

data class AccountTransactionsEmbedded (val transactions:List<AccountTransactionsList>?=null) : Serializable