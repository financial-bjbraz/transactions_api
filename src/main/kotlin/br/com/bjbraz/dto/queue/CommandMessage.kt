package br.com.bjbraz.dto.queue

import lombok.Data

@Data
class CommandMessage (
        val command: String = "",
        val type: Int? = 999,
        val identifier:String? = ""
)