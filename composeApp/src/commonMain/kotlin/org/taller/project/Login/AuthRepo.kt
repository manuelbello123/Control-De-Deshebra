package org.taller.project.Login

interface AuthRepo {
    suspend fun login(
        username: String,
        password: String
    ): AuthResult
}