package cmm.apps.esmorga.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EventListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModel<EventListViewModel>()
        lifecycle.addObserver(viewModel)

        enableEdgeToEdge()
        setContent {
            EventListScreen()
        }
    }
}

@Composable
fun EventListScreen(elvm: EventListViewModel = koinViewModel()) {
    val uiState: EventListUiState by elvm.uiState.collectAsStateWithLifecycle()

    EsmorgaTheme {
        Surface(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxSize()
        ) {
            EventListView(uiState)
        }
    }
}

@Composable
fun EventListView(uiState: EventListUiState) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.loading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(64.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        } else {
            if (uiState.error.isNullOrBlank().not()) {
                Text(color = Color.Red, text = uiState.error.orEmpty())
            } else {
                LazyColumn {
                    items(uiState.eventList.size) { pos ->
                        Text(text = uiState.eventList[pos])
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventListPreview() {
    EsmorgaTheme {
        EventListView(EventListUiState(false, listOf("Android")))
    }
}