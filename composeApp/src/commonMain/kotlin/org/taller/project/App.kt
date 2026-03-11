package org.taller.project

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.taller.project.Components.AppModule
import org.taller.project.Components.BottomNavigationBar
import org.taller.project.Components.ScreenHeader
import org.taller.project.Components.TopBar
import org.taller.project.Login.UserRole
import org.taller.project.Navigation.AppNavGraph
import org.taller.project.Navigation.Routes
import kotlin.collections.get

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val currentBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry.value?.destination?.route

        // ⬇️⬇️⬇️ AGREGAR ESTAS 3 LÍNEAS ⬇️⬇️⬇️
        val sessionState by AppModule.sessionManager.sessionState.collectAsState()
        val isAdmin = sessionState.user?.rol == UserRole.ADMIN

        // ⬇️⬇️⬇️ MODIFICAR ESTA LÍNEA - Agregar "isAdmin &&" ⬇️⬇️⬇️
        val showBottomBar = isAdmin && currentRoute in listOf(
            Routes.HOME_WORKER,
            Routes.HISTORY,
            Routes.TOTAL_WEEKLY,
        )

        val showTopBar = Routes.headers.containsKey(currentRoute)
        val headerData = Routes.headers[currentRoute]

        Scaffold(
            containerColor = Color.White,
            topBar = {
                if (showTopBar && headerData != null) {
                    TopBar {
                        ScreenHeader(headerData, navController)
                    }
                }
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(navController)
                }
            }
        ) {
            AppNavGraph(navController)
        }
    }
}