package cmm.apps.esmorga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import cmm.apps.esmorga.ui.theme.EsmorgaTheme

class EventListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel by viewModels<EventListViewModel>()
        lifecycle.addObserver(viewModel)

        enableEdgeToEdge()
        setContent {
            EventListScreen(viewModel())
        }
    }
}

@Composable
fun EventListScreen(elvm: EventListViewModel = viewModel()) {
    val uiState: EventListUiState by elvm.uiState.collectAsStateWithLifecycle()

    EsmorgaTheme {
        Surface(modifier = Modifier.padding(32.dp).fillMaxSize()) {
            EventListView(uiState.eventList)
        }
    }
}

@Composable
fun EventListView(list: List<String>) {
    LazyColumn {
        items(list.size) { pos ->
            Text(text = list[pos])
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EsmorgaTheme {
        EventListView(listOf("Android"))
    }
}