package com.example.familydirectory.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.familydirectory.ui.admin.AdminAuthScreen
import com.example.familydirectory.ui.admin.AdminUploadScreen
import com.example.familydirectory.ui.detail.FamilyDetailScreen
import com.example.familydirectory.ui.events.EventsScreen
import com.example.familydirectory.ui.quotes.QuotesScreen
import com.example.familydirectory.ui.search.SearchScreen
import com.example.familydirectory.ui.upload.UploadEventScreen

// Navigation Routes
sealed class Screen(val route: String) {
    object Quotes : Screen("quotes")
    object Search : Screen("search")
    object Events : Screen("events")
    object Upload : Screen("upload")
    object AdminAuth : Screen("admin_auth")
    object AdminUpload : Screen("admin_upload")
    object FamilyDetail : Screen("family/{familyId}") {
        fun createRoute(familyId: String) = "family/$familyId"
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Search.route,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // Quotes Screen (Home)
        composable(Screen.Quotes.route) {
            QuotesScreen()
        }

        // Search Screen
        composable(Screen.Search.route) {
            SearchScreen(
                onFamilyClick = { familyId ->
                    navController.navigate(Screen.FamilyDetail.createRoute(familyId))
                },
                onAdminClick = {
                    navController.navigate(Screen.AdminAuth.route)
                }
            )
        }

        // Events Screen
        composable(Screen.Events.route) {
            EventsScreen(
                onNavigateToUpload = {
                    navController.navigate(Screen.Upload.route)
                }
            )
        }

        // Upload Screen
        composable(Screen.Upload.route) {
            UploadEventScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Admin Auth Screen
        composable(Screen.AdminAuth.route) {
            AdminAuthScreen(
                onAuthSuccess = {
                    navController.navigate(Screen.AdminUpload.route) {
                        popUpTo(Screen.AdminAuth.route) { inclusive = true }
                    }
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }

        // Admin Upload Screen
        composable(Screen.AdminUpload.route) {
            AdminUploadScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Family Detail Screen
        composable(
            route = Screen.FamilyDetail.route,
            arguments = listOf(
                navArgument("familyId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val familyId = backStackEntry.arguments?.getString("familyId") ?: return@composable
            FamilyDetailScreen(
                familyId = familyId,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}