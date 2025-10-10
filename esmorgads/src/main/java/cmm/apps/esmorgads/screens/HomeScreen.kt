package cmm.apps.esmorgads.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorgads.navigation.ShowcaseCategory
import cmm.apps.esmorgads.navigation.ShowcaseScreen

@Composable
fun HomeScreen(
    categories: List<ShowcaseCategory>,
    onNavigate: (ShowcaseScreen) -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }

    val filteredCategories = if (searchQuery.text.isBlank()) {
        categories
    } else {
        categories.mapNotNull { category ->
            val filteredItems = category.items.filter { screen ->
                screen.title.contains(searchQuery.text, ignoreCase = true)
            }
            if (filteredItems.isNotEmpty()) {
                ShowcaseCategory(category.title, filteredItems)
            } else null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            if (filteredCategories.isEmpty()) {
                NoResultsMessage()
            } else {
                filteredCategories.forEach { category ->
                    ExpandableCategorySection(
                        category = category,
                        onItemClick = onNavigate,
                        isInitiallyExpanded = searchQuery.text.isNotBlank()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: TextFieldValue,
    onQueryChange: (TextFieldValue) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            EsmorgaText(
                text = "Search components...",
                style = EsmorgaTextStyle.BODY_1
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        singleLine = true
    )
}

@Composable
private fun ExpandableCategorySection(
    category: ShowcaseCategory,
    onItemClick: (ShowcaseScreen) -> Unit,
    isInitiallyExpanded: Boolean = false
) {
    var expanded by remember { mutableStateOf(isInitiallyExpanded) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column {
            // Category Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    EsmorgaText(
                        text = category.title,
                        style = EsmorgaTextStyle.HEADING_1
                    )
                    EsmorgaText(
                        text = "${category.items.size} items",
                        style = EsmorgaTextStyle.CAPTION
                    )
                }
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "Collapse" else "Expand"
                    )
                }
            }

            // Category Items
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    HorizontalDivider()
                    category.items.forEach { screen ->
                        ComponentListItem(
                            title = screen.title,
                            onClick = { onItemClick(screen) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun ComponentListItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EsmorgaText(
            text = title,
            style = EsmorgaTextStyle.HEADING_2
        )
    }
}

@Composable
private fun NoResultsMessage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EsmorgaText(
            text = "No results found",
            style = EsmorgaTextStyle.HEADING_1
        )
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(
            text = "Try a different search term",
            style = EsmorgaTextStyle.BODY_1
        )
    }
}

