package com.example.familydirectory.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.familydirectory.ui.theme.*

object PhoneCallHelper {

    fun makeCall(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        context.startActivity(intent)
    }

    fun sendSms(context: Context, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
        }
        context.startActivity(intent)
    }

    fun sendEmail(context: Context, email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        context.startActivity(intent)
    }
}

@Composable
fun ClickablePhoneNumber(
    phoneNumber: String,
    label: String = "ഫോൺ",
    sublabel: String = "Phone"
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        color = Color.Transparent
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
                        imageVector = Icons.Default.Phone,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = phoneNumber,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DeepRoyalBlue,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        Icons.Default.TouchApp,
                        contentDescription = "Click to call",
                        modifier = Modifier.size(16.dp),
                        tint = DeepRoyalBlue.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }

    if (showDialog) {
        PhoneActionDialog(
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
fun PhoneActionDialog(
    phoneNumber: String,
    onDismiss: () -> Unit,
    onCall: () -> Unit,
    onSms: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Surface(
                shape = CircleShape,
                color = DeepRoyalBlue,
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Default.Phone,
                        contentDescription = null,
                        tint = PureWhite,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "ബന്ധപ്പെടുക",
                    fontWeight = FontWeight.Bold,
                    color = DeepRoyalBlue
                )
                Text(
                    text = "Contact",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = SoftGray
                ) {
                    Text(
                        text = phoneNumber,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepRoyalBlue,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onCall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PureWhite
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("വിളിക്കുക / Call", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onSms,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = PureWhite
                ),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, DeepRoyalBlue)
            ) {
                Icon(Icons.Default.Message, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("SMS", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = PureWhite,
        shape = RoundedCornerShape(20.dp)
    )
}

@Composable
fun ClickableEmail(
    email: String,
    label: String = "ഇമെയിൽ",
    sublabel: String = "Email"
) {
    val context = LocalContext.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { PhoneCallHelper.sendEmail(context, email) },
        color = Color.Transparent
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
                        imageVector = Icons.Default.Email,
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
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodyLarge,
                        color = DeepRoyalBlue,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        Icons.Default.TouchApp,
                        contentDescription = "Click to email",
                        modifier = Modifier.size(16.dp),
                        tint = DeepRoyalBlue.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}