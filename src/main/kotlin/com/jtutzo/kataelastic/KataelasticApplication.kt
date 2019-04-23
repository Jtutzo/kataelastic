package com.jtutzo.kataelastic

import org.apache.http.HttpHost
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.beans.factory.annotation.Autowired
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
    fun client(): RestHighLevelClient = RestHighLevelClient(RestClient.builder(HttpHost(host, port.toInt(), "http")))

    @Autowired
    fun createRequestsMapping(client: RestHighLevelClient) {
        val request = CreateIndexRequest("requests")
        val properties = hashMapOf(
                "code" to mappingCodeField(),
                "rctCode" to mappingRctCodeField(),
                "dealName" to mappingDealNameField(),
                "users" to mappingUsersField(),
                "teams" to mappingTeamsField()
        )
        val mapping = hashMapOf(Pair("request", hashMapOf(Pair("properties", properties))))
        request.mapping("request", mapping)
        client.indices().create(request, RequestOptions.DEFAULT)
    }

    private fun mappingCodeField() = hashMapOf("type" to "text", "fields" to hashMapOf("raw" to hashMapOf("type" to "keyword")))
    private fun mappingRctCodeField() = hashMapOf("type" to "text", "fields" to hashMapOf("raw" to hashMapOf("type" to "keyword")))
    private fun mappingDealNameField() = hashMapOf("type" to "text", "fields" to hashMapOf("keyword" to hashMapOf("type" to "keyword")))
    private fun mappingUsersField() = hashMapOf("type" to "text", "fields" to hashMapOf("keyword" to hashMapOf("type" to "keyword")))
    private fun mappingTeamsField() = hashMapOf("type" to "text", "fields" to hashMapOf("keyword" to hashMapOf("type" to "keyword")))

}

@Configuration
@EnableSwagger2
class SwaggerConfig {

    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build()
}
