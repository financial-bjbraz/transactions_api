package br.com.bjbraz.dto

import br.com.bjbraz.entity.card.AccountStatus
import java.util.*


class Account(

        var idempotencyKey:String?=null,

        val owner:People?=null,

        val responsible:People?=null,

        val accountNumber: Integer?=null,

        val dt_ref: Date?=null,

        val firebaseId: String?=null,

        var financialOperationKey:String?=null,

        var requestCard:Boolean=true,

        var status: AccountStatus = AccountStatus.CREATED
        )