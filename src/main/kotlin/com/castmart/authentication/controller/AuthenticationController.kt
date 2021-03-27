package com.castmart.authentication.controller

import com.castmart.authentication.dto.LoginRequest
import com.castmart.authentication.dto.LoginResponse
import com.castmart.authentication.config.security.EtktAdminUserService
import com.castmart.authentication.config.security.JWTUtil
import com.castmart.authentication.domain.EtktAdmin
import com.castmart.authentication.service.AdminUserService
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class AuthenticationController(
    val authenticationManager: AuthenticationManager, val userDetailsService: EtktAdminUserService,
    val jwtUtil: JWTUtil, val adminUserService: AdminUserService
    ) {

    @PostMapping("/authenticate")
    fun login(@RequestBody loginRequest: LoginRequest): ResponseEntity<Any> {
        println("Login attempt: ${loginRequest.username} with password ${loginRequest.password}")
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        val userDetails = this.userDetailsService.loadUserByUsername(loginRequest.username)
        val jwt = this.jwtUtil.generateToken(userDetails)
        return ResponseEntity.ok(LoginResponse(jwt))
    }

    @GetMapping("/logout")
    fun logout(): ResponseEntity<Any> {
        println("Logout")
        return ResponseEntity.ok().build()
    }

    @PostMapping("/create")
    fun execute(@RequestBody userData: LoginRequest): ResponseEntity<Any> {
        val adminUser = this.adminUserService.createAdminUser(userData?.username, userData?.password)
        adminUser.password = "********"
        return ResponseEntity.ok(adminUser)
    }

    @GetMapping("/admins")
    fun getAdminUsers(): ResponseEntity<Any> {
        val admins = adminUserService.getAllAdmins()
        hidePassword(admins)
        return ResponseEntity.ok(admins)
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialsException():ResponseEntity<Any> {
        return ResponseEntity.badRequest().build()
    }

    private fun hidePassword(users: List<EtktAdmin>) {
        users.forEach { user -> user.password = "********"}
    }
}