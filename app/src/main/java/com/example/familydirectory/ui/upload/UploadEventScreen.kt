package com.example.familydirectory.ui.upload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.familydirectory.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadEventScreen(
    viewModel: UploadEventViewModel = viewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedImages by viewModel.selectedImages.collectAsState()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        if (uris.isNotEmpty()) {
            viewModel.addImages(uris)
        }
    }

    LaunchedEffect(uiState) {
        if (uiState is UploadUiState.Success) {
            onNavigateBack()
            viewModel.resetState()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "ഇവന്റ് അപ്‌ലോഡ്",
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Text(
                            "Upload Event",
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = PureWhite.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.Close,
                            "Close",
                            tint = PureWhite
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            viewModel.uploadEvent(
                                title = title,
                                description = description
                            )
                        },
                        enabled = uiState !is UploadUiState.Uploading &&
                                title.isNotBlank() &&
                                selectedImages.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HeritageGold,
                            contentColor = PureWhite,
                            disabledContainerColor = PureWhite.copy(alpha = 0.3f),
                            disabledContentColor = PureWhite.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            Icons.Default.CloudUpload,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("പോസ്റ്റ്", fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepRoyalBlue
                )
            )
        }
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title Input Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = PureWhite
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = DeepRoyalBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Title,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = DeepRoyalBlue
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "ഇവന്റ് ശീർഷകം",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepRoyalBlue
                                )
                                Text(
                                    text = "Event Title",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = {
                                Text("ഇവന്റ് പേര് നൽകുക *", color = TextHint)
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            enabled = uiState !is UploadUiState.Uploading,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepRoyalBlue,
                                unfocusedBorderColor = LightBorder,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                // Description Input Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = PureWhite
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = DeepRoyalBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Description,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = DeepRoyalBlue
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "വിവരണം",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepRoyalBlue
                                )
                                Text(
                                    text = "Description (Optional)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = {
                                Text("ഇവന്റിനെക്കുറിച്ചുള്ള വിവരണം...", color = TextHint)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            maxLines = 5,
                            enabled = uiState !is UploadUiState.Uploading,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepRoyalBlue,
                                unfocusedBorderColor = LightBorder,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                // Add Images Button
                Button(
                    onClick = { imagePickerLauncher.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    enabled = uiState !is UploadUiState.Uploading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedImages.isEmpty()) DeepRoyalBlue else WarmTerracotta,
                        contentColor = PureWhite
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Icon(
                        if (selectedImages.isEmpty()) Icons.Default.AddPhotoAlternate else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            if (selectedImages.isEmpty()) "ചിത്രങ്ങൾ തിരഞ്ഞെടുക്കുക" else "കൂടുതൽ ചിത്രങ്ങൾ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (selectedImages.isNotEmpty()) {
                            Text(
                                "${selectedImages.size} images selected",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                // Selected Images Grid
                if (selectedImages.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = PureWhite
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = DeepRoyalBlue.copy(alpha = 0.1f),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.PhotoLibrary,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp),
                                            tint = DeepRoyalBlue
                                        )
                                    }
                                }
                                Column {
                                    Text(
                                        text = "തിരഞ്ഞെടുത്ത ചിത്രങ്ങൾ",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepRoyalBlue
                                    )
                                    Text(
                                        text = "${selectedImages.size} images",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(selectedImages) { uri ->
                                    ImagePreviewItem(
                                        uri = uri,
                                        onRemove = { viewModel.removeImage(uri) },
                                        enabled = uiState !is UploadUiState.Uploading
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        colors = CardDefaults.cardColors(
                            containerColor = PureWhite
                        ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = DeepRoyalBlue.copy(alpha = 0.1f),
                                    modifier = Modifier.size(100.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            Icons.Default.PhotoLibrary,
                                            contentDescription = null,
                                            modifier = Modifier.size(50.dp),
                                            tint = DeepRoyalBlue
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = "ചിത്രങ്ങളൊന്നും തിരഞ്ഞെടുത്തിട്ടില്ല",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextDark,
                                    fontWeight = FontWeight.Medium
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "No images selected",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                // Upload Status
                when (val state = uiState) {
                    is UploadUiState.Uploading -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = DeepRoyalBlue.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(24.dp),
                                        color = DeepRoyalBlue,
                                        strokeWidth = 3.dp
                                    )
                                    Column {
                                        Text(
                                            text = "അപ്‌ലോഡ് ചെയ്യുന്നു...",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = DeepRoyalBlue,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = "Uploading your event",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(6.dp)
                                        .clip(RoundedCornerShape(3.dp)),
                                    color = DeepRoyalBlue,
                                    trackColor = PureWhite
                                )
                            }
                        }
                    }
                    is UploadUiState.Error -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = ErrorRed.copy(alpha = 0.1f)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Icon(
                                    Icons.Default.Error,
                                    contentDescription = null,
                                    tint = ErrorRed,
                                    modifier = Modifier.size(24.dp)
                                )
                                Column {
                                    Text(
                                        text = "അപ്‌ലോഡ് പരാജയപ്പെട്ടു",
                                        color = ErrorRed,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = state.message,
                                        color = ErrorRed,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun ImagePreviewItem(
    uri: Uri,
    onRemove: () -> Unit,
    enabled: Boolean
) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        if (enabled) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(28.dp),
                shape = CircleShape,
                color = ErrorRed,
                shadowElevation = 4.dp,
                onClick = onRemove
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = PureWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}