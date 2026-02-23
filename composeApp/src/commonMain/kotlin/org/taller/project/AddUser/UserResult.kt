package org.taller.project.AddUser

import org.taller.project.Models.UsuarioDto

sealed class UserResult {
    data class Success(val usuario: UsuarioDto) : UserResult()
    data class Error(val message: String) : UserResult()
}

sealed class UsersListResult {
    data class Success(val usuarios: List<UsuarioDto>) : UsersListResult()
    data class Error(val message: String) : UsersListResult()
}

sealed class UpdateUserResult {
    object Success : UpdateUserResult()
    data class Error(val message: String) : UpdateUserResult()
}

sealed class DeleteUserResult {
    object Success : DeleteUserResult()
    data class Error(val message: String) : DeleteUserResult()
}