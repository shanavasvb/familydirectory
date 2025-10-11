package com.example.familydirectory.ui.search

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import androidx.compose.ui.graphics.graphicsLayer
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
    onFamilyClick: (String) -> Unit,
    onAdminClick: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val showFilterSheet by viewModel.showFilterSheet.collectAsState()
    val filters by viewModel.filters.collectAsState()

    // ✅ State for refresh animation
    var isRefreshing by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (isRefreshing) 360f else 0f,
        animationSpec = tween(durationMillis = 600, easing = LinearEasing),
        finishedListener = { isRefreshing = false }
    )

    val hasActiveFilters = filters.parish != null ||
            filters.region != null ||
            filters.bloodGroup != null ||
            filters.gender != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "കുടുംബ ഡയറക്ടറി",
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Text(
                            "Malikudy Kudumbayogam",
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = PureWhite.copy(alpha = 0.9f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepRoyalBlue
                ),
                actions = {
                    // ✅ Refresh Button with Animation
                    IconButton(
                        onClick = {
                            isRefreshing = true
                            viewModel.refreshFilterOptions()
                        }
                    ) {
                        Icon(
                            Icons.Default.Refresh,
                            "Refresh Filters",
                            tint = PureWhite,
                            modifier = Modifier.graphicsLayer {
                                rotationZ = rotation
                            }
                        )
                    }

                    // Admin Button
                    IconButton(onClick = onAdminClick) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            "Admin",
                            tint = HeritageGold
                        )
                    }

                    // Filter Button with Badge
                    BadgedBox(
                        badge = {
                            if (hasActiveFilters) {
                                Badge(
                                    containerColor = HeritageGold,
                                    contentColor = DeepRoyalBlue
                                ) {
                                    Text(
                                        "${listOfNotNull(
                                            filters.parish,
                                            filters.region,
                                            filters.bloodGroup,
                                            filters.gender
                                        ).size}",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    ) {
                        IconButton(onClick = { viewModel.toggleFilterSheet() }) {
                            Icon(
                                Icons.Default.FilterList,
                                "Filters",
                                tint = PureWhite
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
                .background(SoftGray)
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Decorative Header Border
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    HeritageGold,
                                    WarmTerracotta,
                                    HeritageGold
                                )
                            )
                        )
                )

                // Search Bar with Animation
                AnimatedVisibility(
                    visible = true,
                    enter = slideInVertically(
                        initialOffsetY = { -40 },
                        animationSpec = tween(400, easing = EaseOutCubic)
                    ) + fadeIn()
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .shadow(8.dp, RoundedCornerShape(28.dp)),
                        shape = RoundedCornerShape(28.dp),
                        color = PureWhite
                    ) {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { viewModel.onSearchQueryChange(it) },
                            onClearClick = { viewModel.clearSearch() }
                        )
                    }
                }

                // Active Filters
                AnimatedVisibility(
                    visible = hasActiveFilters,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = PureWhite,
                        shadowElevation = 4.dp
                    ) {
                        ActiveFiltersRow(
                            filters = filters,
                            onClearFilters = { viewModel.clearFilters() },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                        )
                    }
                }

                // Content with Animation
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
                                message = error ?: "തിരയൽ ഫലങ്ങളൊന്നുമില്ല",
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
                                message = "കുടുംബങ്ങളൊന്നും കണ്ടെത്തിയില്ല",
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

            // ✅ Refresh Snackbar
            AnimatedVisibility(
                visible = isRefreshing,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = DeepRoyalBlue,
                    shadowElevation = 8.dp
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = PureWhite,
                            strokeWidth = 2.dp
                        )
                        Column {
                            Text(
                                text = "ഫിൽട്ടറുകൾ പുതുക്കുന്നു...",
                                color = PureWhite,
                                fontWeight = FontWeight.Medium,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = "Refreshing filter options",
                                color = PureWhite.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodySmall
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
                "പേര്, സ്ഥലം, ഫോൺ എന്നിവ തിരയുക...",
                color = TextHint,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            )
        },
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "Search",
                tint = DeepRoyalBlue
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
            focusedBorderColor = DeepRoyalBlue,
            unfocusedBorderColor = LightBorder,
            focusedContainerColor = PureWhite,
            unfocusedContainerColor = PureWhite,
            focusedTextColor = TextDark,
            unfocusedTextColor = TextDark
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
            tint = DeepRoyalBlue,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = "ഫിൽട്ടറുകൾ:",
            style = MaterialTheme.typography.labelMedium,
            color = DeepRoyalBlue,
            fontWeight = FontWeight.Bold
        )

        filters.parish?.let {
            FilterChip(
                selected = true,
                onClick = {},
                label = {
                    Text(
                        it,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DeepRoyalBlue.copy(alpha = 0.15f),
                    selectedLabelColor = DeepRoyalBlue
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = true,
                    borderColor = DeepRoyalBlue.copy(alpha = 0.5f),
                    selectedBorderColor = DeepRoyalBlue,
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.5.dp
                )
            )
        }

        filters.region?.let {
            FilterChip(
                selected = true,
                onClick = {},
                label = {
                    Text(
                        it,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DeepRoyalBlue.copy(alpha = 0.15f),
                    selectedLabelColor = DeepRoyalBlue
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = true,
                    borderColor = DeepRoyalBlue.copy(alpha = 0.5f),
                    selectedBorderColor = DeepRoyalBlue,
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.5.dp
                )
            )
        }

        filters.bloodGroup?.let {
            FilterChip(
                selected = true,
                onClick = {},
                label = {
                    Text(
                        it,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DeepRoyalBlue.copy(alpha = 0.15f),
                    selectedLabelColor = DeepRoyalBlue
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = true,
                    borderColor = DeepRoyalBlue.copy(alpha = 0.5f),
                    selectedBorderColor = DeepRoyalBlue,
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.5.dp
                )
            )
        }

        filters.gender?.let {
            FilterChip(
                selected = true,
                onClick = {},
                label = {
                    Text(
                        it,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DeepRoyalBlue.copy(alpha = 0.15f),
                    selectedLabelColor = DeepRoyalBlue
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = true,
                    borderColor = DeepRoyalBlue.copy(alpha = 0.5f),
                    selectedBorderColor = DeepRoyalBlue,
                    borderWidth = 1.dp,
                    selectedBorderWidth = 1.5.dp
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onClearFilters) {
            Text(
                "മായ്ക്കുക",
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
                color = DeepRoyalBlue.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = DeepRoyalBlue,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${results.size}",
                                color = PureWhite,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "${results.size} കുടുംബം കണ്ടെത്തി",
                            style = MaterialTheme.typography.titleMedium,
                            color = DeepRoyalBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Results Found",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        items(
            items = results,
            key = { it.family.id }
        ) { result ->
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
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with Kerala arch design
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                DeepRoyalBlue,
                                RoyalBlueLight
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = result.family.familyHead,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = HeritageGold.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "📍 ${result.matchedIn}",
                                style = MaterialTheme.typography.bodySmall,
                                color = PureWhite,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = HeritageGold,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = result.family.familyHead.firstOrNull()?.uppercase() ?: "?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepRoyalBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Details Grid
            if (result.family.place.isNotEmpty()) {
                DetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "സ്ഥലം",
                    value = result.family.place
                )
            }

            if (result.family.phone.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow(
                    icon = Icons.Default.Phone,
                    label = "ഫോൺ",
                    value = result.family.phone
                )
            }

            if (result.family.parish.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                DetailRow(
                    icon = Icons.Default.Church,
                    label = "ഇടവക",
                    value = result.family.parish
                )
            }

            // Footer
            if (result.family.familyMembers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(
                    color = LightBorder,
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.People,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = WarmTerracotta
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${result.family.familyMembers.size} അംഗങ്ങൾ",
                            style = MaterialTheme.typography.bodySmall,
                            color = WarmTerracotta,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        Icons.Default.ArrowForward,
                        contentDescription = "View Details",
                        modifier = Modifier.size(18.dp),
                        tint = DeepRoyalBlue
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
            color = DeepRoyalBlue.copy(alpha = 0.1f),
            modifier = Modifier.size(32.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = DeepRoyalBlue
                )
            }
        }

        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = TextDark,
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
            color = DeepRoyalBlue,
            strokeWidth = 3.dp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "തിരയുന്നു...",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = "Searching...",
            style = MaterialTheme.typography.bodySmall,
            color = TextHint
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
            color = DeepRoyalBlue.copy(alpha = 0.1f),
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp),
                    tint = DeepRoyalBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "കുടുംബങ്ങൾ തിരയുക",
            style = MaterialTheme.typography.headlineSmall,
            color = DeepRoyalBlue,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Search Family Directory",
            style = MaterialTheme.typography.titleSmall,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "പേര്, സ്ഥലം അല്ലെങ്കിൽ ഫിൽട്ടറുകൾ ഉപയോഗിക്കുക",
            style = MaterialTheme.typography.bodyMedium,
            color = TextHint,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
            color = TextDark,
            fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "വ്യത്യസ്ത കീവേഡുകൾ അല്ലെങ്കിൽ ഫിൽട്ടറുകൾ ശ്രമിക്കുക",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}