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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familydirectory.data.model.FilterOptions
import com.example.familydirectory.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormUploadContent(viewModel: AdminUploadViewModel) {
    // Family Head Details
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
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = SoftGray
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = DeepRoyalBlue,
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                    }
                }
                Text(
                    "Fill in family head details below",
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepRoyalBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Family Head Section
        SectionHeader(
            title = "Family Head Information",
            icon = Icons.Default.Person
        )

        FormTextField(
            value = familyHead,
            onValueChange = { familyHead = it },
            label = "Family Head Name *",
            icon = Icons.Default.Person,
            required = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(
                value = dob,
                onValueChange = { dob = it },
                label = "Date of Birth (YYYY-MM-DD)",
                icon = Icons.Default.Cake,
                modifier = Modifier.weight(1f)
            )

            FormDropdown(
                value = gender,
                onValueChange = { gender = it },
                label = "Gender",
                options = FilterOptions.genders,
                icon = Icons.Default.Person,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "Phone *",
                icon = Icons.Default.Phone,
                required = true,
                modifier = Modifier.weight(1f)
            )

            FormTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                icon = Icons.Default.Email,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(
                value = job,
                onValueChange = { job = it },
                label = "Occupation",
                icon = Icons.Default.Work,
                modifier = Modifier.weight(1f)
            )

            FormTextField(
                value = education,
                onValueChange = { education = it },
                label = "Education",
                icon = Icons.Default.School,
                modifier = Modifier.weight(1f)
            )
        }

        FormDropdown(
            value = bloodGroup,
            onValueChange = { bloodGroup = it },
            label = "Blood Group",
            options = FilterOptions.bloodGroups,
            icon = Icons.Default.Favorite
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Location Section
        SectionHeader(
            title = "Location Details",
            icon = Icons.Default.LocationOn
        )

        FormTextField(
            value = place,
            onValueChange = { place = it },
            label = "Place *",
            icon = Icons.Default.Home,
            required = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FormTextField(
                value = postOffice,
                onValueChange = { postOffice = it },
                label = "Post Office",
                icon = Icons.Default.LocationOn,
                modifier = Modifier.weight(1f)
            )

            FormTextField(
                value = region,
                onValueChange = { region = it },
                label = "Region",
                icon = Icons.Default.Place,
                modifier = Modifier.weight(1f)
            )
        }

        FormDropdown(
            value = parish,
            onValueChange = { parish = it },
            label = "Parish *",
            options = FilterOptions.parishes,
            icon = Icons.Default.Church,
            required = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Additional Info Section
        SectionHeader(
            title = "Additional Information",
            icon = Icons.Default.Description
        )

        FormTextField(
            value = otherInfo,
            onValueChange = { otherInfo = it },
            label = "Other Information",
            icon = Icons.Default.Notes,
            singleLine = false,
            maxLines = 5
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Upload Button
        Button(
            onClick = {
                viewModel.uploadFromForm(
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
            enabled = familyHead.isNotBlank() && place.isNotBlank() &&
                    parish.isNotBlank() && phone.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = DeepRoyalBlue,
                disabledContainerColor = LightBorder
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(4.dp)
        ) {
            Icon(
                Icons.Default.CloudUpload,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "Upload Family Data",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SectionHeader(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = SoftGray,
            modifier = Modifier.size(36.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = DeepRoyalBlue
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = DeepRoyalBlue
        )
    }
    HorizontalDivider(color = LightBorder)
}

@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    required: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                if (required) "$label *" else label,
                color = if (required) DeepRoyalBlue else TextSecondary
            )
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = DeepRoyalBlue,
                modifier = Modifier.size(20.dp)
            )
        },
        modifier = modifier.fillMaxWidth(),
        singleLine = singleLine,
        maxLines = maxLines,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = DeepRoyalBlue,
            unfocusedBorderColor = LightBorder,
            focusedTextColor = TextDark,
            unfocusedTextColor = TextDark
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDropdown(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    required: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value.ifEmpty { "Select" },
            onValueChange = {},
            readOnly = true,
            label = {
                Text(
                    if (required) "$label *" else label,
                    color = if (required) DeepRoyalBlue else TextSecondary
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = DeepRoyalBlue,
                    modifier = Modifier.size(20.dp)
                )
            },
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
                unfocusedTextColor = TextDark
            ),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            option,
                            color = if (value == option) DeepRoyalBlue else TextDark,
                            fontWeight = if (value == option) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    onClick = {
                        onValueChange(option)
                        expanded = false
                    },
                    leadingIcon = if (value == option) {
                        { Icon(Icons.Default.Check, null, tint = DeepRoyalBlue) }
                    } else null
                )
            }
        }
    }
}