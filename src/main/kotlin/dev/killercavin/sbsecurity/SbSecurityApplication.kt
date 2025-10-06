package dev.killercavin.sbsecurity

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SbSecurityApplication

fun main(args: Array<String>) {
    runApplication<SbSecurityApplication>(*args)
}
