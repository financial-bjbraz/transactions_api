package br.com.bjbraz.dto.queue

import lombok.Data

@Data
class QueueMessageCard (
        val cpf: String? = null,
        val fok: String? = "",
        val orderNumber: String = ""
)