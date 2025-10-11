package com.example.familydirectory.ui.detail

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.familydirectory.data.model.Family
import com.example.familydirectory.data.model.FamilyMember
import com.example.familydirectory.data.repository.FamilyRepository
import com.example.familydirectory.ui.theme.*
import com.example.familydirectory.utils.ClickableEmail
import com.example.familydirectory.utils.ClickablePhoneNumber
import com.example.familydirectory.utils.PhoneCallHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
                    Column {
                        Text(
                            "കുടുംബ വിവരങ്ങൾ",
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Text(
                            "Family Details",
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = PureWhite.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back",
                            tint = PureWhite
                        )
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

            when {
                isLoading -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = DeepRoyalBlue,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "ലോഡ് ചെയ്യുന്നു...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                family == null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = ErrorRed.copy(alpha = 0.1f),
                            modifier = Modifier.size(80.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = ErrorRed
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "കുടുംബം കണ്ടെത്തിയില്ല",
                            style = MaterialTheme.typography.titleLarge,
                            color = ErrorRed,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Family not found",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
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
                containerColor = PureWhite
            ),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                DeepRoyalBlue,
                                RoyalBlueLight
                            )
                        )
                    )
                    .padding(28.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Avatar
                    Surface(
                        shape = CircleShape,
                        color = HeritageGold,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = family.familyHead.firstOrNull()?.uppercase() ?: "F",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = DeepRoyalBlue
                            )
                        }
                    }

                    // Info
                    Column {
                        Text(
                            text = "കുടുംബനാഥൻ",
                            style = MaterialTheme.typography.labelMedium,
                            color = PureWhite.copy(alpha = 0.9f)
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = family.familyHead,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = PureWhite
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Family Head",
                            style = MaterialTheme.typography.bodySmall,
                            color = PureWhite.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Contact Information
        if (family.phone.isNotEmpty() || family.email.isNotEmpty()) {
            InfoSection(
                title = "ബന്ധപ്പെടാനുള്ള വിവരങ്ങൾ",
                subtitle = "Contact Information",
                icon = Icons.Default.Phone
            ) {
                if (family.phone.isNotEmpty()) {
                    ClickablePhoneNumber(
                        phoneNumber = family.phone,
                        label = "ഫോൺ",
                        sublabel = "Phone"
                    )
                }

                if (family.email.isNotEmpty()) {
                    if (family.phone.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                    ClickableEmail(
                        email = family.email,
                        label = "ഇമെയിൽ",
                        sublabel = "Email"
                    )
                }
            }
        }

        // Location Information
        if (family.place.isNotEmpty() || family.postOffice.isNotEmpty() ||
            family.region.isNotEmpty() || family.parish.isNotEmpty()) {
            InfoSection(
                title = "സ്ഥലം",
                subtitle = "Location",
                icon = Icons.Default.LocationOn
            ) {
                var firstItem = true

                if (family.place.isNotEmpty()) {
                    InfoRow(
                        icon = Icons.Default.Home,
                        label = "സ്ഥലം",
                        sublabel = "Place",
                        value = family.place
                    )
                    firstItem = false
                }

                if (family.postOffice.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.LocationOn,
                        label = "പോസ്റ്റ് ഓഫീസ്",
                        sublabel = "Post Office",
                        value = family.postOffice
                    )
                    firstItem = false
                }

                if (family.region.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Place,
                        label = "പ്രദേശം",
                        sublabel = "Region",
                        value = family.region
                    )
                    firstItem = false
                }

                if (family.parish.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Church,
                        label = "ഇടവക",
                        sublabel = "Parish",
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
                title = "വ്യക്തിഗത വിവരങ്ങൾ",
                subtitle = "Personal Information",
                icon = Icons.Default.Person
            ) {
                var firstItem = true

                if (family.dob.isNotEmpty()) {
                    InfoRow(
                        icon = Icons.Default.Cake,
                        label = "ജനനത്തീയതി",
                        sublabel = "Date of Birth",
                        value = family.dob
                    )
                    firstItem = false
                }

                if (family.gender.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Person,
                        label = "ലിംഗം",
                        sublabel = "Gender",
                        value = family.gender
                    )
                    firstItem = false
                }

                if (family.bloodGroup.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Favorite,
                        label = "രക്തഗ്രൂപ്പ്",
                        sublabel = "Blood Group",
                        value = family.bloodGroup
                    )
                    firstItem = false
                }

                if (family.job.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.Work,
                        label = "തൊഴിൽ",
                        sublabel = "Occupation",
                        value = family.job
                    )
                    firstItem = false
                }

                if (family.education.isNotEmpty()) {
                    if (!firstItem) Spacer(modifier = Modifier.height(12.dp))
                    InfoRow(
                        icon = Icons.Default.School,
                        label = "വിദ്യാഭ്യാസം",
                        sublabel = "Education",
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
                    containerColor = PureWhite
                ),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = DeepRoyalBlue.copy(alpha = 0.1f),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.People,
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = DeepRoyalBlue
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "കുടുംബാംഗങ്ങൾ",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = DeepRoyalBlue
                            )
                            Text(
                                text = "${family.familyMembers.size} members",
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
                                color = LightBorder
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
                title = "കൂടുതൽ വിവരങ്ങൾ",
                subtitle = "Additional Information",
                icon = Icons.Default.Info
            ) {
                Text(
                    text = family.otherInfo,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextDark,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight.times(1.5f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun InfoSection(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = DeepRoyalBlue.copy(alpha = 0.1f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = DeepRoyalBlue
                        )
                    }
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepRoyalBlue
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
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
    sublabel: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = DeepRoyalBlue.copy(alpha = 0.08f),
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
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = sublabel,
                style = MaterialTheme.typography.labelSmall,
                color = TextHint
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = TextDark,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun FamilyMemberCard(member: FamilyMember) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = SoftGray
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
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
                        color = WarmTerracotta,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = member.name.firstOrNull()?.uppercase() ?: "?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite
                            )
                        }
                    }
                    Text(
                        text = member.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }

                if (member.relation.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = HeritageGold.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = member.relation,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = WarmTerracotta,
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
                    MemberDetailRow(label = "ജനനത്തീയതി", sublabel = "DOB", value = member.dob)
                }
                if (member.gender.isNotEmpty()) {
                    MemberDetailRow(label = "ലിംഗം", sublabel = "Gender", value = member.gender)
                }
                if (member.bloodGroup.isNotEmpty()) {
                    MemberDetailRow(label = "രക്തഗ്രൂപ്പ്", sublabel = "Blood Group", value = member.bloodGroup)
                }
                if (member.phone.isNotEmpty()) {
                    ClickableMemberPhone(phoneNumber = member.phone)
                }
                if (member.email.isNotEmpty()) {
                    ClickableMemberEmail(email = member.email)
                }
                if (member.education.isNotEmpty()) {
                    MemberDetailRow(label = "വിദ്യാഭ്യാസം", sublabel = "Education", value = member.education)
                }
                if (member.job.isNotEmpty()) {
                    MemberDetailRow(label = "തൊഴിൽ", sublabel = "Occupation", value = member.job)
                }
                if (member.institution.isNotEmpty()) {
                    MemberDetailRow(label = "സ്ഥാപനം", sublabel = "Institution", value = member.institution)
                }
                if (member.spouseName.isNotEmpty()) {
                    MemberDetailRow(label = "ഇണ", sublabel = "Spouse", value = member.spouseName)
                }
                if (member.fatherName.isNotEmpty()) {
                    MemberDetailRow(label = "പിതാവ്", sublabel = "Father", value = member.fatherName)
                }
                if (member.motherName.isNotEmpty()) {
                    MemberDetailRow(label = "മാതാവ്", sublabel = "Mother", value = member.motherName)
                }
            }
        }
    }
}

