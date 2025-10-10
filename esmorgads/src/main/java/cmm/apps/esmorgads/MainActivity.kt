package cmm.apps.esmorgads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cmm.apps.esmorga.view.theme.EsmorgaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EsmorgaTheme {
                DesignSystemShowcase()
            }
        }
    }
}

