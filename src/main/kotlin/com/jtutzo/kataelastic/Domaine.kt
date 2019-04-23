package com.jtutzo.kataelastic


data class Request(val code: String, val rctCode: String, val dealName: String, val users: Set<String>, val teams: Set<String>)

interface RequestRepository {
    fun create(request: Request): String
    fun update(request: Request): String
    fun updateRctCode(code: String, value: String): String
    fun findByCode(code: String): Request
    fun findAll(): Set<Request>
    fun search(term: String, users: Set<String> = emptySet(), teams: Set<String> = emptySet()): Set<Request>
}