package cmm.apps.designsystem

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_RETRY_BUTTON
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_SUBTITLE
import cmm.apps.designsystem.ErrorScreenTestTags.ERROR_TITLE
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun EsmorgaFullScreenError(
    showAnimation: Boolean = false,
    title: String,
    subtitle: String? = null,
    buttonText: String,
    buttonAction: () -> Unit
) {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.no_connection_anim)
    )
    Scaffold { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showAnimation) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.Inside,
                        modifier = Modifier.size(150.dp)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.outline_cancel),
                        contentDescription = "Error",
                        modifier = Modifier.size(128.dp)
                    )
                }

                EsmorgaText(text = title, style = EsmorgaTextStyle.HEADING_1, textAlign = TextAlign.Center, modifier = Modifier.testTag(ERROR_TITLE))
                subtitle?.let {
                    EsmorgaText(text = subtitle, style = EsmorgaTextStyle.BODY_1, textAlign = TextAlign.Center, modifier = Modifier
                        .padding(top = 16.dp)
                        .testTag(ERROR_SUBTITLE))
                }

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
    const val ERROR_SUBTITLE = "error screen subtitle"
}