package cmm.apps.esmorga.view.viewmodel.mock

import cmm.apps.esmorga.view.home.BottomNavItem

object BottomBarMock {

    fun provideBottomNavItems(): List<BottomNavItem> {
        return listOf(
            BottomNavItem.Explore,
            BottomNavItem.MyEvents,
            BottomNavItem.Profile
        )
    }
}