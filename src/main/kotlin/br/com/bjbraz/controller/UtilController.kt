package br.com.bjbraz.controller

import br.com.bjbraz.auth.User
import br.com.bjbraz.dto.*
import br.com.bjbraz.entity.general.ParameterEntity
import br.com.bjbraz.repository.ParameterRepository
import br.com.bjbraz.service.AccountServiceImpl
import br.com.bjbraz.service.UtilService
import br.com.bjbraz.service.getUserMono
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/")
//@CacheConfig(cacheNames= ["devices"])
class UtilController(@Autowired val utilService:UtilService, val paramRepo: ParameterRepository, val accountService: AccountServiceImpl
                     ) {

    fun currentUser(auth: Authentication?): Optional<User>? {
        if (auth != null) {
            val principal = auth.principal
            if (principal is User) // User is your user type that implements UserDetails
                return Optional.of(principal)
        }
        return Optional.empty()
    }

    @GetMapping(value = ["principal"], produces = ["application/json"])
    fun principal(): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.OK).body(getUserMono())
    }


    @GetMapping(value = ["cool-cars"], produces = ["application/json"])
    fun getCoolCars(): ResponseEntity<List<Car>> {
        return ResponseEntity.status(HttpStatus.OK).body(utilService.cars("simple"))
    }

    @GetMapping(value = ["devices"], produces = ["application/json"])
    fun devices(): ResponseEntity<List<Car>> {
        return ResponseEntity.status(HttpStatus.OK).body(utilService.devices("simple"))
    }

    @GetMapping(value = ["orders"], produces = ["application/json"])
    fun orders(): ResponseEntity<List<Car>> {
        return ResponseEntity.status(HttpStatus.OK).body(utilService.orders("simple"))
    }

    @GetMapping(value = ["inventory"], produces = ["application/json"])
    fun inventory(): ResponseEntity<List<Inventory>> {
        return ResponseEntity.status(HttpStatus.OK).body(utilService.inventory("simple"))
    }

    @GetMapping(value = ["values"], produces = ["application/json"])
    fun values(): ResponseEntity<List<String>> {
        return ResponseEntity.status(HttpStatus.OK).body(utilService.values("simple"))
    }

    @GetMapping(value = ["cars"], produces = ["application/json"])
    fun cars(): ResponseEntity<List<Car>> {
        return ResponseEntity.status(HttpStatus.OK).body(utilService.cars("complex"))
    }

    @GetMapping(value = ["params/{key}"], produces = ["application/json"])
    fun params(@PathVariable key:String): ResponseEntity<ParameterEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(paramRepo.findByName(key).get())
    }

    @PostMapping(value = ["test"], produces = ["application/json"])
    fun teste(): ResponseEntity<AccountTransactions> {

        val tx1:AccountTransactionsList = AccountTransactionsList(id = 1, entryType = "Local",entryModeDescription = "Contact")
        val tx2:AccountTransactionsList = AccountTransactionsList(id = 2, entryType = "Local",entryModeDescription = "Contact")
        val tx3:AccountTransactionsList = AccountTransactionsList(id = 3, entryType = "Local",entryModeDescription = "Contact")

        val transactions:List<AccountTransactionsList> = listOf(tx1, tx2, tx3)
        val embedded:AccountTransactionsEmbedded = AccountTransactionsEmbedded(transactions = transactions)
        val tx:AccountTransactions = AccountTransactions(_embedded = embedded)
        return ResponseEntity.status(HttpStatus.OK).body(tx)
    }

    @PostMapping(value = ["test1"], produces = ["application/json"])
    fun teste1(): ResponseEntity<AccountTransactions> {
        val tx1:AccountTransactionsList = AccountTransactionsList(id = 1, entryType = "Remote",entryModeDescription = "Contactless")
        val tx2:AccountTransactionsList = AccountTransactionsList(id = 2, entryType = "Remote",entryModeDescription = "Contactless")
        val tx3:AccountTransactionsList = AccountTransactionsList(id = 3, entryType = "Remote",entryModeDescription = "Contactless")

        val transactions:List<AccountTransactionsList> = listOf(tx1, tx2, tx3)
        val embedded:AccountTransactionsEmbedded = AccountTransactionsEmbedded(transactions = transactions)
        val tx:AccountTransactions = AccountTransactions(_embedded = embedded)
        return ResponseEntity.status(HttpStatus.OK).body(tx)
    }

    @GetMapping(value = ["statements"], produces = ["application/json"])
    suspend fun statements(): ResponseEntity<Transactions> = coroutineScope {
        val product: Deferred<AccountTransactions?> = async(start = CoroutineStart.LAZY) {
            //productRepository.getProductById(id)
            accountService.requestTransactionsFallBack()
        }
        val quantity: Deferred<AccountTransactions> = async(start = CoroutineStart.LAZY) {
            accountService.requestLocalTransactions()
        }

        val legacy: Deferred<AccountTransactions> = async(start = CoroutineStart.LAZY) {
            accountService.requestStatements()
        }

        Transactions(product.await()!!, quantity.await(), legacy.await()).let { it -> ResponseEntity.status(HttpStatus.OK).body(it) }

    }

}