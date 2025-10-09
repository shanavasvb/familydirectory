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

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BackgroundWhite,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Header
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
                        color = SurfaceBlueLight,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.FilterAlt,
                                contentDescription = null,
                                modifier = Modifier.size(24.dp),
                                tint = PrimaryBlue
                            )
                        }
                    }
                    Text(
                        text = "Filter Families",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        "Close",
                        tint = TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Parish Filter
                FilterSection(
                    title = "Parish",
                    icon = Icons.Default.Church,
                    selectedValue = selectedParish,
                    options = if (availableParishes.isNotEmpty())
                        availableParishes
                    else
                        FilterOptions.parishes,
                    onValueChange = { selectedParish = it }
                )

                HorizontalDivider(color = DividerLight)

                // Region Filter
                FilterSection(
                    title = "Region",
                    icon = Icons.Default.Place,
                    selectedValue = selectedRegion,
                    options = if (availableRegions.isNotEmpty())
                        availableRegions
                    else
                        FilterOptions.regions,
                    onValueChange = { selectedRegion = it }
                )

                HorizontalDivider(color = DividerLight)

                // Blood Group Filter
                FilterSection(
                    title = "Blood Group",
                    icon = Icons.Default.Favorite,
                    selectedValue = selectedBloodGroup,
                    options = FilterOptions.bloodGroups,
                    onValueChange = { selectedBloodGroup = it }
                )

                HorizontalDivider(color = DividerLight)

                // Gender Filter
                FilterSection(
                    title = "Gender",
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
                    border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
                ) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Clear All", fontWeight = FontWeight.Bold)
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
                        containerColor = PrimaryBlue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Apply", fontWeight = FontWeight.Bold)
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
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selectedValue: String?,
    options: List<String>,
    onValueChange: (String?) -> Unit
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
                color = SurfaceBlueLight,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = PrimaryBlue
                    )
                }
            }
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedValue ?: "All",
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = BorderBlue,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(Color.White)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            "All",
                            fontWeight = if (selectedValue == null) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedValue == null) PrimaryBlue else TextPrimary
                        )
                    },
                    onClick = {
                        onValueChange(null)
                        expanded = false
                    },
                    leadingIcon = if (selectedValue == null) {
                        { Icon(Icons.Default.Check, null, tint = PrimaryBlue) }
                    } else null
                )

                options.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                option,
                                fontWeight = if (selectedValue == option) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedValue == option) PrimaryBlue else TextPrimary
                            )
                        },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        },
                        leadingIcon = if (selectedValue == option) {
                            { Icon(Icons.Default.Check, null, tint = PrimaryBlue) }
                        } else null
                    )
                }
            }
        }
    }
}