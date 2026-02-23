package org.taller.project.HomeWorker

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.taller.project.Navigation.Routes

@Composable
fun HomeWorkerScreen(
    navController: NavController,
    viewModel: HomeWorkerViewModel
) {

    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(start = 16.dp, end = 16.dp, top = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Home Worker Screen")
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 96.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.End
        ) {

            AnimatedVisibility(visible = expanded) {

                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.End
                ) {

                    // Agregar Trabajador
                    SmallActionButton(
                        text = "Trabajador",
                        icon = Icons.Outlined.Person,
                        primaryColor = Color(0xFF001427),
                    ) {
                        expanded = false
                        navController.navigate(Routes.ADD_WORKER)
                    }

                    // Agregar Usuario
                    SmallActionButton(
                        text = "Usuario",
                        icon = Icons.Outlined.Badge,
                        primaryColor = Color(0xFF001427)
                    ) {
                        expanded = false
                        navController.navigate(Routes.ADD_USER)
                    }

                    // Agregar Prenda
                    SmallActionButton(
                        text = "Prenda",
                        icon = Icons.Outlined.Checkroom,
                        primaryColor = Color(0xFF001427)
                    ) {
                        expanded = false
                        navController.navigate(Routes.ADD_GARMENT)
                    }
                }
            }
            FloatingActionButton(
                onClick = { expanded = !expanded },
                containerColor = Color(0xFF001427),
                shape = RoundedCornerShape(18.dp),
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(
                    imageVector = if (expanded)
                        Icons.Outlined.Close
                    else
                        Icons.Outlined.Add,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}