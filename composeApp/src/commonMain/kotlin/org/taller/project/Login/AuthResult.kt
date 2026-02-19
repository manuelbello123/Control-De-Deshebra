package org.taller.project.Login

import org.taller.project.Models.AuthUser

sealed class AuthResult {
    data class Success(val user: AuthUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
}