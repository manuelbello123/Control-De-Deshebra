package org.taller.project.Components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import org.taller.project.Navigation.Routes

@Composable
fun BottomNavigationBar(navController: NavController) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF001427)
    ) {
        NavigationBarItem(
            selected = currentRoute == Routes.HOME_WORKER,
            onClick = { navController.navigate(Routes.HOME_WORKER) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Home,
                    contentDescription = null,
                    Modifier.size(25.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color(0xFF90A4AE),
                selectedTextColor = Color.White,
                unselectedTextColor = Color(0xFFB0BEC5),
                indicatorColor = Color(0xFF002F5C)
            )
        )

        NavigationBarItem(
            selected = currentRoute == Routes.HISTORY,
            onClick = { navController.navigate(Routes.HISTORY) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = null,
                    Modifier.size(25.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color(0xFF90A4AE),
                selectedTextColor = Color.White,
                unselectedTextColor = Color(0xFFB0BEC5),
                indicatorColor = Color(0xFF002F5C)
            )
        )
        NavigationBarItem(
            selected = currentRoute == Routes.TOTAL_WEEKLY,
            onClick = { navController.navigate(Routes.TOTAL_WEEKLY) },
            icon = {
                Icon(
                    imageVector = Icons.Outlined.AttachMoney,
                    contentDescription = null,
                    Modifier.size(25.dp)
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color(0xFF90A4AE),
                selectedTextColor = Color.White,
                unselectedTextColor = Color(0xFFB0BEC5),
                indicatorColor = Color(0xFF002F5C)
            )
        )
    }
}