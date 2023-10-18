package br.com.bjbraz.service

import br.com.bjbraz.entity.card.CardEntity

interface ReissueCard {
    fun requestReissueHubCard(cardEntity: CardEntity, idempotency:String): Boolean
}