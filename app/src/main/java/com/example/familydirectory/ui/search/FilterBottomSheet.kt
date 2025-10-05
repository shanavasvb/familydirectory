package com.example.familydirectory.ui.search

// FilterBottomSheet.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.familydirectory.data.model.FilterOptions
import com.example.familydirectory.data.model.SearchFilters

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
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filter Families",
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, "Close")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Scrollable content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Parish Filter
                FilterSection(
                    title = "Parish",
                    selectedValue = selectedParish,
                    options = if (availableParishes.isNotEmpty())
                        availableParishes
                    else
                        FilterOptions.parishes,
                    onValueChange = { selectedParish = it }
                )

                Divider()

                // Region Filter
                FilterSection(
                    title = "Region",
                    selectedValue = selectedRegion,
                    options = if (availableRegions.isNotEmpty())
                        availableRegions
                    else
                        FilterOptions.regions,
                    onValueChange = { selectedRegion = it }
                )

                Divider()

                // Blood Group Filter
                FilterSection(
                    title = "Blood Group",
                    selectedValue = selectedBloodGroup,
                    options = FilterOptions.bloodGroups,
                    onValueChange = { selectedBloodGroup = it }
                )

                Divider()

                // Gender Filter
                FilterSection(
                    title = "Gender",
                    selectedValue = selectedGender,
                    options = FilterOptions.genders,
                    onValueChange = { selectedGender = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        selectedParish = null
                        selectedRegion = null
                        selectedBloodGroup = null
                        selectedGender = null
                        viewModel.clearFilters()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear All")
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
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Apply Filters")
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
    selectedValue: String?,
    options: List<String>,
    onValueChange: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            OutlinedTextField(
                value = selectedValue ?: "All",
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(),
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("All") },
                    onClick = {
                        onValueChange(null)
                        expanded = false
                    }
                )

                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}