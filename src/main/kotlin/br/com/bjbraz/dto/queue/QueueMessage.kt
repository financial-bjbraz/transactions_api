package br.com.bjbraz.dto.queue

import br.com.bjbraz.dto.Account
import lombok.Data


@Data
class QueueMessage (
        val message: Account? = null,
        val id: String? = ""
)