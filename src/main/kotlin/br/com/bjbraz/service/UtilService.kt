package br.com.bjbraz.service

import br.com.bjbraz.dto.Car
import br.com.bjbraz.dto.Inventory

interface UtilService {
    fun devices(name:String) : List<Car>

    fun orders(name:String) : List<Car>

    fun inventory(name:String) : List<Inventory>

    fun values(name:String) : List<String>

    fun cars(name:String): List<Car>

}