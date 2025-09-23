package cmm.apps.designsystem

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EsmorgaDatePicker(state: DatePickerState) {
    DatePicker(
        modifier = Modifier.padding(top = 16.dp),
        state = state,
        showModeToggle = false,
        title = null,
        headline = null,
        colors = DatePickerDefaults.colors().copy(containerColor = MaterialTheme.colorScheme.background),
    )

}

@OptIn(ExperimentalMaterial3Api::class)
object PossibleSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val dateFromPicker = Instant.ofEpochMilli(utcTimeMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()

        val today = LocalDate.now(ZoneId.systemDefault())

        return !dateFromPicker.isBefore(today)
    }
}