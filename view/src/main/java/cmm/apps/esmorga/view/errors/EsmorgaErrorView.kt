package cmm.apps.esmorga.view.errors

import androidx.compose.runtime.Composable
import cmm.apps.designsystem.EsmorgaFullScreenError
import cmm.apps.esmorga.view.Screen
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.theme.EsmorgaTheme

@Screen
@Composable
fun EsmorgaErrorScreen(
    esmorgaErrorScreenArguments: EsmorgaErrorScreenArguments,
    onButtonPressed: () -> Unit,
) {
    EsmorgaTheme {
        EsmorgaFullScreenError(
            title = esmorgaErrorScreenArguments.title,
            buttonText = esmorgaErrorScreenArguments.buttonText,
            buttonAction = onButtonPressed
        )
    }
}
