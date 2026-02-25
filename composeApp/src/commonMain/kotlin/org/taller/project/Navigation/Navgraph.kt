package org.taller.project.Navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.taller.project.AddGarment.AddGarmentScreen
import org.taller.project.AddUser.AddUserRepository
import org.taller.project.AddUser.AddUserScreen
import org.taller.project.AddUser.AddUserViewModel
import org.taller.project.AddWorker.AddWorkerRepository
import org.taller.project.AddWorker.AddWorkerScreen
import org.taller.project.AddWorker.AddWorkerViewModel
import org.taller.project.History.HistoryRepository
import org.taller.project.History.HistoryScreen
import org.taller.project.History.HistoryViewModel
import org.taller.project.HomeWorker.HomeWorkerRepository
import org.taller.project.HomeWorker.HomeWorkerScreen
import org.taller.project.HomeWorker.HomeWorkerViewModel
import org.taller.project.Login.AuthRepository
import org.taller.project.Login.AuthViewModel
import org.taller.project.Login.InMemorySessionManager
import org.taller.project.Login.LoginScreen
import org.taller.project.Network.NetworkUtils
import org.taller.project.ProductionWorker.ProductionWorkerScreen
import org.taller.project.TotalWeekly.TotalWeeklyRepository
import org.taller.project.TotalWeekly.TotalWeeklyScreen
import org.taller.project.TotalWeekly.TotalWeeklyViewModel


@Composable
fun AppNavGraph(navController: NavHostController) {

    // ── Session Manager (única instancia compartida) ──────────────────
    val sessionManager = remember { InMemorySessionManager() }
    val sessionState by sessionManager.sessionState.collectAsState()

    // ── ViewModels ────────────────────────────────────────────────────
    val authViewModel = remember {
        AuthViewModel(
            authRepository = AuthRepository(),
            sessionManager = sessionManager
        )
    }

    val homeWorkerViewModel = remember {
        HomeWorkerViewModel(
            repository = HomeWorkerRepository(
                client = NetworkUtils.buildHttpClient(sessionManager)
            )
        )
    }

    val historyViewModel = remember {
        HistoryViewModel(
            repository = HistoryRepository(
                client = NetworkUtils.buildHttpClient(sessionManager)
            )
        )
    }

    val addWorkerViewModel = remember {
        AddWorkerViewModel(
            repository = AddWorkerRepository(
                client = NetworkUtils.buildHttpClient(sessionManager)
            )
        )
    }
    val addUserViewModel = remember {
        AddUserViewModel(
            repository = AddUserRepository(
                client = NetworkUtils.buildHttpClient(sessionManager)
            )
        )
    }
    val totalWeeklyViewModel = remember {
        TotalWeeklyViewModel(
            repository = TotalWeeklyRepository(
                client = NetworkUtils.buildHttpClient(sessionManager)
            )
        )
    }

    // ── Navegación automática al login exitoso ────────────────────────
    LaunchedEffect(sessionState.isLoggedIn) {
        if (sessionState.isLoggedIn) {
            navController.navigate(Routes.HOME_WORKER) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        }
    }

    // ── Nav Host ──────────────────────────────────────────────────────
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

        composable(Routes.ADD_WORKER) {
            AddWorkerScreen(navController, addWorkerViewModel)
        }

        composable(Routes.ADD_USER) {
            AddUserScreen(navController, addUserViewModel)
        }

        composable(Routes.ADD_GARMENT) {
            AddGarmentScreen(navController)
        }

        composable(Routes.HISTORY) {
            HistoryScreen(navController, historyViewModel)
        }

        composable(Routes.TOTAL_WEEKLY) {
            TotalWeeklyScreen(navController, totalWeeklyViewModel)
        }
        // Ruta con parámetro para PRODUCTION_WORKER


    }
}