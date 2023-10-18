package br.com.bjbraz.entity.general

enum class Command(val type:Int) {
    BLOCK_CARD( 0),
    UNBLOCK_CARD( 1),
    BLOCK_ACCOUNT( 2),
    UNBLOCK_ACCOUNT( 3)
}