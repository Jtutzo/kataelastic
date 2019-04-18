package com.jtutzo.kataelastic

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.inject.Inject

@RestController
@RequestMapping("/request")
class RequestResource @Inject constructor(private val requestService: RequestService) {

    @PostMapping
    fun create(@RequestBody request: Request) = ResponseEntity(requestService.create(request), HttpStatus.CREATED)

}