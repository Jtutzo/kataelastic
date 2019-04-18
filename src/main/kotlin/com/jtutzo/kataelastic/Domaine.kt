package com.jtutzo.kataelastic

import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.stereotype.Service
import java.util.*
import javax.inject.Inject

data class Request(val code: String, val rctCode: String, val dealName:String, val users: Set<String>, val teams: Set<String>)

@Service
class RequestService @Inject constructor(private val client: RestHighLevelClient, private val objectMapper: ObjectMapper){

    private val index = "requests"
    private val type = "request"

    fun create(request: Request): String {
        val id = UUID.randomUUID()
        val requestMapper = objectMapper.convertValue(request, Map::class.java)
        val indexRequest = IndexRequest(index, type, id.toString()).source(requestMapper)

        return client.index(indexRequest, RequestOptions.DEFAULT).result.name
    }

}