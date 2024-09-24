package cmm.apps.esmorga.view.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.R
import cmm.apps.esmorga.view.navigation.Navigation
import cmm.apps.esmorga.view.theme.Lavender
import cmm.apps.esmorga.view.theme.Sepia

@Composable
fun EsmorgaBottomBar(navigationController: NavHostController, items: List<BottomNavItem>, currentRoute: String?) {

    NavigationBar(
        containerColor = Lavender,
    ) {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route.toString(),
                onClick = {
                    navigationController.navigate(item.route) {
                        popUpTo(navigationController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(item.icon),
                        contentDescription = null,
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp),
                        tint = if (currentRoute == item.route.toString()) DarkGray else Sepia
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
                        selectedIconColor = DarkGray,
                        selectedTextColor = DarkGray,
                        unselectedIconColor = Sepia,
                        unselectedTextColor = Sepia,
                        indicatorColor = Lavender
                    )
            )
        }
    }
}

sealed class BottomNavItem(val route: Navigation, val icon: Int, val label: Int) {
    object Explore : BottomNavItem(Navigation.EventListScreen, R.drawable.ic_explore, R.string.bottom_bar_explore)
    object MyEvents : BottomNavItem(Navigation.MyEventsScreen, R.drawable.ic_my_events, R.string.bottom_bar_myevents)
    object Profile : BottomNavItem(Navigation.ProfileScreen, R.drawable.ic_profile, R.string.bottom_bar_myprofile)
}