package com.a2z.app.ui.screen.dashboard


import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.a2z.app.ui.theme.BackgroundColor
import com.a2z.app.ui.theme.PrimaryColorDark
import com.a2z.app.ui.theme.PrimaryColorLight


private sealed class DashboardBottomBarItem(
    val route: String,
    val icon: ImageVector,
    val title: String
) {
    object Dashboard : DashboardBottomBarItem("bottom-home-page", Icons.Default.Home, "Dashboard")
    object Ledger :
        DashboardBottomBarItem("bottom-ledger-page", Icons.Default.Receipt, "Ledger")
}


private val dashboardBottomBarList = listOf(
    DashboardBottomBarItem.Dashboard,
    DashboardBottomBarItem.Ledger,
)

@Composable
fun DashboardBottomBarWidget(navController: NavHostController? = null) {
    BottomNavigation(
        backgroundColor = PrimaryColorLight,
        elevation = 16.dp,
        contentColor = Color.White,

    ) {
        val navBackStackEntry = navController?.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.value?.destination

        dashboardBottomBarList.forEach { screen ->
            val isSelected =
                currentDestination?.hierarchy?.any { it.route == screen.route } ?: false
            BottomNavigationItem(
                selected = isSelected,
                onClick = {
                    navController?.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                label = { Text(text = screen.title, fontWeight = FontWeight.Bold) },
                icon = { Icon(imageVector = screen.icon, contentDescription = null) })
        }


    }
}