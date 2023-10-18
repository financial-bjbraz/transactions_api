package br.com.bjbraz.dto

class AccountTransferBody(val amount:Double, var toFinancialOperationKey:String, var fromFinancialOperationKey:String, val cpf:String, var transferCode:String, var summary:String, var idempotencyKey:String)