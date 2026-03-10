package org.taller.project.Login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginCard(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    onLoginClick: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }  // ⬅️ NUEVO: Estado para visibilidad

    LaunchedEffect(Unit) {
        visible = true
    }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.95f,
        animationSpec = tween(500)
    )

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(500)) +
                slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec = tween(500)
                )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                },
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(
                1.dp,
                Color(0xFF001427).copy(alpha = 0.08f)
            )
        ) {

            Column(
                modifier = Modifier.padding(32.dp),  // ⬅️ MEJORADO: Más padding
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Bienvenido",
                    style = MaterialTheme.typography.headlineMedium,  // ⬅️ MEJORADO: headlineMedium
                    fontWeight = FontWeight.Bold,  // ⬅️ NUEVO: Bold
                    color = Color(0xFF001427)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Inicia sesión para continuar",  // ⬅️ MEJORADO: Texto más claro
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF001427).copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ── Campo Usuario ─────────────────────────────────
                OutlinedTextField(
                    value = username,
                    onValueChange = onUsernameChange,
                    label = { Text("Usuario") },
                    singleLine = true,
                    enabled = !isLoading,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Person,
                            contentDescription = null,
                            tint = Color(0xFF001427)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001427),
                        unfocusedBorderColor = Color(0xFF001427).copy(alpha = 0.3f),
                        cursorColor = Color(0xFF001427),
                        focusedLabelColor = Color(0xFF001427),
                        unfocusedLabelColor = Color(0xFF001427).copy(alpha = 0.6f),
                        focusedTextColor = Color(0xFF001427),
                        unfocusedTextColor = Color(0xFF001427),
                        disabledBorderColor = Color(0xFF001427).copy(alpha = 0.2f),
                        disabledLabelColor = Color(0xFF001427).copy(alpha = 0.4f),
                        disabledLeadingIconColor = Color(0xFF001427).copy(alpha = 0.4f),
                        disabledTextColor = Color(0xFF001427).copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(12.dp),  // ⬅️ NUEVO: Shape
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Campo Contraseña con botón de ojo ─────────────
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = { Text("Contraseña") },
                    singleLine = true,
                    enabled = !isLoading,
                    visualTransformation = if (passwordVisible) {  // ⬅️ MEJORADO
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,  // ⬅️ MEJORADO: Lock en lugar de Password
                            contentDescription = null,
                            tint = Color(0xFF001427)
                        )
                    },
                    trailingIcon = {  // ⬅️ NUEVO: Botón de ojo
                        IconButton(
                            onClick = { passwordVisible = !passwordVisible },
                            enabled = !isLoading
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Outlined.VisibilityOff
                                } else {
                                    Icons.Outlined.Visibility
                                },
                                contentDescription = if (passwordVisible) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                },
                                tint = Color(0xFF001427).copy(
                                    alpha = if (isLoading) 0.4f else 0.6f
                                )
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF001427),
                        unfocusedBorderColor = Color(0xFF001427).copy(alpha = 0.3f),
                        cursorColor = Color(0xFF001427),
                        focusedLabelColor = Color(0xFF001427),
                        unfocusedLabelColor = Color(0xFF001427).copy(alpha = 0.6f),
                        focusedTextColor = Color(0xFF001427),
                        unfocusedTextColor = Color(0xFF001427),
                        disabledBorderColor = Color(0xFF001427).copy(alpha = 0.2f),
                        disabledLabelColor = Color(0xFF001427).copy(alpha = 0.4f),
                        disabledLeadingIconColor = Color(0xFF001427).copy(alpha = 0.4f),
                        disabledTextColor = Color(0xFF001427).copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(12.dp),  // ⬅️ NUEVO: Shape
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ── Botón de Login ────────────────────────────────
                Button(
                    onClick = onLoginClick,
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),  // ⬅️ MEJORADO: Más alto
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF001427),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF001427).copy(alpha = 0.5f),
                        disabledContentColor = Color.White.copy(alpha = 0.7f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 4.dp
                    )
                ) {

                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.5.dp,
                            color = Color.White
                        )
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Iniciar sesión",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}