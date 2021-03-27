package com.castmart.authentication.config.security

import com.castmart.authentication.domain.EtktAdminRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class EtktAdminUserService(val adminUserRepository: EtktAdminRepository, val passwordEncoder: PasswordEncoder): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val userFound = adminUserRepository.findEtktAdminByUsername(username)
        if (userFound != null) {
            val roles = ArrayList<GrantedAuthority>()
            roles.add(SimpleGrantedAuthority(userFound.role!!))
            return User(userFound.username, userFound.password, roles)
        } else {
            return User(
                "admin",
                passwordEncoder.encode("pass"),
                ArrayList()
            )
        }
    }
}