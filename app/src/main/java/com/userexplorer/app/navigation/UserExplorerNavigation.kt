package com.userexplorer.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.userexplorer.feature.users.presentation.common.LoadingScreen
import com.userexplorer.feature.users.presentation.detail.ui.UserDetailScreen
import com.userexplorer.feature.users.presentation.list.contract.UsersContract
import com.userexplorer.feature.users.presentation.list.ui.UsersScreen
import com.userexplorer.feature.users.presentation.list.viewmodel.UsersViewModel

/**
 * Navigation routes for the app
 */
object UserExplorerDestinations {
    const val USERS_GRAPH = "users_graph"
    const val USER_LIST_ROUTE = "users"
    private const val USER_DETAIL = "user_detail"
    const val USER_ID_ARG = "userId"
    const val USER_DETAIL_ROUTE = "$USER_DETAIL/{$USER_ID_ARG}"
    fun userDetail(userId: Int) = "$USER_DETAIL/$userId"
}

/**
 * Main navigation graph for the app.
 * Handles navigation between different screens using a shared ViewModel and route arguments.
 */
@Composable
fun UserExplorerNavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = UserExplorerDestinations.USERS_GRAPH
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        navigation(
            route = UserExplorerDestinations.USERS_GRAPH,
            startDestination = UserExplorerDestinations.USER_LIST_ROUTE
        ) {
            composable(UserExplorerDestinations.USER_LIST_ROUTE) { backStackEntry ->
                val viewModel = rememberUsersViewModel(navController, backStackEntry)

                UsersScreen(
                    viewModel = viewModel,
                    onUserClick = { user ->
                        navController.navigate(UserExplorerDestinations.userDetail(user.id)) {
                            launchSingleTop = true
                        }
                    }
                )
            }

            composable(
                route = UserExplorerDestinations.USER_DETAIL_ROUTE,
                arguments = listOf(
                    navArgument(UserExplorerDestinations.USER_ID_ARG) { type = NavType.IntType }
                )
            ) { backStackEntry ->
                val viewModel = rememberUsersViewModel(navController, backStackEntry)
                val userId = backStackEntry.arguments?.getInt(UserExplorerDestinations.USER_ID_ARG)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                val user = remember(userId, uiState is UsersContract.State.Success) {
                    userId?.let { viewModel.findUser(it) }
                }
                val shouldPopBack = userId == null ||
                    (user == null && uiState !is UsersContract.State.Loading)

                LaunchedEffect(shouldPopBack) {
                    if (shouldPopBack) {
                        navController.popBackStack()
                    }
                }

                when {
                    user != null -> {
                        UserDetailScreen(
                            user = user,
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    userId != null && uiState is UsersContract.State.Loading -> {
                        LoadingScreen()
                    }
                }
            }
        }
    }
}

/**
 * Returns the UsersViewModel scoped to the users navigation graph
 * so it can be shared across list and detail destinations.
 */
@Composable
private fun rememberUsersViewModel(
    navController: NavHostController,
    backStackEntry: NavBackStackEntry,
): UsersViewModel {
    val graphEntry = remember(backStackEntry) {
        navController.getBackStackEntry(UserExplorerDestinations.USERS_GRAPH)
    }
    return hiltViewModel(graphEntry)
}