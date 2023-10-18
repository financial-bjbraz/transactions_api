package br.com.bjbraz.service

import br.com.bjbraz.dto.Account
import br.com.bjbraz.dto.GeneratedAccountInfo

interface GenerateAccount {
    fun createNewAccount(account: Account) : GeneratedAccountInfo?
}
