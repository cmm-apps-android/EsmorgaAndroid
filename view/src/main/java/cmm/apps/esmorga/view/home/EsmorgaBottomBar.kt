package cmm.apps.esmorga.view.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.navigation.Navigation

@Composable
fun EsmorgaBottomBar(navigationController: NavHostController, items: List<BottomNavItem>, currentRoute: BottomNavItemRoute?) {
    NavigationBar(
        containerColor = colorScheme.surface,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navigationController.navigate(item.route.navigation) {
                        currentRoute?.let {
                            popUpTo(it.navigation) {
                                inclusive = true
                            }
                        }
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(item.icon),
                        contentDescription = null,
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        tint = if (currentRoute == item.route) colorScheme.onSurface else colorScheme.onSurfaceVariant
                    )
                },
                label = {
                    EsmorgaText(
                        text = stringResource(item.label),
                        style = EsmorgaTextStyle.CAPTION
                    )
                },
                colors = androidx.compose.material3.NavigationBarItemDefaults
                    .colors(
                        selectedIconColor = colorScheme.onSurface,
                        selectedTextColor = colorScheme.onSurface,
                        unselectedIconColor = colorScheme.onSurfaceVariant,
                        unselectedTextColor = colorScheme.onSurfaceVariant,
                        indicatorColor = colorScheme.surface
                    )
            )
        }
    }
}

sealed class BottomNavItem(val route: BottomNavItemRoute, val icon: Int, val label: Int) {
    object Explore : BottomNavItem(BottomNavItemRoute.EVENT_LIST, R.drawable.ic_explore, R.string.bottom_bar_explore)
    object MyEvents : BottomNavItem(BottomNavItemRoute.MY_EVENTS, R.drawable.ic_my_events, R.string.bottom_bar_myevents)
    object Profile : BottomNavItem(BottomNavItemRoute.PROFILE, R.drawable.ic_profile, R.string.bottom_bar_myprofile)
}

enum class BottomNavItemRoute(val screen: String, val navigation: Navigation) {
    EVENT_LIST("EventListScreen", Navigation.EventListScreen), MY_EVENTS("MyEventsScreen", Navigation.MyEventsScreen), PROFILE("ProfileScreen", Navigation.ProfileScreen)
}