package com.example.familydirectory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.familydirectory.navigation.AppNavigation
import com.example.familydirectory.navigation.Screen
import com.example.familydirectory.ui.components.BottomNavigationBar
import com.example.familydirectory.ui.theme.FamilydirectoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FamilydirectoryTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // Screens that should show bottom navigation
                val screensWithBottomNav = listOf(
                    Screen.Quotes.route,
                    Screen.Search.route,
                    Screen.Events.route
                )

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        // Only show bottom navigation on main screens
                        if (currentRoute in screensWithBottomNav) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    AppNavigation(
                        navController = navController,
                        startDestination = Screen.Search.route,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}