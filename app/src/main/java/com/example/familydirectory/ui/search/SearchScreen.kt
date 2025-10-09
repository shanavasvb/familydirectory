package com.example.familydirectory.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.familydirectory.data.model.SearchResult
import com.example.familydirectory.ui.theme.*

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
                title = {
                    Text(
                        "Family Directory",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue
                ),
                actions = {
                    BadgedBox(
                        badge = {
                            if (hasActiveFilters) {
                                Badge(
                                    containerColor = AccentOrange,
                                    contentColor = Color.White
                                ) {
                                    Text("${listOfNotNull(
                                        filters.parish,
                                        filters.region,
                                        filters.bloodGroup,
                                        filters.gender
                                    ).size}")
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = { viewModel.toggleFilterSheet() }) {
                            Icon(
                                Icons.Default.FilterList,
                                "Filters",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Search Bar with Shadow
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .shadow(4.dp, RoundedCornerShape(28.dp)),
                    shape = RoundedCornerShape(28.dp),
                    color = Color.White
                ) {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { viewModel.onSearchQueryChange(it) },
                        onClearClick = { viewModel.clearSearch() }
                    )
                }

                // Active Filters
                if (hasActiveFilters) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        ActiveFiltersRow(
                            filters = filters,
                            onClearFilters = { viewModel.clearFilters() },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }

                // Content
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    when {
                        isLoading -> {
                            LoadingState(modifier = Modifier.align(Alignment.Center))
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

                        searchResults.isEmpty() -> {
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
        }

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
    onClearClick: () -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                "Search by name, place, phone...",
                color = TextHint
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = PrimaryBlue
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = onClearClick) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = TextSecondary
                    )
                }
            }
        },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryBlue,
            unfocusedBorderColor = BorderBlue,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary
        ),
        shape = RoundedCornerShape(28.dp)
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
        Icon(
            Icons.Default.FilterAlt,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = "Active:",
            style = MaterialTheme.typography.labelMedium,
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold
        )

        filters.parish?.let {
            AssistChip(
                onClick = {},
                label = { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = SurfaceBlueLight,
                    labelColor = PrimaryBlue
                ),
                border = AssistChipDefaults.assistChipBorder(
                    borderColor = PrimaryBlue.copy(alpha = 0.3f)
                )
            )
        }

        filters.region?.let {
            AssistChip(
                onClick = {},
                label = { Text(it) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = SurfaceBlueLight,
                    labelColor = PrimaryBlue
                ),
                border = AssistChipDefaults.assistChipBorder(
                    borderColor = PrimaryBlue.copy(alpha = 0.3f)
                )
            )
        }

        filters.bloodGroup?.let {
            AssistChip(
                onClick = {},
                label = { Text(it) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = SurfaceBlueLight,
                    labelColor = PrimaryBlue
                ),
                border = AssistChipDefaults.assistChipBorder(
                    borderColor = PrimaryBlue.copy(alpha = 0.3f)
                )
            )
        }

        filters.gender?.let {
            AssistChip(
                onClick = {},
                label = { Text(it) },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = SurfaceBlueLight,
                    labelColor = PrimaryBlue
                ),
                border = AssistChipDefaults.assistChipBorder(
                    borderColor = PrimaryBlue.copy(alpha = 0.3f)
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onClearFilters) {
            Text(
                "Clear",
                color = ErrorRed,
                fontWeight = FontWeight.Medium
            )
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
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = SurfaceBlueLight,
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = PrimaryBlue,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${results.size}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Result${if (results.size != 1) "s" else ""} Found",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
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
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                GradientBlueStart,
                                GradientBlueEnd
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = result.family.familyHead,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Matched: ${result.matchedIn}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details with Icons
            if (result.family.place.isNotEmpty()) {
                DetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "Location",
                    value = result.family.place
                )
            }

            if (result.family.phone.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow(
                    icon = Icons.Default.Phone,
                    label = "Phone",
                    value = result.family.phone
                )
            }

            if (result.family.parish.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow(
                    icon = Icons.Default.Church,
                    label = "Parish",
                    value = result.family.parish
                )
            }

            // Footer
            if (result.family.familyMembers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = DividerLight)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.People,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = SecondaryBlue
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${result.family.familyMembers.size} Family Member${if (result.family.familyMembers.size != 1) "s" else ""}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryBlue,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "View Details",
                        modifier = Modifier.size(18.dp),
                        tint = PrimaryBlue
                    )
                }
            }
        }
    }
}

@Composable
fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = SurfaceBlueLight,
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = PrimaryBlue
                )
            }
        }

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun LoadingState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            color = PrimaryBlue,
            strokeWidth = 3.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Searching...",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun InitialSearchState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = SurfaceBlueLight,
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = PrimaryBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Search Family Directory",
            style = MaterialTheme.typography.headlineSmall,
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Enter name, place, phone or use filters",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
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
        Surface(
            shape = RoundedCornerShape(50),
            color = ErrorRed.copy(alpha = 0.1f),
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.SearchOff,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = ErrorRed
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.titleLarge,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Try different keywords or filters",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}