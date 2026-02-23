package org.taller.project.AddUser

import org.taller.project.Models.UsuarioDto

data class UserState(
    val isLoading: Boolean = false,
    val usuarios: List<UsuarioDto> = emptyList(),
    val error: String? = null,
    val successMessage: String? = null,
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false
)