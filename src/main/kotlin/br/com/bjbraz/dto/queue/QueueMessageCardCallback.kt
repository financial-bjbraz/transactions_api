package br.com.bjbraz.dto.queue

import br.com.bjbraz.dto.CardCreationCallback
import lombok.Data

@Data
class QueueMessageCardCallback (
        var callback: CardCreationCallback?=null
)