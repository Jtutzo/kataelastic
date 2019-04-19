package com.jtutzo.kataelastic

import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.stereotype.Repository
import javax.inject.Inject

@Repository
class RequestElasticSearchReporistory @Inject constructor(private val client: RestHighLevelClient, private val objectMapper: ObjectMapper): RequestRepository {

    private val index = "requests"
    private val type = "request"

    override fun create(request: Request): String {
        val requestMapper = objectMapper.convertValue(request, Map::class.java)
        val indexRequest = IndexRequest(index, type, request.code).source(requestMapper)

        return client.index(indexRequest, RequestOptions.DEFAULT).result.name
    }

    override fun update(request: Request): String {
        val updateRequest = UpdateRequest(index, type, request.code)
        val requestMapper = objectMapper.convertValue(request, Map::class.java)

        updateRequest.doc(requestMapper)

        return client.update(updateRequest, RequestOptions.DEFAULT).result.name
    }

    override fun findByCode(code: String): Request {
        val getRequest = GetRequest(index, type, code)
        val getResponse = client.get(getRequest, RequestOptions.DEFAULT)

        return objectMapper.convertValue(getResponse.source, Request::class.java)
    }
}