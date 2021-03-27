package com.castmart.authentication.service

import com.castmart.authentication.domain.Admin
import com.castmart.authentication.domain.AdminRepository
import com.castmart.authentication.domain.ROLES
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AdminUserService (private val passwordEncoder: PasswordEncoder, private val adminRepository: AdminRepository){

    fun createAdminUser(username: String, password: String): Admin {
        val newUser = Admin(
            username=username,
            password=this.passwordEncoder.encode(password),
            role= ROLES.ADMIN.name
        )
        adminRepository.save(newUser)
        return newUser
    }

    fun getAllAdmins(): List<Admin> {
        return adminRepository.findAll()
    }
}