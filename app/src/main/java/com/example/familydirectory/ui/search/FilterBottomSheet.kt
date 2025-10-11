package com.example.familydirectory.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familydirectory.data.model.FilterOptions
import com.example.familydirectory.data.model.SearchFilters
import com.example.familydirectory.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    viewModel: SearchViewModel,
    onDismiss: () -> Unit
) {
    val currentFilters by viewModel.filters.collectAsState()
    val availableParishes by viewModel.availableParishes.collectAsState()
    val availableRegions by viewModel.availableRegions.collectAsState()

    var selectedParish by remember { mutableStateOf(currentFilters.parish) }
    var selectedRegion by remember { mutableStateOf(currentFilters.region) }
    var selectedBloodGroup by remember { mutableStateOf(currentFilters.bloodGroup) }
    var selectedGender by remember { mutableStateOf(currentFilters.gender) }
    LaunchedEffect(currentFilters) {
        selectedParish = currentFilters.parish
        selectedRegion = currentFilters.region
        selectedBloodGroup = currentFilters.bloodGroup
        selectedGender = currentFilters.gender
    }


    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = PureWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header with gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                DeepRoyalBlue,
                                RoyalBlueLight
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = HeritageGold,
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.FilterAlt,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = DeepRoyalBlue
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "ഫിൽട്ടറുകൾ",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                            Text(
                                text = "Filter Families",
                                style = MaterialTheme.typography.bodySmall,
                                color = PureWhite.copy(alpha = 0.9f)
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            "Close",
                            tint = PureWhite
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Info card if data is loading
            if (availableParishes.isEmpty() && availableRegions.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = InfoBlue.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = InfoBlue
                        )
                        Text(
                            text = "ഫിൽട്ടർ ഓപ്ഷനുകൾ ലോഡ് ചെയ്യുന്നു...",
                            style = MaterialTheme.typography.bodySmall,
                            color = InfoBlue
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Scrollable filters
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Parish Filter - ✅ DYNAMIC from Firebase
                FilterSection(
                    title = "ഇടവക",
                    subtitle = "Parish",
                    icon = Icons.Default.Church,
                    selectedValue = selectedParish,
                    options = if (availableParishes.isNotEmpty())
                        availableParishes
                    else
                        FilterOptions.parishes, // Fallback to static
                    onValueChange = { selectedParish = it },
                    showCount = availableParishes.isNotEmpty(),
                    count = availableParishes.size
                )

                HorizontalDivider(color = LightBorder)

                // Region Filter - ✅ DYNAMIC from Firebase
                FilterSection(
                    title = "പ്രദേശം",
                    subtitle = "Region",
                    icon = Icons.Default.Place,
                    selectedValue = selectedRegion,
                    options = if (availableRegions.isNotEmpty())
                        availableRegions
                    else
                        FilterOptions.regions, // Fallback to static
                    onValueChange = { selectedRegion = it },
                    showCount = availableRegions.isNotEmpty(),
                    count = availableRegions.size
                )

                HorizontalDivider(color = LightBorder)

                // Blood Group Filter - Static (doesn't need to grow)
                FilterSection(
                    title = "രക്തഗ്രൂപ്പ്",
                    subtitle = "Blood Group",
                    icon = Icons.Default.Favorite,
                    selectedValue = selectedBloodGroup,
                    options = FilterOptions.bloodGroups,
                    onValueChange = { selectedBloodGroup = it }
                )

                HorizontalDivider(color = LightBorder)

                // Gender Filter - Static
                FilterSection(
                    title = "ലിംഗം",
                    subtitle = "Gender",
                    icon = Icons.Default.Person,
                    selectedValue = selectedGender,
                    options = FilterOptions.genders,
                    onValueChange = { selectedGender = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        selectedParish = null
                        selectedRegion = null
                        selectedBloodGroup = null
                        selectedGender = null
                        viewModel.clearFilters()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = ErrorRed
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, ErrorRed)
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("മായ്ക്കുക", fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        viewModel.updateFilters(
                            SearchFilters(
                                parish = selectedParish,
                                region = selectedRegion,
                                bloodGroup = selectedBloodGroup,
                                gender = selectedGender
                            )
                        )
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepRoyalBlue,
                        contentColor = PureWhite
                    )
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("പ്രയോഗിക്കുക", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSection(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selectedValue: String?,
    options: List<String>,
    onValueChange: (String?) -> Unit,
    showCount: Boolean = false,
    count: Int = 0
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = DeepRoyalBlue.copy(alpha = 0.1f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = DeepRoyalBlue
                    )
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    if (showCount && count > 0) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DeepRoyalBlue.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "$count options",
                                style = MaterialTheme.typography.labelSmall,
                                color = DeepRoyalBlue,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedValue ?: "എല്ലാം",
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder,
                    focusedTextColor = TextDark,
                    unfocusedTextColor = TextDark,
                    focusedContainerColor = PureWhite,
                    unfocusedContainerColor = PureWhite
                ),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(PureWhite)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "എല്ലാം / All",
                            fontWeight = if (selectedValue == null) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedValue == null) DeepRoyalBlue else TextDark
                        )
                    },
                    onClick = {
                        onValueChange(null)
                        expanded = false
                    },
                    leadingIcon = if (selectedValue == null) {
                        { Icon(Icons.Default.Check, null, tint = DeepRoyalBlue) }
                    } else null
                )

                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                option,
                                fontWeight = if (selectedValue == option) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedValue == option) DeepRoyalBlue else TextDark
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        },
                        leadingIcon = if (selectedValue == option) {
                            { Icon(Icons.Default.Check, null, tint = DeepRoyalBlue) }
                        } else null
                    )
                }
            }
        }
    }
}