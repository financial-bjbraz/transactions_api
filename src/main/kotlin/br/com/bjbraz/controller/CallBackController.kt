package br.com.bjbraz.controller

import br.com.bjbraz.dto.CardCreationCallback
import br.com.bjbraz.dto.CardReissueCallback
import br.com.bjbraz.dto.queue.QueueMessageCardCallback
import br.com.bjbraz.dto.queue.QueueMessageCardReissueCallback
import br.com.bjbraz.repository.ParameterRepository
import br.com.bjbraz.service.SqsMessageQueueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/callback")
//@CacheConfig(cacheNames= ["devices"])
class CallBackController (@Autowired val queueService: SqsMessageQueueService, @Autowired val repository: ParameterRepository) {

    @RequestMapping(
            value = ["/card"],
            method = [RequestMethod.POST],
            produces = ["application/json"]
    )
    fun onCallBackReceived(@RequestBody request: CardCreationCallback): ResponseEntity<Void> {
        queueService.sendMessageCardCallBack(QueueMessageCardCallback(callback = request))
        return ResponseEntity.ok().build()
    }

    @RequestMapping(
            value = ["/reissue"],
            method = [RequestMethod.POST],
            produces = ["application/json"]
    )
    fun reissued(@RequestBody request: CardReissueCallback): ResponseEntity<Void> {
        queueService.sendMessageCardReissueCallBack(QueueMessageCardReissueCallback(callback = request))
        return ResponseEntity.ok().build()
    }

    @RequestMapping(
            value = ["/docted"],
            method = [RequestMethod.POST],
            produces = ["application/json"]
    )
    fun docted(@RequestBody request: CardCreationCallback): ResponseEntity<Void> {
        queueService.sendMessageCardCallBack(QueueMessageCardCallback(callback = request))
        return ResponseEntity.ok().build()
    }

    @RequestMapping(
            value = ["/payment"],
            method = [RequestMethod.POST],
            produces = ["application/json"]
    )
    fun payment(@RequestBody request: CardCreationCallback): ResponseEntity<Void> {
        queueService.sendMessageCardCallBack(QueueMessageCardCallback(callback = request))
        return ResponseEntity.ok().build()
    }

}