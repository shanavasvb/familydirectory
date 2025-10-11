package com.example.familydirectory.ui.events

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.familydirectory.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    viewModel: EventsViewModel = viewModel(),
    onNavigateToUpload: () -> Unit
) {
    val events by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val deleteStatus by viewModel.deleteStatus.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(deleteStatus) {
        when (deleteStatus) {
            is DeleteStatus.Success -> {
                snackbarHostState.showSnackbar(
                    message = "ഇവന്റ് വിജയകരമായി ഇല്ലാതാക്കി",
                    duration = SnackbarDuration.Short
                )
                viewModel.resetDeleteStatus()
            }
            is DeleteStatus.Error -> {
                snackbarHostState.showSnackbar(
                    message = (deleteStatus as DeleteStatus.Error).message,
                    duration = SnackbarDuration.Long
                )
                viewModel.resetDeleteStatus()
            }
            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "ഇവന്റ് ഗാലറി",
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Text(
                            "Events Gallery",
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = PureWhite.copy(alpha = 0.9f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepRoyalBlue
                ),
                actions = {
                    IconButton(onClick = { viewModel.loadEvents() }) {
                        Icon(
                            Icons.Default.Refresh,
                            "Refresh",
                            tint = HeritageGold
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNavigateToUpload,
                containerColor = HeritageGold,
                contentColor = DeepRoyalBlue,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
            ) {
                Icon(Icons.Default.Add, "Upload Event")
                Spacer(modifier = Modifier.width(8.dp))
                Text("അപ്‌ലോഡ്", fontWeight = FontWeight.Bold)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SoftGray)
                .padding(padding)
        ) {
            // Decorative header border
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

            when {
                isLoading -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = DeepRoyalBlue,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "ലോഡ് ചെയ്യുന്നു...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                events.isEmpty() -> {
                    EmptyEventsState(
                        onUploadClick = onNavigateToUpload,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            items = events,
                            key = { it.id }
                        ) { event ->
                            EventCard(
                                event = event,
                                onLikeClick = { viewModel.toggleLike(event.id) },
                                onDeleteClick = { viewModel.deleteEvent(event.id) },
                                isDeleting = deleteStatus is DeleteStatus.Deleting
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventCard(
    event: com.example.familydirectory.data.repository.Event,
    onLikeClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isDeleting: Boolean
) {
    var isLiked by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = null,
                    tint = ErrorRed,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "ഇവന്റ് ഇല്ലാതാക്കണോ?",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "\"${event.title}\" ഇല്ലാതാക്കണമെന്ന് ഉറപ്പാണോ? ഈ പ്രവർത്തനം പഴയപടിയാക്കാൻ കഴിയില്ല.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Are you sure you want to delete this event? This action cannot be undone.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClick()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ErrorRed
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ഇല്ലാതാക്കുക")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("റദ്ദാക്കുക")
                }
            },
            containerColor = PureWhite,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {
            if (event.images.isNotEmpty()) {
                Box {
                    ImageCarousel(
                        images = event.images.map { it.url },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                    )

                    // Delete button overlay
                    Surface(
                        onClick = { showDeleteDialog = true },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(12.dp)
                            .size(44.dp),
                        shape = CircleShape,
                        color = ErrorRed,
                        shadowElevation = 6.dp,
                        enabled = !isDeleting
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (isDeleting) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = PureWhite,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = PureWhite,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Title with Kerala arch design
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
                    Text(
                        text = event.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Date
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = DeepRoyalBlue.copy(alpha = 0.1f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = DeepRoyalBlue
                            )
                        }
                    }
                    Text(
                        text = formatDate(event.createdAt),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }

                if (event.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = event.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextDark,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = LightBorder, thickness = 1.dp)

                Spacer(modifier = Modifier.height(16.dp))

                // Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Like button
                    Surface(
                        onClick = {
                            isLiked = !isLiked
                            onLikeClick()
                        },
                        shape = RoundedCornerShape(20.dp),
                        color = if (isLiked) ErrorRed.copy(alpha = 0.1f) else DeepRoyalBlue.copy(alpha = 0.1f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isLiked) ErrorRed else DeepRoyalBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "${event.likesCount + if (isLiked) 1 else 0}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isLiked) ErrorRed else DeepRoyalBlue
                            )
                        }
                    }

                    // Photos count
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = HeritageGold.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = WarmTerracotta
                            )
                            Text(
                                text = "${event.images.size} ഫോട്ടോ",
                                style = MaterialTheme.typography.bodySmall,
                                color = WarmTerracotta,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageCarousel(
    images: List<String>,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = "Event image ${page + 1}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        // Page indicators
        if (images.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(images.size) { index ->
                    Surface(
                        shape = CircleShape,
                        color = if (pagerState.currentPage == index)
                            HeritageGold
                        else
                            PureWhite.copy(alpha = 0.5f),
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(if (pagerState.currentPage == index) 10.dp else 8.dp)
                    ) {}
                }
            }
        }

        // Page counter
        if (images.size > 1) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                color = DeepRoyalBlue.copy(alpha = 0.9f)
            ) {
                Text(
                    text = "${pagerState.currentPage + 1}/${images.size}",
                    style = MaterialTheme.typography.labelMedium,
                    color = PureWhite,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun EmptyEventsState(
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = RoundedCornerShape(50),
            color = DeepRoyalBlue.copy(alpha = 0.1f),
            modifier = Modifier.size(140.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = null,
                    modifier = Modifier.size(70.dp),
                    tint = DeepRoyalBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "ഇവന്റുകളൊന്നുമില്ല",
            style = MaterialTheme.typography.headlineMedium,
            color = DeepRoyalBlue,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "No Events Yet",
            style = MaterialTheme.typography.titleMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "കമ്മ്യൂണിറ്റിയുമായി നിങ്ങളുടെ ആദ്യത്തെ ഇവന്റ് ഫോട്ടോ പങ്കിടുക",
            style = MaterialTheme.typography.bodyMedium,
            color = TextHint,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onUploadClick,
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = HeritageGold,
                contentColor = DeepRoyalBlue
            ),
            elevation = ButtonDefaults.buttonElevation(6.dp),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ഇവന്റ് അപ്‌ലോഡ് ചെയ്യുക", fontWeight = FontWeight.Bold)
        }
    }
}

fun formatDate(date: Date): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
    return sdf.format(date)
}