@Composable
fun MemberDetailRow(label: String, sublabel: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.width(120.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = sublabel,
                style = MaterialTheme.typography.labelSmall,
                color = TextHint
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = TextDark
        )
    }
}

@Composable
fun ClickableMemberPhone(phoneNumber: String) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
    ) {
        Column(modifier = Modifier.width(120.dp)) {
            Text(
                text = "ഫോൺ",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Phone",
                style = MaterialTheme.typography.labelSmall,
                color = TextHint
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = phoneNumber,
                style = MaterialTheme.typography.bodySmall,
                color = DeepRoyalBlue,
                fontWeight = FontWeight.Medium
            )
            Icon(
                Icons.Default.Phone,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = DeepRoyalBlue.copy(alpha = 0.6f)
            )
        }
    }

    if (showDialog) {
        com.example.familydirectory.utils.PhoneActionDialog(
            phoneNumber = phoneNumber,
            onDismiss = { showDialog = false },
            onCall = {
                PhoneCallHelper.makeCall(context, phoneNumber)
                showDialog = false
            },
            onSms = {
                PhoneCallHelper.sendSms(context, phoneNumber)
                showDialog = false
            }
        )
    }
}

@Composable
fun ClickableMemberEmail(email: String) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { PhoneCallHelper.sendEmail(context, email) }
    ) {
        Column(modifier = Modifier.width(120.dp)) {
            Text(
                text = "ഇമെയിൽ",
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Email",
                style = MaterialTheme.typography.labelSmall,
                color = TextHint
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = email,
                style = MaterialTheme.typography.bodySmall,
                color = DeepRoyalBlue,
                fontWeight = FontWeight.Medium
            )
            Icon(
                Icons.Default.Email,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = DeepRoyalBlue.copy(alpha = 0.6f)
            )
        }
    }
}