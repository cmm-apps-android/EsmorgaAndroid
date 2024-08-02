package cmm.apps.esmorga.view.errors

import androidx.compose.runtime.Composable
import cmm.apps.designsystem.EsmorgaFullScreenError
import cmm.apps.esmorga.view.errors.model.EsmorgaErrorScreenArguments
import cmm.apps.esmorga.view.theme.EsmorgaTheme

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
