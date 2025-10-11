package com.example.familydirectory.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.familydirectory.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAuthScreen(
    onAuthSuccess: () -> Unit,
    onDismiss: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Admin credentials (in production, use proper authentication)
    val ADMIN_USERNAME = "admin"
    val ADMIN_PASSWORD = "1234567"

    fun validateLogin() {
        when {
            username.isBlank() -> errorMessage = "Please enter username"
            password.isBlank() -> errorMessage = "Please enter password"
            username != ADMIN_USERNAME || password != ADMIN_PASSWORD -> {
                errorMessage = "Invalid username or password"
            }
            else -> {
                errorMessage = null
                onAuthSuccess()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "അഡ്മിൻ ലോഗിൻ",
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Text(
                            "Admin Login",
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = PureWhite.copy(alpha = 0.9f)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            "Close",
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
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            SoftGray,
                            PureWhite,
                            SoftGray
                        )
                    )
                )
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Admin Icon with glow
                Box(contentAlignment = Alignment.Center) {
                    // Glow effect
                    Surface(
                        shape = CircleShape,
                        color = DeepRoyalBlue.copy(alpha = 0.2f),
                        modifier = Modifier.size(120.dp)
                    ) {}

                    // Main icon
                    Surface(
                        shape = CircleShape,
                        color = DeepRoyalBlue,
                        modifier = Modifier.size(100.dp),
                        shadowElevation = 8.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                modifier = Modifier.size(56.dp),
                                tint = HeritageGold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "അഡ്മിനിസ്ട്രേറ്റർ ആക്സസ്",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = DeepRoyalBlue
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Administrator Access",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "നിങ്ങളുടെ യോഗ്യതാപത്രങ്ങൾ നൽകുക",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextHint
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Username Field
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = DeepRoyalBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Person,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = DeepRoyalBlue
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "ഉപയോക്തൃനാമം",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepRoyalBlue
                                )
                                Text(
                                    text = "Username",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        OutlinedTextField(
                            value = username,
                            onValueChange = {
                                username = it
                                errorMessage = null
                            },
                            placeholder = { Text("Enter username", color = TextHint) },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepRoyalBlue,
                                unfocusedBorderColor = LightBorder,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password Field
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
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = DeepRoyalBlue.copy(alpha = 0.1f),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Default.Lock,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = DeepRoyalBlue
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "പാസ്‌വേഡ്",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepRoyalBlue
                                )
                                Text(
                                    text = "Password",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                errorMessage = null
                            },
                            placeholder = { Text("Enter password", color = TextHint) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                        tint = TextSecondary
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible)
                                VisualTransformation.None
                            else
                                PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepRoyalBlue,
                                unfocusedBorderColor = LightBorder,
                                focusedTextColor = TextDark,
                                unfocusedTextColor = TextDark
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                // Error Message
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = ErrorRed.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Error,
                                contentDescription = null,
                                tint = ErrorRed
                            )
                            Text(
                                text = errorMessage!!,
                                color = ErrorRed,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button
                Button(
                    onClick = { validateLogin() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepRoyalBlue
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(6.dp)
                ) {
                    Icon(
                        Icons.Default.Login,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            "പ്രവേശിക്കുക",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Login",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Info Card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = DeepRoyalBlue.copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = DeepRoyalBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                "അഡ്മിൻ ആക്സസ് ആവശ്യമാണ്",
                                style = MaterialTheme.typography.bodySmall,
                                color = DeepRoyalBlue,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Admin access required",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}