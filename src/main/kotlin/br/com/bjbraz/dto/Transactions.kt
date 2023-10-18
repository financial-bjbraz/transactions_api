package br.com.bjbraz.dto

import java.io.Serializable

data class Transactions(val localTransactions:AccountTransactions?=null, val remoteTransactions:AccountTransactions?=null, val legacy: AccountTransactions?= null) : Serializable