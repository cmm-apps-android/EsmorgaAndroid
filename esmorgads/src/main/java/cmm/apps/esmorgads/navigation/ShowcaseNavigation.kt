package cmm.apps.esmorgads.navigation

sealed class ShowcaseScreen(val route: String, val title: String) {
    data object Home : ShowcaseScreen("home", "Design System")
    data object Buttons : ShowcaseScreen("buttons", "Buttons")
    data object TextFields : ShowcaseScreen("textfields", "Text Fields")
    data object Loaders : ShowcaseScreen("loaders", "Loaders")
    data object Cards : ShowcaseScreen("cards", "Cards")
    data object Rows : ShowcaseScreen("rows", "Rows")
    data object RadioButtons : ShowcaseScreen("radiobuttons", "Radio Buttons")
    data object Typography : ShowcaseScreen("typography", "Typography")
    data object Colors : ShowcaseScreen("colors", "Colors")
    data object ButtonPlayground : ShowcaseScreen("playground_button", "Button Playground")
    data object TextFieldPlayground : ShowcaseScreen("playground_textfield", "Text Field Playground")
    data object DialogPlayground : ShowcaseScreen("playground_dialog", "Dialog Playground")
    data object DatePickerPlayground : ShowcaseScreen("playground_datepicker", "Date Picker Playground")
}

data class ShowcaseCategory(
    val title: String,
    val items: List<ShowcaseScreen>
)

val showcaseCategories = listOf(
    ShowcaseCategory(
        title = "Components",
        items = listOf(
            ShowcaseScreen.Buttons,
            ShowcaseScreen.TextFields,
            ShowcaseScreen.Loaders,
            ShowcaseScreen.Cards,
            ShowcaseScreen.Rows,
            ShowcaseScreen.RadioButtons
        )
    ),
    ShowcaseCategory(
        title = "Theme",
        items = listOf(
            ShowcaseScreen.Typography,
            ShowcaseScreen.Colors
        )
    ),
    ShowcaseCategory(
        title = "Playgrounds",
        items = listOf(
            ShowcaseScreen.ButtonPlayground,
            ShowcaseScreen.TextFieldPlayground,
            ShowcaseScreen.DialogPlayground,
            ShowcaseScreen.DatePickerPlayground
        )
    )
)

