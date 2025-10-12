package dev.killercavin.sbsecurity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(val user: User) : UserDetails {
    override fun getUsername(): String = user.username
    override fun getPassword(): String = user.hashedPassword
    override fun getAuthorities(): Collection<GrantedAuthority> = listOf(SimpleGrantedAuthority("ROLE_USER"))
    override fun isAccountNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean =true
    override fun isAccountNonLocked(): Boolean = true
}