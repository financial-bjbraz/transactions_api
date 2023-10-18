package br.com.bjbraz.service

import br.com.bjbraz.dto.Car
import br.com.bjbraz.dto.Inventory
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
//@CacheConfig(cacheNames= ["devices", "orders", "inventories", "valores"])
class UtilServiceImpl() : UtilService {

    @Cacheable(key="#name", value= ["devices"])
    override fun devices(name:String): List<Car> {
        return listOf(Car("teste1", Integer(1)), Car("teste2", Integer(2)))
    }

    @Cacheable(key="#name", value = ["orders"])
    override fun orders(name:String): List<Car> {
        return listOf(Car("teste1", Integer(1)), Car("teste2", Integer(2)))
    }

    @Cacheable(key="#name", value = ["inventories"])
    override fun inventory(name:String): List<Inventory> {
        return listOf(Inventory("teste1", Integer(1), remaining = Integer(4)), Inventory("teste2", Integer(5), remaining = Integer(6)))
    }

    @Cacheable(key="#name", value = ["valores"])
    override fun values(name:String): List<String> {
        return listOf( "13,28,19,24,43,49,40,35,42,46,38,32,45" )
    }

    @Cacheable(key="#name", value = ["cars"])
    override fun cars(name:String): List<Car> {
        return listOf(Car("teste1", Integer(1)), Car("teste2", Integer(2)))
    }
}