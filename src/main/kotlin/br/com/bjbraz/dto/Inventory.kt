package br.com.bjbraz.dto

import java.io.Serializable

class Inventory(
        val name: String?,
        val items: Integer?,
        val remaining: Integer?
) : Serializable