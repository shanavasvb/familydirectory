package com.example.familydirectory.ui.detail

// FamilyDetailScreen.kt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.familydirectory.data.model.Family
import com.example.familydirectory.data.model.FamilyMember
import com.example.familydirectory.data.repository.FamilyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// FamilyDetailViewModel.kt
class FamilyDetailViewModel : ViewModel() {
    private val repository = FamilyRepository()

    private val _family = MutableStateFlow<Family?>(null)
    val family: StateFlow<Family?> = _family.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadFamily(familyId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val familyData = repository.getFamilyById(familyId)
            _family.value = familyData
            _isLoading.value = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyDetailScreen(
    familyId: String,
    onNavigateBack: () -> Unit,
    viewModel: FamilyDetailViewModel = viewModel()
) {
    val family by viewModel.family.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(familyId) {
        viewModel.loadFamily(familyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Family Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                family == null -> {
                    Text(
                        text = "Family not found",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                else -> {
                    FamilyDetailContent(family = family!!)
                }
            }
        }
    }
}

@Composable
fun FamilyDetailContent(family: Family) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Family Head Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Family Head",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = family.familyHead,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Contact Information
        InfoSection(title = "Contact Information") {
            if (family.phone.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.Phone,
                    label = "Phone",
                    value = family.phone
                )
            }

            if (family.email.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = family.email
                )
            }
        }

        // Location Information
        InfoSection(title = "Location") {
            if (family.place.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.Home,
                    label = "Place",
                    value = family.place
                )
            }

            if (family.postOffice.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.LocationOn,
                    label = "Post Office",
                    value = family.postOffice
                )
            }

            if (family.region.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.Place,
                    label = "Region",
                    value = family.region
                )
            }

            if (family.parish.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.Church,
                    label = "Parish",
                    value = family.parish
                )
            }
        }

        // Personal Information
        InfoSection(title = "Personal Information") {
            if (family.dob.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.Cake,
                    label = "Date of Birth",
                    value = family.dob
                )
            }

            if (family.gender.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.Person,
                    label = "Gender",
                    value = family.gender
                )
            }

            if (family.bloodGroup.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.Favorite,
                    label = "Blood Group",
                    value = family.bloodGroup
                )
            }

            if (family.job.isNotEmpty()) {
                InfoRow(
                        icon = Icons.Default.Work,
                    label = "Occupation",
                    value = family.job
                )
            }

            if (family.education.isNotEmpty()) {
                InfoRow(
                    icon = Icons.Default.School,
                    label = "Education",
                    value = family.education
                )
            }
        }

        // Family Members
        if (family.familyMembers.isNotEmpty()) {
            Text(
                text = "Family Members (${family.familyMembers.size})",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            family.familyMembers.forEach { member ->
                FamilyMemberCard(member = member)
            }
        }

        // Other Information
        if (family.otherInfo.isNotEmpty()) {
            InfoSection(title = "Additional Information") {
                Text(
                    text = family.otherInfo,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun InfoSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun FamilyMemberCard(member: FamilyMember) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Name and Relation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = member.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )

                if (member.relation.isNotEmpty()) {
                    AssistChip(
                        onClick = {},
                        label = { Text(member.relation) }
                    )
                }
            }

            // Details
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (member.dob.isNotEmpty()) {
                    MemberDetailRow(label = "DOB", value = member.dob)
                }

                if (member.gender.isNotEmpty()) {
                    MemberDetailRow(label = "Gender", value = member.gender)
                }

                if (member.bloodGroup.isNotEmpty()) {
                    MemberDetailRow(label = "Blood Group", value = member.bloodGroup)
                }

                if (member.phone.isNotEmpty()) {
                    MemberDetailRow(label = "Phone", value = member.phone)
                }

                if (member.email.isNotEmpty()) {
                    MemberDetailRow(label = "Email", value = member.email)
                }

                if (member.education.isNotEmpty()) {
                    MemberDetailRow(label = "Education", value = member.education)
                }

                if (member.job.isNotEmpty()) {
                    MemberDetailRow(label = "Occupation", value = member.job)
                }

                if (member.institution.isNotEmpty()) {
                    MemberDetailRow(label = "Institution", value = member.institution)
                }

                if (member.spouseName.isNotEmpty()) {
                    MemberDetailRow(label = "Spouse", value = member.spouseName)
                }

                if (member.fatherName.isNotEmpty()) {
                    MemberDetailRow(label = "Father", value = member.fatherName)
                }

                if (member.motherName.isNotEmpty()) {
                    MemberDetailRow(label = "Mother", value = member.motherName)
                }
            }
        }
    }
}

@Composable
fun MemberDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f),
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}