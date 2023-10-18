package br.com.bjbraz.dto

import lombok.Data

@Data
class DocTedBankBeneficiary (
        var name:String? = null, //Destination Bank Name
        var branch:String?= null, // AGENCIA

        var account:String? = null,
        var accountDigit:String? = null,
        var number:String? = null,
        var beneficiaryName:String? = null,
        var beneficiaryDocument:String? = null,
        var bankAccountType:String? = null
)