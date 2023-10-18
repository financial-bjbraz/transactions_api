package br.com.bjbraz.service

import br.com.bjbraz.entity.docted.BeneficiaryEntity
import br.com.bjbraz.repository.BankRepository
import br.com.bjbraz.repository.BankTypeRepository
import br.com.bjbraz.repository.BeneficiaryRepository
import br.com.bjbraz.repository.SQLAccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service("bankAccountServiceImpl")
class BankAccountServiceImpl(
    private val repository: BeneficiaryRepository,
    private val bankRepository: BankRepository,
    val accountRepository: SQLAccountRepository,
    val accountTypeRepository: BankTypeRepository
    ) {

    fun saveBeneficiary(cpf:String, beneficiary: BeneficiaryEntity): Boolean {

        return true
    }


}