package br.com.bjbraz

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@SpringBootApplication(scanBasePackages = [
    "br.com.bjbraz.service",
    "br.com.bjbraz.auth",
    "br.com.bjbraz.configuration",
    "br.com.bjbraz.consumer",
    "br.com.bjbraz.controller",
    "br.com.bjbraz.entity",
    "br.com.bjbraz.handler",
    "br.com.bjbraz.**"])
@EnableReactiveMongoRepositories(basePackages = [
    "br.com.bjbraz.repository"])
class TransactionsApiApplication

fun main(args: Array<String>) {
    val context = runApplication<TransactionsApiApplication>(*args)

    println("Sending an email message.")
//    jmsTemplate.convertAndSend("teste", "application uma mensagem de teste")

}
