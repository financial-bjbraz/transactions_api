package br.com.bjbraz.service

import br.com.bjbraz.entity.card.CardEntity

interface GenerateCard {
    fun requestHubCard(cardEntity: CardEntity): Boolean
}