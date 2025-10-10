package com.example.familydirectory.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familydirectory.R
import com.example.familydirectory.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    onNavigateToDirectory: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var titleVisible by remember { mutableStateOf(false) }
    var contentVisible by remember { mutableStateOf(false) }
    var buttonVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(300)
        visible = true
        delay(500)
        titleVisible = true
        delay(300)
        contentVisible = true
        delay(400)
        buttonVisible = true
    }

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
    ) {
        // Decorative background patterns
        DecorativeBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo with Animation
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(800)) + scaleIn(
                    initialScale = 0.8f,
                    animationSpec = tween(800, easing = EaseOutBack)
                )
            ) {
                LogoSection()
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Title Card
            AnimatedVisibility(
                visible = titleVisible,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInVertically(
                            initialOffsetY = { -40 },
                            animationSpec = tween(600, easing = EaseOutCubic)
                        )
            ) {
                TitleCard()
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Content Card
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(animationSpec = tween(600)) +
                        slideInVertically(
                            initialOffsetY = { 40 },
                            animationSpec = tween(600, easing = EaseOutCubic)
                        )
            ) {
                ContentCard()
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Enter Button
            AnimatedVisibility(
                visible = buttonVisible,
                enter = fadeIn(animationSpec = tween(600)) +
                        scaleIn(
                            initialScale = 0.8f,
                            animationSpec = tween(400, easing = EaseOutBack)
                        )
            ) {
                EnterButton(onClick = onNavigateToDirectory)
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun LogoSection() {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_animation")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        contentAlignment = Alignment.Center
    ) {
        // Outer glow effect
        Box(
            modifier = Modifier
                .size(260.dp)
                .scale(scale * 1.08f)
                .background(
                    DeepRoyalBlue.copy(alpha = glowAlpha * 0.5f),
                    CircleShape
                )
                .blur(25.dp)
        )

        // Inner glow effect
        Box(
            modifier = Modifier
                .size(250.dp)
                .scale(scale * 1.05f)
                .background(
                    HeritageGold.copy(alpha = glowAlpha * 0.3f),
                    CircleShape
                )
                .blur(15.dp)
        )

        // Main logo
        Surface(
            modifier = Modifier
                .size(240.dp)
                .scale(scale),
            shape = CircleShape,
            color = PureWhite,
            shadowElevation = 16.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.malikudy_logo),
                contentDescription = "Malikudy Kudumbayogam - Estd. 2001",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
fun TitleCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Decorative top border with Kerala pattern
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .width(40.dp)
                        .height(3.dp),
                    color = HeritageGold,
                    shape = RoundedCornerShape(2.dp)
                ) {}

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    modifier = Modifier.size(8.dp),
                    shape = CircleShape,
                    color = DeepRoyalBlue
                ) {}

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    modifier = Modifier
                        .width(40.dp)
                        .height(3.dp),
                    color = HeritageGold,
                    shape = RoundedCornerShape(2.dp)
                ) {}
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main title
            Text(
                text = "മാലിക്കുടി കുടുംബയോഗം",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = DeepRoyalBlue,
                textAlign = TextAlign.Center,
                lineHeight = 38.sp,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Subtitle
            Text(
                text = "ഇരിങ്ങോൾ",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = WarmTerracotta,
                textAlign = TextAlign.Center,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dedication badge
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Brush.horizontalGradient(
                    colors = listOf(
                        HeritageGold.copy(alpha = 0.2f),
                        HeritageGold.copy(alpha = 0.4f),
                        HeritageGold.copy(alpha = 0.2f)
                    )
                ).let { SoftGray },
                shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    HeritageGold.copy(alpha = 0.15f),
                                    HeritageGold.copy(alpha = 0.3f),
                                    HeritageGold.copy(alpha = 0.15f)
                                )
                            )
                        )
                ) {
                    Text(
                        text = "✦ സമർപ്പണം ✦",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = HeritageGold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 14.dp),
                        letterSpacing = 2.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ContentCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Quote section with arch design
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = DeepRoyalBlue.copy(alpha = 0.05f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    // Opening quote mark
                    Text(
                        text = "❝",
                        fontSize = 40.sp,
                        color = HeritageGold,
                        modifier = Modifier.offset(x = (-8).dp, y = (-10).dp)
                    )

                    Text(
                        text = "സഹോദരന്മാർ ഒത്തൊരുമിച്ച് വസിക്കുന്നത് എത്ര മനോഹരവും ശുഭവും ആകുന്നു.",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepRoyalBlue,
                        textAlign = TextAlign.Justify,
                        lineHeight = 28.sp,
                        letterSpacing = 0.3.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "നാം അന്യോന്യം സ്നേഹിക്കുക. സ്നേഹം ദൈവത്തിൽ നിന്ന് വരുന്നു. സ്നേഹിക്കുന്നവനെല്ലാം ദൈവത്തിൽ നിന്നും ജനിച്ചിരിക്കുന്നു.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        color = TextDark,
                        textAlign = TextAlign.Justify,
                        lineHeight = 26.sp,
                        letterSpacing = 0.2.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier
                                .height(1.dp)
                                .width(40.dp),
                            color = HeritageGold
                        ) {}

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "യോഹന്നാൻ ശ്ലീഹാ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = HeritageGold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    // Closing quote mark
                    Text(
                        text = "❞",
                        fontSize = 40.sp,
                        color = HeritageGold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.End)
                            .offset(x = 8.dp, y = (-5).dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Message section
            Text(
                text = "യോഹന്നാൻ ശ്ലീഹായുടെ ഈ വാക്കുകൾ നമുക്കും മാർഗദർശമാകട്ടെ എന്ന ആശംസയോടെ അഭിമാനിക്കാവുന്ന ഒരു പാരമ്പര്യത്തിൻ്റെ അനന്തരാവകാശി ആക്കിത്തീർത്തുകൊണ്ട് കാലയവനികയ്ക്കുള്ളിൽ മറഞ്ഞുപോയ പൂർവ്വപിതാക്കന്മാരുടെ സ്‌മരണയ്ക്കുവേണ്ടി തളിരിട്ടുവരുന്ന ഇളം തലമുറയുടെ അറിവിനുവേണ്ടിയും",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextDark,
                textAlign = TextAlign.Justify,
                lineHeight = 25.sp,
                letterSpacing = 0.2.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Historical section with brick pattern
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = WarmTerracotta.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    WarmTerracotta.copy(alpha = 0.05f),
                                    WarmTerracotta.copy(alpha = 0.12f)
                                )
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(32.dp),
                                shape = CircleShape,
                                color = WarmTerracotta.copy(alpha = 0.2f)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "🏛",
                                        fontSize = 18.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "ചരിത്രം",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = WarmTerracotta,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "ഏകദേശം A.D.1655-ൽ കാലടിക്കടുത്തുള്ള കാഞ്ഞൂർ നിന്നും ഇവിടെ കുടിയേറിപ്പാർത്ത പടയാട്ടി മത്തായി എന്ന പൂർവ്വപിതാവിൻ്റെ വംശാവലിയായ മാലിക്കുടി കുടുംബത്തിന്റെ പാരമ്പര്യം നിലനിർത്തുന്നതിനും ഇളം തലമുറകൾക്ക് കൂടുതൽ പ്രചോദനം നൽകത്തക്കവിധം നവീന പ്രവർത്തന മാതൃകയിൽ പ്രത്യേക വഴിത്തിരിവിലൂടെ നമ്മുടെ കുടുംബയോഗ പ്രയാണം തുടരാം.....",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            color = TextDark,
                            textAlign = TextAlign.Justify,
                            lineHeight = 24.sp,
                            letterSpacing = 0.2.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EnterButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "button_animation")

    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "button_scale"
    )

    val shimmer by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .scale(scale),
        colors = ButtonDefaults.buttonColors(
            containerColor = DeepRoyalBlue
        ),
        shape = RoundedCornerShape(18.dp),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 8.dp,
            pressedElevation = 12.dp
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "കുടുംബ ഡയറക്ടറി കാണുക",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = PureWhite,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Surface(
                    shape = CircleShape,
                    color = HeritageGold,
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = PureWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DecorativeBackground() {
    // Top left decorative pattern
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.05f)
    ) {
        val width = size.width
        val height = size.height

        // Top left arch
        drawArc(
            color = DeepRoyalBlue,
            startAngle = 180f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(-50f, -50f),
            size = androidx.compose.ui.geometry.Size(200f, 200f),
            style = Stroke(width = 8f)
        )

        // Bottom right arch
        drawArc(
            color = HeritageGold,
            startAngle = 0f,
            sweepAngle = 90f,
            useCenter = false,
            topLeft = Offset(width - 150f, height - 150f),
            size = androidx.compose.ui.geometry.Size(200f, 200f),
            style = Stroke(width = 8f)
        )

        // Brick pattern (top right)
        val brickWidth = 40f
        val brickHeight = 20f
        val startX = width - 200f
        val startY = 100f

        for (row in 0..3) {
            val offsetX = if (row % 2 == 0) 0f else brickWidth / 2
            for (col in 0..3) {
                drawRect(
                    color = WarmTerracotta,
                    topLeft = Offset(
                        startX + offsetX + (col * (brickWidth + 4f)),
                        startY + (row * (brickHeight + 4f))
                    ),
                    size = androidx.compose.ui.geometry.Size(brickWidth, brickHeight),
                    style = Stroke(width = 2f)
                )
            }
        }
    }
}