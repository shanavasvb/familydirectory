@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel = viewModel(),
    onFamilyClick: (String) -> Unit,
    onAdminClick: () -> Unit // ✅ Added admin callback
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
                    // Admin Button
                    IconButton(onClick = onAdminClick) {
                        Icon(
                            Icons.Default.AdminPanelSettings,
                            "Admin",
                            tint = Color.White
                        )
                    }

                    // Filter Button
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