package com.castmart.authentication.domain

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Version

enum class ROLES {
        ADMIN, AUDIT, CASHIER
}

@Entity
data class Admin (
        @Id @Column(name="id", length=16, unique=true, nullable=false)
        var id: UUID? = UUID.randomUUID(),
        @Version
        var version: Long? = null,
        var username: String?,
        var password: String?,
        var role: String?
)

@Repository
interface AdminRepository:JpaRepository<Admin, UUID> {
        fun findAdminByUsername(username: String?): Admin?
}