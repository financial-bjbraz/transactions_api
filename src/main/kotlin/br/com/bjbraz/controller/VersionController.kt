package br.com.bjbraz.controller

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/version")
class VersionController {
    private val logger = LoggerFactory.getLogger(VersionController::class.java)

    @RequestMapping(
            value = [""],
            method = [RequestMethod.GET],
            produces = ["application/json"]
    )
    fun getVersion(): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body("{\"version\":\"1.10 23/06/2020\"}")
    }


}