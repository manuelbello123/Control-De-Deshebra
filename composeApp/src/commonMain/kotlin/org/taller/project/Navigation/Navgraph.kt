package org.taller.project.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.taller.project.History.HistoryRepository
import org.taller.project.History.HistoryScreen
import org.taller.project.History.HistoryViewModel
import org.taller.project.HomeWorker.HomeWorkerScreen
import org.taller.project.HomeWorker.HomeWorkerViewModel
import org.taller.project.Login.AuthRepository
import org.taller.project.Login.AuthViewModel
import org.taller.project.Login.InMemorySessionManager
import org.taller.project.Login.LoginScreen
import org.taller.project.Network.NetworkUtils
import org.taller.project.TotalWeekly.TotalWeeklyScreen

@Composable
fun AppNavGraph(navController: NavHostController) {

    val sessionManager = remember { InMemorySessionManager() }

    val authViewModel = remember {
        AuthViewModel(
            authRepository = AuthRepository(),
            sessionManager = sessionManager
        )
    }

    val homeWorkerViewModel = remember {
        HomeWorkerViewModel(sessionManager)
    }

    val sessionState by sessionManager.sessionState.collectAsState()

    LaunchedEffect(sessionState.isLoggedIn) {
        if (sessionState.isLoggedIn) {
            navController.navigate(Routes.HOME_WORKER) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }
    val historyViewModel = remember {
        HistoryViewModel(
            sessionManager = sessionManager,
            repository = HistoryRepository(
                client = NetworkUtils.buildHttpClient(sessionManager)
            )
        )
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(navController, authViewModel)
        }

        composable(Routes.HOME_WORKER) {
            HomeWorkerScreen(navController, homeWorkerViewModel)
        }
        composable(Routes.HISTORY) {
            HistoryScreen(navController, historyViewModel)
        }
        composable(Routes.TOTAL_WEEKLY) {
            TotalWeeklyScreen(navController)
        }
    }
}
