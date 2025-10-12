package dev.killercavin.sbsecurity

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HomeController {
    @GetMapping("/home")
    fun home(): ResponseEntity<String> = ResponseEntity.status(HttpStatus.OK).body("API is running...")
}