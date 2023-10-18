package br.com.bjbraz.controller

import br.com.bjbraz.entity.docted.BankEntity
import br.com.bjbraz.exception.DuplicatedBankException
import br.com.bjbraz.exception.NotFoundException
import br.com.bjbraz.repository.BankRepository
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheConfig
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.BodyExtractors
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/bank")
@CacheConfig(cacheNames= ["bank"])
class BankController(val repository: BankRepository) {
    private val logger = LoggerFactory.getLogger(BankController::class.java)

    @GetMapping(value = ["/all"], produces = ["application/json"])
    fun listAll(): ResponseEntity<Flux<BankEntity>> {
        return ResponseEntity.status(HttpStatus.OK).body(repository.findAll())
    }

    @PutMapping(value = [""], produces = ["application/json"])
    @ResponseStatus(HttpStatus.CREATED)
    fun addBank(@RequestBody request: BankEntity): Mono<BankEntity> {
        try{
          return repository
            .findByNumber(request.number!!)
            .switchIfEmpty(
                repository.save(request)
            )

        }catch(ex:Exception){
            logger.error("Error finding Bank")
            throw DuplicatedBankException(message = "Mensagem", path = "", logref = "")
        }

    }

    @PatchMapping(value = ["/{bankNumber}"], produces = ["application/json"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateBank(@PathVariable bankNumber: Long, @RequestBody request: BankEntity): Mono<BankEntity> {
        try{

//            return repository.findByNumber(request.number!!)
//                .switchIfEmpty(Mono.error(NotFoundException))
//                .flatMap { bank -> repository.save( request ) }

            return repository
                .findByNumber(bankNumber)
                .switchIfEmpty(
                    Mono.error(NotFoundException)
                )
                .doOnSuccess{ it.name = request.name }
                .flatMap { bank -> repository.save(bank) }


        }catch(ex:Exception){
            logger.error("Error finding Bank")
            throw DuplicatedBankException(message = "Mensagem", path = "", logref = "")
        }

    }

    @DeleteMapping(value = ["/{bankNumber}"])
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    fun delete(@PathVariable bankNumber: Long): Mono<Void> {
        return repository.findByNumber(bankNumber)
            .switchIfEmpty(Mono.error(NotFoundException))
            .flatMap { bank -> repository.delete(bank) }
            .then(Mono.empty())
    }

    @GetMapping(value = ["/{bankNumber}"])
    @ResponseStatus(value = HttpStatus.OK)
    fun get(@PathVariable bankNumber: Long): Mono<BankEntity> {
        return repository
            .findByNumber(bankNumber)
            .switchIfEmpty(
                Mono.error(NotFoundException)
            )
    }

}
