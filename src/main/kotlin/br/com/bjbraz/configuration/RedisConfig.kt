package br.com.bjbraz.configuration

import org.springframework.beans.factory.annotation.Value

//
//@Configuration
//@EnableRedisRepositories(basePackages=["br.com.bjbraz", "br.com.bjbraz.**", "br.com.bjbraz.entity.*"] )
//@ConditionalOnProperty(name = ["spring.cache.type"], havingValue = "redis")
//@EnableCaching
class RedisConfig (@Value("\${spring.redis.host}") val host: String, @Value("\${spring.redis.port}") val port: Int) {

//
//    private val cacheNames: List<String> = ArrayList()
//
//    private val cacheExpiration: Map<String, Long> = HashMap()
//
//    private val logger = LoggerFactory.getLogger(RedisConfig::class.java)


//    @Bean
//    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory {
//        return LettuceConnectionFactory(RedisStandaloneConfiguration("localhost", 6379))
//    }
//
//    @Bean
//    fun redisConnectionFactory(): LettuceConnectionFactory? {
//        val conf = RedisStandaloneConfiguration(host, port)
//
//        return LettuceConnectionFactory()
//    }
//
//    @PostConstruct
//    private fun configureCache() {
//        logger.info("Cache foi reiniciado.")
//        RegisteredCaches.values().forEach { x ->
//            cacheNames.toMutableList().add(x.cacheName)
//            cacheExpiration.toMutableMap().putIfAbsent(x.name, x.expireTime)
//        }
//    }
//
//    @Bean
//    fun redisTemplate(): RedisTemplate<Any, Any>? {
//        val redisTemplate = RedisTemplate<Any, Any>()
//        redisConnectionFactory()?.let { redisTemplate.setConnectionFactory(it) }
//        redisTemplate.keySerializer = Jackson2JsonRedisSerializer(Any::class.java)
//        redisTemplate.hashKeySerializer = Jackson2JsonRedisSerializer(Any::class.java)
//        redisTemplate.valueSerializer = Jackson2JsonRedisSerializer(Any::class.java)
//        redisTemplate.setDefaultSerializer(Jackson2JsonRedisSerializer(Any::class.java))
//        redisTemplate.stringSerializer = Jackson2JsonRedisSerializer(String::class.java)
//
//
//        return redisTemplate
//    }
//
//    @Bean
//    fun cacheManager(connectionFactory: RedisConnectionFactory?): RedisCacheManager? {
//        val ret = RedisCacheManager.create(connectionFactory!!)
//
//        var cacheConfig = defaultCacheConfig().disableCachingNullValues()
//        cacheConfig.entryTtl(Duration.ofSeconds(3600))
//        cacheConfig.disableCachingNullValues()
//
//        val cm = RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(cacheConfig)
//                .transactionAware()
//                .build()
//
//        return ret
//    }

//     fun cacheManager(): RedisCacheManager? {
//        val redisCacheManager = RedisCacheManager.create(connectionFactory!!)
//        redisCacheManager.ex(true)
//        redisCacheManager.setDefaultExpiration(3600L)
//        redisCacheManager.setCacheNames(cacheNames)
//        redisCacheManager.exp(cacheExpiration)
//        return redisCacheManager
//    }

//
//    enum class RegisteredCaches( val cacheName: String, val expireTime: Long) {
//        PARAMS("params", 86400L),
//        CARS("cars", 86400L),
//        DEVICES("devices", 86400L),
//        VALORES("valores", 86400L),
//        INVENTORIES("inventories", 86400L),
//        ORDERS("orders", 86400L),
//        DEFAULT_PACK("defaultPack", 86400L),
//        ACCOUNT("account", 86400L),
//        CONTRACT("contract", 604800L);
//    }
}