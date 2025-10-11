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
import com.example.familydirectory.ui.theme.*

@Composable
fun JsonUploadContent(
    viewModel: AdminUploadViewModel,
    uploadStatus: AdminUploadState
) {
    var jsonInput by remember { mutableStateOf("") }

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
                containerColor = InfoBlue.copy(alpha = 0.1f)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = InfoBlue
                )
                Column {
                    Text(
                        text = "JSON Upload Supports:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = InfoBlue
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• Single family: { ... }\n• Multiple families: [ {...}, {...} ]",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextDark
                    )
                }
            }
        }

        // Template Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    jsonInput = getSingleFamilyTemplate()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Description, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Single Family", fontSize = MaterialTheme.typography.bodySmall.fontSize)
            }

            Button(
                onClick = {
                    jsonInput = getMultipleFamiliesTemplate()
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DeepRoyalBlue
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.People, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Multiple", fontSize = MaterialTheme.typography.bodySmall.fontSize)
            }
        }

        // JSON Input
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
                                Icons.Default.Code,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = DeepRoyalBlue
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "JSON Input",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = DeepRoyalBlue
                        )
                        Text(
                            text = "${jsonInput.length} characters",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = jsonInput,
                    onValueChange = { jsonInput = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    placeholder = {
                        Text(
                            "Paste JSON here...\n\nSingle: { \"familyHead\": \"...\", ... }\n\nMultiple: [ {...}, {...} ]",
                            color = TextHint
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DeepRoyalBlue,
                        unfocusedBorderColor = LightBorder,
                        focusedTextColor = TextDark,
                        unfocusedTextColor = TextDark
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                if (jsonInput.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { jsonInput = "" },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = ErrorRed
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Clear, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Clear")
                        }

                        Button(
                            onClick = {
                                viewModel.uploadFamilyFromJson(jsonInput)
                            },
                            modifier = Modifier.weight(1f),
                            enabled = jsonInput.isNotBlank() && uploadStatus !is AdminUploadState.Uploading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = HeritageGold,
                                contentColor = DeepRoyalBlue
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.CloudUpload, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Upload", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Upload Status
        when (uploadStatus) {
            is AdminUploadState.Uploading -> {
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
                                    text = "Uploading ${uploadStatus.current} of ${uploadStatus.total}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            progress = { uploadStatus.current.toFloat() / uploadStatus.total.toFloat() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp),
                            color = DeepRoyalBlue,
                            trackColor = PureWhite
                        )
                    }
                }
            }
            is AdminUploadState.Success -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = SuccessGreen.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "വിജയകരമായി അപ്‌ലോഡ് ചെയ്തു!",
                                color = SuccessGreen,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${uploadStatus.count} ${if (uploadStatus.count == 1) "family" else "families"} uploaded successfully",
                                color = SuccessGreen,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
            is AdminUploadState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = ErrorRed.copy(alpha = 0.1f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top,
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
                                text = uploadStatus.message,
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

private fun getSingleFamilyTemplate(): String {
    return """
{
  "familyHead": "John Mathew",
  "place": "Kumbleri",
  "postOffice": "Meenangadi",
  "region": "Kumbleri",
  "parish": "St.Mary's Jacobite Cheengeri",
  "phone": "9446860975",
  "email": "john@example.com",
  "job": "Farmer",
  "education": "High School",
  "bloodGroup": "O positive (O+)",
  "dob": "1980-05-15",
  "gender": "Male",
  "familyMembers": [
    {
      "name": "Mary John",
      "relation": "Wife",
      "fatherName": "Thomas",
      "motherName": "Sara",
      "spouseName": "John Mathew",
      "dob": "1985-08-20",
      "education": "Degree",
      "job": "Teacher",
      "institution": "St. Mary's School",
      "phone": "9847123456",
      "email": "mary@example.com",
      "bloodGroup": "A positive (A+)",
      "gender": "Female"
    }
  ],
  "otherInfo": "Active church member"
}
    """.trimIndent()
}

private fun getMultipleFamiliesTemplate(): String {
    return """
[
  {
    "familyHead": "John Mathew",
    "place": "Kumbleri",
    "postOffice": "Meenangadi",
    "region": "Kumbleri",
    "parish": "St.Mary's Jacobite Cheengeri",
    "phone": "9446860975",
    "email": "john@example.com",
    "job": "Farmer",
    "education": "High School",
    "bloodGroup": "O positive (O+)",
    "dob": "1980-05-15",
    "gender": "Male",
    "familyMembers": [],
    "otherInfo": ""
  },
  {
    "familyHead": "Thomas Joseph",
    "place": "Meenangadi",
    "postOffice": "Meenangadi",
    "region": "Meenangadi",
    "parish": "St.Mary's Jacobite Cheengeri",
    "phone": "9447123456",
    "email": "thomas@example.com",
    "job": "Teacher",
    "education": "B.Ed",
    "bloodGroup": "A positive (A+)",
    "dob": "1975-03-20",
    "gender": "Male",
    "familyMembers": [
      {
        "name": "Maria Thomas",
        "relation": "Wife",
        "fatherName": "George",
        "motherName": "Anna",
        "spouseName": "Thomas Joseph",
        "dob": "1980-06-15",
        "education": "Degree",
        "job": "Nurse",
        "institution": "District Hospital",
        "phone": "9847654321",
        "email": "maria@example.com",
        "bloodGroup": "B positive (B+)",
        "gender": "Female"
      }
    ],
    "otherInfo": "Active member"
  },
  {
    "familyHead": "George Philip",
    "place": "Kumbleri",
    "postOffice": "Kumbleri",
    "region": "Kumbleri",
    "parish": "St.Mary's Jacobite Cheengeri",
    "phone": "9496123456",
    "email": "",
    "job": "Business",
    "education": "Degree",
    "bloodGroup": "O positive (O+)",
    "dob": "1970-12-10",
    "gender": "Male",
    "familyMembers": [],
    "otherInfo": ""
  }
]
    """.trimIndent()
}