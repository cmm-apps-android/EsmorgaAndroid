package cmm.apps.designsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun EsmorgaGuestError(errorMessage: String, buttonText: String, onButtonClicked: () -> Unit, animation: Int) {
    val lottieAnimation by rememberLottieComposition(LottieCompositionSpec.RawRes(animation))
    Column(
        modifier = Modifier
            .padding(
                start = 16.dp,
                end = 16.dp
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        LottieAnimation(
            composition = lottieAnimation,
            iterations = Int.MAX_VALUE,
            contentScale = ContentScale.Inside,
            modifier = Modifier.fillMaxHeight(0.3f)
        )
        EsmorgaText(errorMessage, style = EsmorgaTextStyle.HEADING_2)
        Spacer(modifier = Modifier.weight(1f))
        EsmorgaButton(text = buttonText, modifier = Modifier.testTag(GuestErrorTestTags.GUEST_ERROR_PRIMARY_BUTTON)) {
            onButtonClicked.invoke()
        }
    }
}

object GuestErrorTestTags {
    const val GUEST_ERROR_PRIMARY_BUTTON = "guest error primary button"
}