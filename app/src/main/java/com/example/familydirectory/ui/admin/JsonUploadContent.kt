package com.example.familydirectory.ui.admin

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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familydirectory.ui.theme.*

@Composable
fun JsonUploadContent(viewModel: AdminUploadViewModel) {
    var jsonText by remember { mutableStateOf("") }

    // Sample JSON template
    val sampleJson = """
{
  "familyHead": "M M Varkey",
  "place": "Kumbleri",
  "postOffice": "Kumbleri",
  "region": "Kumbleri",
  "parish": "St.Mary's Jacobite Cheengeri",
  "phone": "9446860975",
  "email": "",
  "job": "Farmer",
  "education": "",
  "bloodGroup": "",
  "dob": "1940-10-24",
  "gender": "Male",
  "familyMembers": [
    {
      "name": "Saramma A P",
      "relation": "Wife",
      "fatherName": "Pathrose",
      "motherName": "Aeliyamma",
      "spouseName": "M M Varkey",
      "dob": "1943-02-04",
      "education": "",
      "job": "",
      "institution": "",
      "phone": "",
      "email": "",
      "bloodGroup": "",
      "gender": "Female"
    }
  ],
  "otherInfo": "Additional information..."
}
    """.trimIndent()

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
                containerColor = SurfaceBlueLight
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = PrimaryBlue,
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
                Column {
                    Text(
                        "JSON Upload Instructions",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "• Paste valid JSON data below\n" +
                                "• Use the sample template as reference\n" +
                                "• Ensure all required fields are filled\n" +
                                "• Click 'Validate & Upload' to submit",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // Sample Template Button
        OutlinedButton(
            onClick = { jsonText = sampleJson },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = PrimaryBlue
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.ContentCopy,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Load Sample Template", fontWeight = FontWeight.Medium)
        }

        // JSON Input Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(3.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = SurfaceBlueLight,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.DataObject,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp),
                                tint = PrimaryBlue
                            )
                        }
                    }
                    Text(
                        "JSON Data",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    if (jsonText.isNotEmpty()) {
                        IconButton(
                            onClick = { jsonText = "" },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                Icons.Default.Clear,
                                contentDescription = "Clear",
                                tint = ErrorRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = jsonText,
                    onValueChange = { jsonText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 300.dp, max = 500.dp),
                    placeholder = {
                        Text(
                            "Paste your JSON data here...",
                            color = TextHint
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = BorderBlue,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    textStyle = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = FontFamily.Monospace
                    )
                )
            }
        }

        // Character Count
        if (jsonText.isNotEmpty()) {
            Text(
                text = "${jsonText.length} characters",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Upload Button
        Button(
            onClick = { viewModel.uploadFromJson(jsonText) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = jsonText.isNotBlank(),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue,
                disabledContainerColor = BorderBlue
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
                "Validate & Upload",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}