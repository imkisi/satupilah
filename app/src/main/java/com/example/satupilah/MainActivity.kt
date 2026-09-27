package com.example.satupilah

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.satupilah.ui.screens.staff.AdminScreen
import com.example.satupilah.ui.screens.category.CategoryScreen
import com.example.satupilah.ui.screens.history.HistoryScreen
import com.example.satupilah.ui.screens.home.HomeScreen
import com.example.satupilah.ui.screens.user.UserScreen

val PrimaryGreen = Color(0xFF005D52)
val SoftGreenBg = Color(0xFFE6F3F1)

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Beranda", Icons.Default.Home)
    object Users : Screen("users", "Pelanggan", Icons.Default.Person)
    object History : Screen("history", "Riwayat", Icons.Default.History)
    object Admin : Screen("admin", "Daftar Admin", Icons.Default.Person)
    object Category : Screen("category", "Daftar Sampah", Icons.Default.Home)
}

@Composable
fun SatupilahTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> darkColorScheme(primary = PrimaryGreen)
        else -> lightColorScheme(primary = PrimaryGreen)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = android.graphics.Color.TRANSPARENT,
                darkScrim = android.graphics.Color.TRANSPARENT
            )
        )
        setContent {
            SatupilahTheme {
                SatupilahMainScreen()
            }
        }
    }
}

@Composable
fun SatupilahMainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavScreens = listOf(Screen.Home, Screen.Users, Screen.History)

    Scaffold(
        bottomBar = {
            if (currentRoute in bottomNavScreens.map { it.route }) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                    bottomNavScreens.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title) },
                            selected = currentRoute == screen.route,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Users.route) { UserScreen() }
            composable(Screen.History.route) { HistoryScreen() }
            composable(Screen.Admin.route) { AdminScreen(navController) }
            composable(Screen.Category.route) { CategoryScreen(navController) }
        }
    }
}
