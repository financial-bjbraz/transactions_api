package br.com.bjbraz.service

import br.com.bjbraz.entity.account.AccountEntity
import br.com.bjbraz.entity.docted.BeneficiaryEntity

interface BankAccountService {
    fun saveBeneficiary(cpf: String, beneficiary: BeneficiaryEntity) : Boolean
}