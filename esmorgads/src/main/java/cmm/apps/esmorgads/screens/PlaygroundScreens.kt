package cmm.apps.esmorgads.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cmm.apps.esmorgads.playground.ButtonPlayground
import cmm.apps.esmorgads.playground.DatePickerPlayground
import cmm.apps.esmorgads.playground.DialogPlayground
import cmm.apps.esmorgads.playground.TextFieldPlayground

@Composable
fun ButtonPlaygroundScreen() {
    PlaygroundScreen {
        ButtonPlayground()
    }
}

@Composable
fun TextFieldPlaygroundScreen() {
    PlaygroundScreen {
        TextFieldPlayground()
    }
}

@Composable
fun DialogPlaygroundScreen() {
    PlaygroundScreen {
        DialogPlayground()
    }
}

@Composable
fun DatePickerPlaygroundScreen() {
    PlaygroundScreen {
        DatePickerPlayground()
    }
}

@Composable
private fun PlaygroundScreen(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        content()
    }
}

