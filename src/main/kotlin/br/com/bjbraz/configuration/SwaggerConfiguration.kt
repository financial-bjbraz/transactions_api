package br.com.bjbraz.configuration


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfiguration {
//
//    fun createRestApi(): Docket {
//        return Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(ApiInfoBuilder()
//                        .description("API para a consulta de dados desnormalizados")
//                        .title("Cards Information API")
//                        .version("1.0.0")
//                        .build())
//                .select()
//
//                .apis(RequestHandlerSelectors.basePackage("br.com.bjbraz.controller"))
//                .paths(PathSelectors.any())
//                .build()
//    }
//
//    @Bean
//    open fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
//            .select()
//            .apis(RequestHandlerSelectors.any())
//            .paths(PathSelectors.any())
//            .build()
//
//
//
//
//    fun createRestApiOld(): Docket {
//        return Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(ApiInfoBuilder()
//                        .description("Financial API soluções financieiras OpenBanking + Seguros")
//                        .title("Financial API")
//                        .version("1.0.0")
//                        .license("Apache 2.0")
//                        .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
//                        .contact(Contact("Alex Braz", "www.bjbraz.com.br", "contato@bjbraz.com.br"))
//                        .build())
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("br.com.bjbraz.controller"))
//                .paths(PathSelectors.any())
//                .build()
//    }

}