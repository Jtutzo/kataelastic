package com.jtutzo.kataelastic

import com.fasterxml.jackson.databind.ObjectMapper
import org.elasticsearch.action.get.GetRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.update.UpdateRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestHighLevelClient
import org.elasticsearch.index.query.QueryBuilders
import org.elasticsearch.search.builder.SearchSourceBuilder
import org.springframework.stereotype.Repository
import javax.inject.Inject


@Repository
class RequestElasticSearchRepository @Inject constructor(private val client: RestHighLevelClient, private val objectMapper: ObjectMapper) : RequestRepository {

    private val index = "requests"
    private val type = "request"

    override fun create(request: Request): String {
        val requestMapper = objectMapper.convertValue(request, Map::class.java)
        val indexRequest = IndexRequest(index, type, request.code).source(requestMapper)

        return client.index(indexRequest, RequestOptions.DEFAULT).result.name
    }

    override fun update(request: Request): String {
        val requestMapper = objectMapper.convertValue(request, Map::class.java)
        val updateRequest = UpdateRequest(index, type, request.code).doc(requestMapper)

        return client.update(updateRequest, RequestOptions.DEFAULT).result.name
    }

    override fun findByCode(code: String): Request {
        val getRequest = GetRequest(index, type, code)
        val getResponse = client.get(getRequest, RequestOptions.DEFAULT)

        return objectMapper.convertValue(getResponse.source, Request::class.java)
    }

    override fun findAll(): Set<Request> {
        val searchRequest = SearchRequest()
        val searchSourceBuilder = SearchSourceBuilder()
        searchSourceBuilder.query(QueryBuilders.matchAllQuery())
        searchRequest.source(searchSourceBuilder)

        val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)

        return getSearchResult(searchResponse)
    }

    override fun search(term: String, users: Set<String>, teams: Set<String>): Set<Request> {
        val searchRequest = SearchRequest()
                .source(SearchSourceBuilder()
                        .query(QueryBuilders
                                .boolQuery()
                                .must(QueryBuilders.boolQuery()
                                        .should(QueryBuilders.termQuery("code", term))
                                        .should(QueryBuilders.termQuery("rctCode", term))
                                        .should(QueryBuilders.fuzzyQuery("dealName", term)))
                                .must(QueryBuilders.boolQuery()
                                        .should(QueryBuilders.termsQuery("users", users))
                                        .should(QueryBuilders.termsQuery("teams", teams)))))

        val searchResponse = client.search(searchRequest, RequestOptions.DEFAULT)

        return getSearchResult(searchResponse)
    }

    override fun updateRctCode(code: String, value: String): String {
        val requestMapper = hashMapOf(Pair("rctCode", value))
        val updateRequest = UpdateRequest(index, type, code).doc(requestMapper)

        return client.update(updateRequest, RequestOptions.DEFAULT).result.name
    }

    private fun getSearchResult(response: SearchResponse): Set<Request> = response.hits.hits
            .toHashSet()
            .map { objectMapper.convertValue(it.sourceAsMap, Request::class.java) }
            .toSet()

}