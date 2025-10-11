package com.example.familydirectory.ui.admin

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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familydirectory.data.model.FilterOptions
import com.example.familydirectory.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormUploadContent(
    viewModel: AdminUploadViewModel,
    uploadStatus: AdminUploadState
) {
    var familyHead by remember { mutableStateOf("") }
    var place by remember { mutableStateOf("") }
    var postOffice by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var parish by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }
    var education by remember { mutableStateOf("") }
    var bloodGroup by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var otherInfo by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SoftGray)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Basic Information
        SectionCard(title = "അടിസ്ഥാന വിവരങ്ങൾ", subtitle = "Basic Information") {
            OutlinedTextField(
                value = familyHead,
                onValueChange = { familyHead = it },
                label = { Text("കുടുംബനാഥൻ / Family Head *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = place,
                onValueChange = { place = it },
                label = { Text("സ്ഥലം / Place *") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )
        }

        // Location Details
        SectionCard(title = "സ്ഥല വിവരങ്ങൾ", subtitle = "Location Details") {
            OutlinedTextField(
                value = postOffice,
                onValueChange = { postOffice = it },
                label = { Text("പോസ്റ്റ് ഓഫീസ് / Post Office") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = region,
                onValueChange = { region = it },
                label = { Text("പ്രദേശം / Region") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            var parishExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = parishExpanded,
                onExpandedChange = { parishExpanded = it }
            ) {
                OutlinedTextField(
                    value = parish,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("ഇടവക / Parish") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = parishExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepRoyalBlue,
                        unfocusedBorderColor = LightBorder
                    )
                )

                ExposedDropdownMenu(
                    expanded = parishExpanded,
                    onDismissRequest = { parishExpanded = false }
                ) {
                    FilterOptions.parishes.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                parish = option
                                parishExpanded = false
                            }
                        )
                    }
                }
            }
        }

        // Contact Information
        SectionCard(title = "ബന്ധപ്പെടാൻ", subtitle = "Contact Information") {
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("ഫോൺ / Phone") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Phone, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("ഇമെയിൽ / Email") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Email, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )
        }

        // Personal Details
        SectionCard(title = "വ്യക്തിഗത വിവരങ്ങൾ", subtitle = "Personal Details") {
            OutlinedTextField(
                value = dob,
                onValueChange = { dob = it },
                label = { Text("ജനനത്തീയതി / DOB (YYYY-MM-DD)") },
                placeholder = { Text("1980-05-15") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Cake, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            var genderExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = it }
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("ലിംഗം / Gender") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepRoyalBlue,
                        unfocusedBorderColor = LightBorder
                    )
                )

                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    FilterOptions.genders.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                gender = option
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            var bloodGroupExpanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = bloodGroupExpanded,
                onExpandedChange = { bloodGroupExpanded = it }
            ) {
                OutlinedTextField(
                    value = bloodGroup,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("രക്തഗ്രൂപ്പ് / Blood Group") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = bloodGroupExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepRoyalBlue,
                        unfocusedBorderColor = LightBorder
                    )
                )

                ExposedDropdownMenu(
                    expanded = bloodGroupExpanded,
                    onDismissRequest = { bloodGroupExpanded = false }
                ) {
                    FilterOptions.bloodGroups.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                bloodGroup = option
                                bloodGroupExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = job,
                onValueChange = { job = it },
                label = { Text("തൊഴിൽ / Occupation") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.Work, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = education,
                onValueChange = { education = it },
                label = { Text("വിദ്യാഭ്യാസം / Education") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                leadingIcon = { Icon(Icons.Default.School, null) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )
        }

        // Other Information
        SectionCard(title = "അധിക വിവരങ്ങൾ", subtitle = "Additional Information") {
            OutlinedTextField(
                value = otherInfo,
                onValueChange = { otherInfo = it },
                label = { Text("കുറിപ്പുകൾ / Notes") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DeepRoyalBlue,
                    unfocusedBorderColor = LightBorder
                )
            )
        }

        // Upload Button
        Button(
            onClick = {
                viewModel.uploadFamilyFromForm(
                    familyHead = familyHead,
                    place = place,
                    postOffice = postOffice,
                    region = region,
                    parish = parish,
                    phone = phone,
                    email = email,
                    job = job,
                    education = education,
                    bloodGroup = bloodGroup,
                    dob = dob,
                    gender = gender,
                    otherInfo = otherInfo
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = familyHead.isNotBlank() && place.isNotBlank() && uploadStatus !is AdminUploadState.Uploading,
            colors = ButtonDefaults.buttonColors(
                containerColor = HeritageGold,
                contentColor = DeepRoyalBlue
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.CloudUpload, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("അപ്‌ലോഡ് ചെയ്യുക", fontWeight = FontWeight.Bold)
        }

        // Upload Status
        when (uploadStatus) {
            is AdminUploadState.Uploading -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = DeepRoyalBlue.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = DeepRoyalBlue,
                            strokeWidth = 3.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("അപ്‌ലോഡ് ചെയ്യുന്നു...", color = DeepRoyalBlue)
                    }
                }
            }
            is AdminUploadState.Success -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = SuccessGreen.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = SuccessGreen)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("വിജയകരമായി അപ്‌ലോഡ് ചെയ്തു!", color = SuccessGreen)
                    }
                }
            }
            is AdminUploadState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = ErrorRed.copy(alpha = 0.1f)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Default.Error, null, tint = ErrorRed)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("അപ്‌ലോഡ് പരാജയപ്പെട്ടു", color = ErrorRed, fontWeight = FontWeight.Bold)
                            Text(uploadStatus.message, color = ErrorRed, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            else -> {}
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SectionCard(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
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

            Spacer(modifier = Modifier.height(16.dp))

            content()
        }
    }
}