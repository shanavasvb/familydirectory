package com.example.familydirectory.ui.search

// SearchScreen.kt

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.familydirectory.data.model.Family
import com.example.familydirectory.data.model.SearchResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onFamilyClick: (String) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val showFilterSheet by viewModel.showFilterSheet.collectAsState()
    val filters by viewModel.filters.collectAsState()

    val hasActiveFilters = filters.parish != null ||
            filters.region != null ||
            filters.bloodGroup != null ||
            filters.gender != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Families") },
                actions = {
                    // Filter button with badge
                    BadgedBox(
                        badge = {
                            if (hasActiveFilters) {
                                Badge { Text("•") }
                            }
                        }
                    ) {
                        IconButton(onClick = { viewModel.toggleFilterSheet() }) {
                            Icon(Icons.Default.FilterList, "Filters")
                        }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Search Bar
            SearchBar(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) },
                onClearClick = { viewModel.clearSearch() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            // Active Filters Chips
            if (hasActiveFilters) {
                ActiveFiltersRow(
                    filters = filters,
                    onClearFilters = { viewModel.clearFilters() },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when {
                    isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    error != null && searchResults.isEmpty() -> {
                        EmptySearchState(
                            message = error ?: "No results found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    searchQuery.isBlank() && !hasActiveFilters -> {
                        InitialSearchState(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    searchResults.isEmpty() && (searchQuery.isNotBlank() || hasActiveFilters) -> {
                        EmptySearchState(
                            message = "No families found",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        SearchResultsList(
                            results = searchResults,
                            onFamilyClick = onFamilyClick
                        )
                    }
                }
            }
        }

        // Filter Bottom Sheet
        if (showFilterSheet) {
            FilterBottomSheet(
                viewModel = viewModel,
                onDismiss = { viewModel.closeFilterSheet() }
            )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier,
        placeholder = { Text("Search by name, place, phone...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Search")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                }
            }
        },
        singleLine = true,
        shape = MaterialTheme.shapes.large
    )
}

@Composable
fun ActiveFiltersRow(
    filters: com.example.familydirectory.data.model.SearchFilters,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Filters:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        filters.parish?.let {
            FilterChip(
                selected = true,
                onClick = {},
                label = { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) }
            )
        }

        filters.region?.let {
            FilterChip(
                selected = true,
                onClick = {},
                label = { Text(it) }
            )
        }

        filters.bloodGroup?.let {
            FilterChip(
                selected = true,
                onClick = {},
                label = { Text(it) }
            )
        }

        filters.gender?.let {
            FilterChip(
                selected = true,
                onClick = {},
                label = { Text(it) }
            )
        }

        TextButton(onClick = onClearFilters) {
            Text("Clear All")
        }
    }
}

@Composable
fun SearchResultsList(
    results: List<SearchResult>,
    onFamilyClick: (String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "${results.size} result${if (results.size != 1) "s" else ""} found",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        items(results) { result ->
            SearchResultCard(
                result = result,
                onClick = { onFamilyClick(result.family.id) }
            )
        }
    }
}

@Composable
fun SearchResultCard(
    result: SearchResult,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Family Head
            Text(
                text = result.family.familyHead,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Matched In
            Text(
                text = "Matched in: ${result.matchedIn}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Location
                DetailItem(
                    icon = Icons.Default.LocationOn,
                    text = result.family.place,
                    modifier = Modifier.weight(1f)
                )

                // Phone
                if (result.family.phone.isNotEmpty()) {
                    DetailItem(
                        icon = Icons.Default.Phone,
                        text = result.family.phone,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Parish
            if (result.family.parish.isNotEmpty()) {
                DetailItem(
                    icon = Icons.Default.Church,
                    text = result.family.parish,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Family Members Count
            if (result.family.familyMembers.isNotEmpty()) {
                Text(
                    text = "${result.family.familyMembers.size} family member${if (result.family.familyMembers.size != 1) "s" else ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
fun DetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun InitialSearchState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Search Family Directory",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Search by name, place, phone, or use filters",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun EmptySearchState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}