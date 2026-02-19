package org.taller.project

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.taller.project.Components.BottomNavigationBar
import org.taller.project.Components.TopBar
import org.taller.project.Navigation.AppNavGraph
import org.taller.project.Navigation.Routes
import kotlin.collections.get

@Composable
fun App() {
    MaterialTheme {
        val navController = rememberNavController()
        val currentBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry.value?.destination?.route
        val showBottomBar = currentRoute in listOf(
            Routes.HOME_WORKER,
            Routes.HISTORY,
            Routes.TOTAL_WEEKLY
        )
        val showTopBar = currentRoute != Routes.LOGIN
        val topBarTitle = Routes.titles[currentRoute] ?:""

        Scaffold(
            containerColor = Color.White,
            topBar = {
                if (showTopBar) {
                    TopBar(title = topBarTitle)
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