package br.com.bjbraz.controller

import br.com.bjbraz.entity.docted.BankAccountTypeEntity
import br.com.bjbraz.repository.BankTypeRepository
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux

@RestController
@RequestMapping("/accounttype")
//@CacheConfig(cacheNames= ["accounttype"])
class BankAccountTypeController(val repository: BankTypeRepository) {
    private val logger = LoggerFactory.getLogger(BankAccountTypeController::class.java)

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun listAll(): ResponseEntity<Flux<BankAccountTypeEntity>> {
        return ResponseEntity.status(HttpStatus.OK).body(repository.findAll())
    }

    @PutMapping(value = [""], produces = ["application/json"])
    fun addBankType(@RequestBody request: BankAccountTypeEntity): ResponseEntity<BankAccountTypeEntity> {
        if(request.name.isNullOrEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        try {
            val obj = repository.findByName(request.name!!) .get()//?.let { it -> it.orElse(repository.save(request))  }
            return ResponseEntity.status(HttpStatus.CREATED).body(obj)
        }catch(ex:Exception){
            logger.error("Error finding Bank")
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
        }

    }

}