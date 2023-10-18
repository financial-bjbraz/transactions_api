package br.com.bjbraz.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.CorsRegistry
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebConfig: WebFluxConfigurer
{

    override fun addCorsMappings(registry: CorsRegistry)
    {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://bjbraz.com.br",
                        "https://bjbraz.com.br",
                        "http://bjbraz.com.br",
                        "http://www.bjbraz.com.br",
                        "https://www.bjbraz.com.br",
                        "http://192.168.0.62",
                        "http://192.168.0.57:4200",
                "http://localhost:4200") // any host or put domain(s) here
                .allowedMethods("*") // put the http verbs you want allow
                .allowedHeaders("*" ,
                        "Access-Control-Allow-Headers",
                        "Access-Control-Allow-Origin",
                        "Access-Control-Allow-Credentials",
                        "Access-Control-Allow-Methods",
                        "Content-type",
                        "x-corralation-id") // put the http headers you want allow
                .exposedHeaders("Access-Control-Allow-Methods")
                .exposedHeaders("Access-Control-Allow-Headers")
                .exposedHeaders("Access-Control-Allow-Credentials")
                .allowCredentials(true).maxAge(3600)
    }

}