package cmm.apps.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_RETRY_BUTTON
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_TITLE

@Composable
fun EsmorgaFullScreenError(
    title: String,
    buttonText: String,
    buttonAction: () -> Unit
) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.outline_cancel),
                    contentDescription = "Error",
                    modifier = Modifier.size(128.dp)
                )
                EsmorgaText(text = title, style = EsmorgaTextStyle.HEADING_1, textAlign = TextAlign.Center, modifier = Modifier.testTag(ERROR_TITLE))
            }
            EsmorgaButton(
                text = buttonText,
                onClick = buttonAction,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(vertical = innerPadding.calculateBottomPadding(), horizontal = 16.dp)
                    .testTag(ERROR_RETRY_BUTTON)
            )
        }
    }
}

object ErrorScreenTestTags {
    const val ERROR_TITLE = "error screen title"
    const val ERROR_RETRY_BUTTON = "error screen retry button"
}