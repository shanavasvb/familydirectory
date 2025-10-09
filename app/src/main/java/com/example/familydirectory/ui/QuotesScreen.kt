package com.example.familydirectory.ui.quotes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.familydirectory.ui.theme.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

// Quote Data Model
data class Quote(
    val id: String = "",
    val text: String = "",
    val author: String = "",
    val category: String = "",
    val date: Date = Date()
)

// QuotesViewModel
class QuotesViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    private val _quotes = MutableStateFlow<List<Quote>>(emptyList())
    val quotes: StateFlow<List<Quote>> = _quotes.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedCategory = MutableStateFlow("all")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    init {
        loadQuotes()
    }

    fun loadQuotes() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val snapshot = firestore.collection("quotes")
                    .get()
                    .await()

                val quotesList = snapshot.documents.mapNotNull { doc ->
                    try {
                        Quote(
                            id = doc.id,
                            text = doc.getString("text") ?: "",
                            author = doc.getString("author") ?: "",
                            category = doc.getString("category") ?: "",
                            date = doc.getDate("date") ?: Date()
                        )
                    } catch (e: Exception) {
                        null
                    }
                }

                _quotes.value = quotesList.shuffled()
                _isLoading.value = false
            } catch (e: Exception) {
                _quotes.value = emptyList()
                _isLoading.value = false
            }
        }
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun getFilteredQuotes(): List<Quote> {
        return if (_selectedCategory.value == "all") {
            _quotes.value
        } else {
            _quotes.value.filter { it.category == _selectedCategory.value }
        }
    }

    fun getCategories(): List<String> {
        return listOf("all") + _quotes.value.map { it.category }.distinct().sorted()
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun QuotesScreen(
    viewModel: QuotesViewModel = viewModel()
) {
    val quotes by viewModel.quotes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val filteredQuotes = remember(quotes, selectedCategory) {
        viewModel.getFilteredQuotes()
    }

    val categories = remember(quotes) {
        viewModel.getCategories()
    }

    val pagerState = rememberPagerState(pageCount = { filteredQuotes.size })

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Daily Inspiration",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(Date()),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue
                ),
                actions = {
                    IconButton(onClick = { viewModel.loadQuotes() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SurfaceBlueLight,
                            BackgroundWhite
                        )
                    )
                )
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = PrimaryBlue,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "Loading inspiration...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                filteredQuotes.isEmpty() -> {
                    EmptyQuotesState(
                        modifier = Modifier.align(Alignment.Center),
                        onRefresh = { viewModel.loadQuotes() }
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Category Filter
                        if (categories.size > 1) {
                            Surface(
                                modifier = Modifier.fillMaxWidth(),
                                color = Color.White,
                                shadowElevation = 2.dp
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    categories.forEach { category ->
                                        FilterChip(
                                            selected = category == selectedCategory,
                                            onClick = { viewModel.selectCategory(category) },
                                            label = {
                                                Text(
                                                    category.replaceFirstChar { it.uppercase() },
                                                    fontWeight = if (category == selectedCategory)
                                                        FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = PrimaryBlue,
                                                selectedLabelColor = Color.White,
                                                containerColor = SurfaceBlueLight,
                                                labelColor = PrimaryBlue
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        // Quote Pager
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                        ) {
                            HorizontalPager(
                                state = pagerState,
                                modifier = Modifier.fillMaxSize()
                            ) { page ->
                                QuoteCard(
                                    quote = filteredQuotes[page],
                                    currentPage = page + 1,
                                    totalPages = filteredQuotes.size
                                )
                            }
                        }

                        // Page Indicator
                        if (filteredQuotes.size > 1) {
                            PageIndicator(
                                currentPage = pagerState.currentPage,
                                totalPages = filteredQuotes.size,
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuoteCard(
    quote: Quote,
    currentPage: Int,
    totalPages: Int
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                SurfaceBlueLight.copy(alpha = 0.4f),
                                Color.White
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Quote Icon
                    Surface(
                        shape = CircleShape,
                        color = PrimaryBlue,
                        modifier = Modifier.size(70.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.FormatQuote,
                                contentDescription = null,
                                modifier = Modifier.size(36.dp),
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Quote Text
                    Text(
                        text = "\"${quote.text}\"",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Medium,
                        lineHeight = MaterialTheme.typography.headlineSmall.lineHeight.times(1.2f)
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Author Badge
                    Surface(
                        shape = RoundedCornerShape(24.dp),
                        color = PrimaryBlue
                    ) {
                        Text(
                            text = "— ${quote.author}",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Category Badge
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SurfaceBlueLight
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.Category,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = PrimaryBlue
                            )
                            Text(
                                text = quote.category.replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelMedium,
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // Quote Counter
                    Text(
                        text = "Quote $currentPage of $totalPages",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Swipe Hint
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.SwipeLeft,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = PrimaryBlue
                        )
                        Text(
                            text = "Swipe for more",
                            style = MaterialTheme.typography.bodySmall,
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            Icons.Default.SwipeRight,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = PrimaryBlue
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PageIndicator(
    currentPage: Int,
    totalPages: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(minOf(totalPages, 10)) { index ->
            Surface(
                shape = CircleShape,
                color = if (currentPage == index)
                    PrimaryBlue
                else
                    BorderBlue,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(if (currentPage == index) 12.dp else 8.dp)
            ) {}
        }
    }
}

@Composable
fun EmptyQuotesState(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = ErrorRed.copy(alpha = 0.1f),
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    Icons.Default.ErrorOutline,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = ErrorRed
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "No Quotes Available",
            style = MaterialTheme.typography.headlineSmall,
            color = ErrorRed,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Unable to load inspirational quotes",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRefresh,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Try Again", fontWeight = FontWeight.Bold)
        }
    }
}