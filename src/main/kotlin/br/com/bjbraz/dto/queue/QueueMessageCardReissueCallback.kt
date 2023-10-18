package br.com.bjbraz.dto.queue

import br.com.bjbraz.dto.CardReissueCallback
import lombok.Data

@Data
class QueueMessageCardReissueCallback (
        var callback: CardReissueCallback?=null
)