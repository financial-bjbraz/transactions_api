package br.com.bjbraz.controller

import br.com.bjbraz.entity.docted.BeneficiaryEntity
import br.com.bjbraz.repository.BankRepository
import br.com.bjbraz.repository.BankTypeRepository
import br.com.bjbraz.repository.BeneficiaryRepository
import br.com.bjbraz.repository.SQLAccountRepository
import br.com.bjbraz.service.BankAccountService
import br.com.bjbraz.service.BankAccountServiceImpl
import kotlinx.coroutines.reactive.awaitFirstOrElse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/beneficiary")
//@CacheConfig(cacheNames= ["beneficiary"])
class BeneficiaryController(
    val repository: BeneficiaryRepository,
    val bankRepository: BankRepository,
    val accountRepository: SQLAccountRepository,
    val service: BankAccountServiceImpl,
    val accountTypeRepository: BankTypeRepository
    ) {
    private val logger = LoggerFactory.getLogger(BeneficiaryController::class.java)

    @GetMapping(value = ["/{cpf}/all"], produces = ["application/json"])
    fun listAll(@PathVariable cpf:String): ResponseEntity<List<BeneficiaryEntity>> {
        var opt = accountRepository.findByResponsibleIdentifierDocumentDocument(cpf)

        if(!opt.isPresent)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()

        val account = opt.get()

        return ResponseEntity.status(HttpStatus.OK).body(repository.findByOwnerDocument(cpf))
    }

    @PutMapping(value = ["/{cpf}"], produces = ["application/json"])
    fun addBeneficiary(@PathVariable cpf:String, @RequestBody request: BeneficiaryEntity): ResponseEntity<Any> {
        var opt = accountRepository.findByResponsibleIdentifierDocumentDocument(cpf)

        if(!opt.isPresent)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ACCOUNT NOT FOUND")

        if(request.bank == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BANK NOT INFORMED")

        if(request.bank!!.name == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("BANK NOT INFORMED")

        if(request.bankAccountType == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ACCOUNT TYPE NOT INFORMED")

        if(request.bankAccountType!!.id == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ACCOUNT TYPE NOT INFORMED")

        val account = opt.get()
        val bank    = bankRepository.findById(request.bank!!.number!!.toString())
//        val accountType = accountTypeRepository.findById(request.bankAccountType!!.number!!)

//        bank.block() {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("BANK NOT FOUND")
//        }


//        if(!accountType.isPresent)
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ACCOUNT TYPE NOT FOUND")

        return try {
            service.saveBeneficiary(cpf, request)
            ResponseEntity.status(HttpStatus.CREATED).build()
        }catch(ex:Exception){
            logger.error("Error saving Beneficiary $ex")
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("COULD NOT SAVE BENEFICIARY")
        }
    }
}