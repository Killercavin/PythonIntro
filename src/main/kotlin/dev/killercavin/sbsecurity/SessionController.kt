package dev.killercavin.sbsecurity

import jakarta.servlet.http.HttpServletRequest
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/session")
class SessionController {
    @GetMapping("/details")
    fun sessionDetails(
        request: HttpServletRequest,
        authentication: Authentication
    ): Map<String, Any?> {
        val session = request.getSession(false) // don't create new session if non-existent
        return mapOf(
            "sessionId" to session?.id,
            "isNew" to session?.isNew,
            "creationTime" to session?.creationTime,
            "lastAccessedTime" to session?.lastAccessedTime,
            "authenticatedUser" to authentication.name
        )
    }
}