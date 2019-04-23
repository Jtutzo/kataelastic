package com.jtutzo.kataelastic

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.inject.Inject

@RestController
@RequestMapping("/request")
class RequestResource @Inject constructor(private val requestRepository: RequestRepository) {

    @GetMapping("/all")
    fun getAll() = requestRepository.findAll()

    @GetMapping("/{code}")
    fun getByCode(@PathVariable code: String) = requestRepository.findByCode(code)

    @GetMapping("/search/{user}")
    fun searchByUser(@PathVariable user: String) = requestRepository.searchByUser(user)

    @GetMapping("/search/rctcode/{rctCode}")
    fun searchByRctCode(@PathVariable rctCode: String) = requestRepository.searchByRctCode(rctCode)

    @PostMapping
    fun create(@RequestBody request: Request) = ResponseEntity(requestRepository.create(request), CREATED)

    @PutMapping
    fun update(@RequestBody request: Request) = ResponseEntity(requestRepository.update(request), OK)

}