package com.jtutzo.kataelastic


data class Request(val code: String, val rctCode: String, val dealName: String, val users: Set<String>, val teams: Set<String>)

interface RequestRepository {
    fun create(request: Request): String
    fun update(request: Request): String
    fun findByCode(code: String): Request
}
