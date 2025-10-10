package cmm.apps.esmorgads.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cmm.apps.esmorgads.components.ButtonShowcase
import cmm.apps.esmorgads.components.CardShowcase
import cmm.apps.esmorgads.components.LoaderShowcase
import cmm.apps.esmorgads.components.RadioButtonShowcase
import cmm.apps.esmorgads.components.RowShowcase
import cmm.apps.esmorgads.components.TextFieldShowcase

@Composable
fun ButtonsScreen() {
    DetailScreen {
        ButtonShowcase()
    }
}

@Composable
fun TextFieldsScreen() {
    DetailScreen {
        TextFieldShowcase()
    }
}

@Composable
fun LoadersScreen() {
    DetailScreen {
        LoaderShowcase()
    }
}

@Composable
fun CardsScreen() {
    DetailScreen {
        CardShowcase()
    }
}

@Composable
fun RowsScreen() {
    DetailScreen {
        RowShowcase()
    }
}

@Composable
fun RadioButtonsScreen() {
    DetailScreen {
        RadioButtonShowcase()
    }
}

@Composable
private fun DetailScreen(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        content()
    }
}

