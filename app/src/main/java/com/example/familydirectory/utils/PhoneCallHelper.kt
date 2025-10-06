package com.example.familydirectory.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.familydirectory.ui.detail.InfoRow

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
    label: String = "Phone"
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.clickable {
            showDialog = true
        }
    ) {
        InfoRow(
            icon = Icons.Default.Phone,
            label = label,
            value = phoneNumber
        )
    }

    // ✅ Dialog appears after click
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
        title = { Text("Contact") },
        text = { Text(phoneNumber) },
        confirmButton = {
            TextButton(onClick = onCall) { Text("Call") }
        },
        dismissButton = {
            TextButton(onClick = onSms) { Text("SMS") }
        }
    )
}

@Composable
fun ClickableEmail(
    email: String,
    label: String = "Email"
) {
    val context = LocalContext.current

    Box(
        modifier = Modifier.clickable {
            PhoneCallHelper.sendEmail(context, email)
        }
    ) {
        InfoRow(
            icon = Icons.Default.Email,
            label = label,
            value = email
        )
    }
}
