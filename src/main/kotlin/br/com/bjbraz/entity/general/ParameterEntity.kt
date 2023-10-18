package br.com.bjbraz.entity.general

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.util.*

@Document(value = "parameters")
class ParameterEntity(
    @Id
    val id: String? = null,

    val name: String? = null,

    val value: String? = null,

    val dtCreation: Date? = null,

    val dhLastUpdate: Date? = null
) : Serializable