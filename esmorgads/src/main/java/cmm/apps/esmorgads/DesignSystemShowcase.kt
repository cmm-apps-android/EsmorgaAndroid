package cmm.apps.esmorgads

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.esmorgads.navigation.ShowcaseScreen
import cmm.apps.esmorgads.navigation.showcaseCategories
import cmm.apps.esmorgads.screens.ButtonPlaygroundScreen
import cmm.apps.esmorgads.screens.ButtonsScreen
import cmm.apps.esmorgads.screens.CardsScreen
import cmm.apps.esmorgads.screens.ColorsScreen
import cmm.apps.esmorgads.screens.DatePickerPlaygroundScreen
import cmm.apps.esmorgads.screens.DialogPlaygroundScreen
import cmm.apps.esmorgads.screens.HomeScreen
import cmm.apps.esmorgads.screens.LoadersScreen
import cmm.apps.esmorgads.screens.RadioButtonsScreen
import cmm.apps.esmorgads.screens.RowsScreen
import cmm.apps.esmorgads.screens.TextFieldPlaygroundScreen
import cmm.apps.esmorgads.screens.TextFieldsScreen
import cmm.apps.esmorgads.screens.TypographyScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DesignSystemShowcase(forceDarkTheme: Boolean? = null) {
    val navController = rememberNavController()
    val systemInDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(forceDarkTheme ?: systemInDarkTheme) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route ?: ShowcaseScreen.Home.route
    val isHome = currentRoute == ShowcaseScreen.Home.route

    val currentScreen = when (currentRoute) {
        ShowcaseScreen.Home.route -> ShowcaseScreen.Home
        ShowcaseScreen.Buttons.route -> ShowcaseScreen.Buttons
        ShowcaseScreen.TextFields.route -> ShowcaseScreen.TextFields
        ShowcaseScreen.Loaders.route -> ShowcaseScreen.Loaders
        ShowcaseScreen.Cards.route -> ShowcaseScreen.Cards
        ShowcaseScreen.Rows.route -> ShowcaseScreen.Rows
        ShowcaseScreen.RadioButtons.route -> ShowcaseScreen.RadioButtons
        ShowcaseScreen.Typography.route -> ShowcaseScreen.Typography
        ShowcaseScreen.Colors.route -> ShowcaseScreen.Colors
        ShowcaseScreen.ButtonPlayground.route -> ShowcaseScreen.ButtonPlayground
        ShowcaseScreen.TextFieldPlayground.route -> ShowcaseScreen.TextFieldPlayground
        ShowcaseScreen.DialogPlayground.route -> ShowcaseScreen.DialogPlayground
        ShowcaseScreen.DatePickerPlayground.route -> ShowcaseScreen.DatePickerPlayground
        else -> ShowcaseScreen.Home
    }

    EsmorgaTheme(darkTheme = isDarkTheme) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        EsmorgaText(
                            text = currentScreen.title,
                            style = EsmorgaTextStyle.HEADING_2
                        )
                    },
                    navigationIcon = {
                        if (!isHome) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            floatingActionButton = {
                ThemeToggleFab(isDarkTheme = isDarkTheme) {
                    isDarkTheme = !isDarkTheme
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = ShowcaseScreen.Home.route,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(paddingValues)
            ) {
                composable(ShowcaseScreen.Home.route) {
                    HomeScreen(
                        categories = showcaseCategories,
                        onNavigate = { screen -> navController.navigate(screen.route) }
                    )
                }
                composable(ShowcaseScreen.Buttons.route) { ButtonsScreen() }
                composable(ShowcaseScreen.TextFields.route) { TextFieldsScreen() }
                composable(ShowcaseScreen.Loaders.route) { LoadersScreen() }
                composable(ShowcaseScreen.Cards.route) { CardsScreen() }
                composable(ShowcaseScreen.Rows.route) { RowsScreen() }
                composable(ShowcaseScreen.RadioButtons.route) { RadioButtonsScreen() }
                composable(ShowcaseScreen.Typography.route) { TypographyScreen() }
                composable(ShowcaseScreen.Colors.route) { ColorsScreen() }
                composable(ShowcaseScreen.ButtonPlayground.route) { ButtonPlaygroundScreen() }
                composable(ShowcaseScreen.TextFieldPlayground.route) { TextFieldPlaygroundScreen() }
                composable(ShowcaseScreen.DialogPlayground.route) { DialogPlaygroundScreen() }
                composable(ShowcaseScreen.DatePickerPlayground.route) { DatePickerPlaygroundScreen() }
            }
        }
    }
}

@Composable
private fun ThemeToggleFab(isDarkTheme: Boolean, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        Icon(
            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
            contentDescription = "Toggle theme"
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, name = "Light Theme")
@Composable
fun DesignSystemShowcaseLightPreview() {
    DesignSystemShowcase(forceDarkTheme = false)
}

@Preview(showBackground = true, showSystemUi = true, name = "Dark Theme")
@Composable
fun DesignSystemShowcaseDarkPreview() {
    DesignSystemShowcase(forceDarkTheme = true)
}

