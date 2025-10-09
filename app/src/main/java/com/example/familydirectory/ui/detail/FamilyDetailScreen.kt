package com.example.familydirectory.ui.detail

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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.familydirectory.data.model.Family
import com.example.familydirectory.data.model.FamilyMember
import com.example.familydirectory.data.repository.FamilyRepository
import com.example.familydirectory.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// FamilyDetailViewModel
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
                title = {
                    Text(
                        "Family Details",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryBlue
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = PrimaryBlue,
                        strokeWidth = 3.dp
                    )
                }

                family == null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.ErrorOutline,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = ErrorRed
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Family not found",
                            style = MaterialTheme.typography.titleLarge,
                            color = ErrorRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
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
        // Family Head Card with Gradient
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                GradientBlueStart,
                                GradientBlueEnd
                            )
                        )
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Surface(
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.3f),
                        modifier = Modifier.size(70.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = family.familyHead.firstOrNull()?.uppercase() ?: "F",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Family Head",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = family.familyHead,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Contact Information
        if (family.phone.isNotEmpty() || family.email.isNotEmpty()) {
            InfoSection(
                title = "Contact Information",
                icon = Icons.Default.Phone
            ) {
                if (family.phone.isNotEmpty()) {
                    InfoRow(
                        icon = Icons.Default.Phone,
                        label = "Phone",
                        value = family.phone
                    )
                }

                if (family.email.isNotEmpty()) {
                    if (family.phone.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    InfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = family.email
                    )
                }
            }
        }

        // Location Information
        if (family.place.isNotEmpty() || family.postOffice.isNotEmpty() ||
            family.region.isNotEmpty() || family.parish.isNotEmpty()) {
            InfoSection(
                title = "Location",
                icon = Icons.Default.LocationOn
            ) {
                if (family.place.isNotEmpty()) {
                    InfoRow(
                        icon = Icons.Default.Home,
                        label = "Place",
                        value = family.place
                    )
                }

                if (family.postOffice.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "Post Office",
                        value = family.postOffice
                    )
                }

                if (family.region.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Place,
                        label = "Region",
                        value = family.region
                    )
                }

                if (family.parish.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Church,
                        label = "Parish",
                        value = family.parish
                    )
                }
            }
        }

        // Personal Information
        if (family.dob.isNotEmpty() || family.gender.isNotEmpty() ||
            family.bloodGroup.isNotEmpty() || family.job.isNotEmpty() ||
            family.education.isNotEmpty()) {
            InfoSection(
                title = "Personal Information",
                icon = Icons.Default.Person
            ) {
                var firstItem = true

                if (family.dob.isNotEmpty()) {
                    InfoRow(
                        icon = Icons.Default.Cake,
                        label = "Date of Birth",
                        value = family.dob
                    )
                    firstItem = false
                }

                if (family.gender.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Person,
                        label = "Gender",
                        value = family.gender
                    )
                    firstItem = false
                }

                if (family.bloodGroup.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Favorite,
                        label = "Blood Group",
                        value = family.bloodGroup
                    )
                    firstItem = false
                }

                if (family.job.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Work,
                        label = "Occupation",
                        value = family.job
                    )
                    firstItem = false
                }

                if (family.education.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.School,
                        label = "Education",
                        value = family.education
                    )
                }
            }
        }

        // Family Members
        if (family.familyMembers.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
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
                                    Icons.Default.People,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = PrimaryBlue
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Family Members",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = PrimaryBlue
                            )
                            Text(
                                text = "${family.familyMembers.size} member${if (family.familyMembers.size != 1) "s" else ""}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    family.familyMembers.forEachIndexed { index, member ->
                        if (index > 0) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 16.dp),
                                color = DividerLight
                            )
                        }
                        FamilyMemberCard(member = member)
                    }
                }
            }
        }

        // Other Information
        if (family.otherInfo.isNotEmpty()) {
            InfoSection(
                title = "Additional Information",
                icon = Icons.Default.Info
            ) {
                Text(
                    text = family.otherInfo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun InfoSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
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
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = PrimaryBlue
                        )
                    }
                }
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            content()
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
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = SurfaceBlueLight,
            modifier = Modifier.size(36.dp)
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

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextTertiary,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun FamilyMemberCard(member: FamilyMember) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SurfaceBlueLight.copy(alpha = 0.3f)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: Name and Relation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = PrimaryBlue,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = member.name.firstOrNull()?.uppercase() ?: "?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                    Text(
                        text = member.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }

                if (member.relation.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = PrimaryBlue
                    ) {
                        Text(
                            text = member.relation,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Details
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodySmall,
            color = TextTertiary,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = TextPrimary,
            fontWeight = FontWeight.Normal
        )
    }
}