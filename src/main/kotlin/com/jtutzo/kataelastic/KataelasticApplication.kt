package com.jtutzo.kataelastic

import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
class KataelasticApplication

fun main(args: Array<String>) {
    runApplication<KataelasticApplication>(*args)
}

@Configuration
class ElasticsearchConfig {

    @Value("\${elasticsearch.host}")
    private lateinit var host: String

    @Value("\${elasticsearch.port}")
    private lateinit var port: String

    @Bean(destroyMethod = "close")
    fun client(): RestHighLevelClient = RestHighLevelClient(RestClient.builder(HttpHost(host, port.toInt())))

}

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api(): Docket {
        return Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
    }
}
