package com.example.familydirectory.ui.quotes

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.familydirectory.R
import com.example.familydirectory.data.repository.BibleQuotesRepository
import com.example.familydirectory.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuotesScreen() {
    val todayQuote = remember { BibleQuotesRepository.getTodayQuote() }
    var visible by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
    val malayalamDateFormat = SimpleDateFormat("EEEE", Locale("ml", "IN"))

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(300)
        visible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "ദൈനംദിന തിരുവചനം",
                            fontWeight = FontWeight.Bold,
                            color = PureWhite,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize
                        )
                        Text(
                            "Daily Bible Verse",
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = PureWhite.copy(alpha = 0.9f)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepRoyalBlue
                ),
                actions = {
                    IconButton(onClick = { /* Share functionality */ }) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Share",
                            tint = HeritageGold
                        )
                    }
                }
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
            // Decorative background
            DecorativeBibleBackground()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(800)) + scaleIn(
                        initialScale = 0.9f,
                        animationSpec = tween(600, easing = EaseOutBack)
                    )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Date Header
                        DateHeader(
                            englishDate = dateFormat.format(calendar.time),
                            malayalamDay = try {
                                malayalamDateFormat.format(calendar.time)
                            } catch (e: Exception) {
                                dateFormat.format(calendar.time)
                            }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Bible Icon with Animation
                        BibleIcon()

                        Spacer(modifier = Modifier.height(32.dp))

                        // Quote Card
                        QuoteCard(quote = todayQuote)
                    }
                }
            }
        }
    }
}

@Composable
fun DateHeader(
    englishDate: String,
    malayalamDay: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = PureWhite
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            DeepRoyalBlue,
                            RoyalBlueLight
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = HeritageGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ഇന്നത്തെ ദിനം",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = PureWhite,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = englishDate,
                    fontSize = 15.sp,
                    color = PureWhite.copy(alpha = 0.95f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun BibleIcon() {
    val infiniteTransition = rememberInfiniteTransition(label = "bible_glow")

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )

    Box(
        contentAlignment = Alignment.Center
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(
                    HeritageGold.copy(alpha = glowAlpha),
                    CircleShape
                )
                .blur(20.dp)
        )

        // Bible icon
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = DeepRoyalBlue,
            shadowElevation = 12.dp,
            modifier = Modifier
                .size(80.dp)
                .rotate(rotation)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                DeepRoyalBlue,
                                RoyalBlueLight
                            )
                        )
                    )
            ) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = "Bible",
                    tint = HeritageGold,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        // Cross overlay
        Icon(
            Icons.Default.Add,
            contentDescription = null,
            tint = PureWhite,
            modifier = Modifier
                .size(30.dp)
                .offset(y = 2.dp)
        )
    }
}

@Composable
fun QuoteCard(quote: com.example.familydirectory.data.model.BibleQuote) {
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
                            DeepRoyalBlue.copy(alpha = 0.05f),
                            PureWhite
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier.padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Opening quote decoration
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "❝",
                        fontSize = 48.sp,
                        color = HeritageGold,
                        modifier = Modifier.offset(x = (-8).dp, y = (-12).dp)
                    )

                    Surface(
                        shape = CircleShape,
                        color = DeepRoyalBlue.copy(alpha = 0.1f),
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "✟",
                                fontSize = 24.sp,
                                color = DeepRoyalBlue
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Malayalam text
                Text(
                    text = quote.malayalamText,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepRoyalBlue,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                    letterSpacing = 0.3.sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Divider with ornament
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier
                            .width(50.dp)
                            .height(2.dp),
                        color = HeritageGold
                    ) {}

                    Spacer(modifier = Modifier.width(12.dp))

                    Surface(
                        shape = CircleShape,
                        color = HeritageGold,
                        modifier = Modifier.size(8.dp)
                    ) {}

                    Spacer(modifier = Modifier.width(12.dp))

                    Surface(
                        modifier = Modifier
                            .width(50.dp)
                            .height(2.dp),
                        color = HeritageGold
                    ) {}
                }

                Spacer(modifier = Modifier.height(20.dp))

                // English text
                Text(
                    text = quote.englishText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextDark,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 0.2.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Reference badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Brush.horizontalGradient(
                        colors = listOf(
                            DeepRoyalBlue,
                            RoyalBlueLight
                        )
                    ).let { DeepRoyalBlue }
                ) {
                    Box(
                        modifier = Modifier
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        DeepRoyalBlue,
                                        RoyalBlueLight
                                    )
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = quote.malayalamReference,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = PureWhite,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = quote.reference,
                                fontSize = 13.sp,
                                color = PureWhite.copy(alpha = 0.9f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Closing quote decoration
                Text(
                    text = "❞",
                    fontSize = 48.sp,
                    color = HeritageGold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                        .offset(x = 8.dp, y = (-8).dp)
                )
            }
        }
    }
}

@Composable
fun DecorativeBibleBackground() {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.04f)
    ) {
        val width = size.width
        val height = size.height

        // Cross pattern top left
        drawLine(
            color = DeepRoyalBlue,
            start = Offset(100f, 50f),
            end = Offset(100f, 150f),
            strokeWidth = 8f
        )
        drawLine(
            color = DeepRoyalBlue,
            start = Offset(50f, 100f),
            end = Offset(150f, 100f),
            strokeWidth = 8f
        )

        // Dove symbol bottom right
        val dovePath = Path().apply {
            moveTo(width - 150f, height - 100f)
            cubicTo(
                width - 120f, height - 120f,
                width - 100f, height - 120f,
                width - 80f, height - 100f
            )
        }
        drawPath(
            path = dovePath,
            color = HeritageGold,
            style = Stroke(width = 6f)
        )

        // Decorative circles
        drawCircle(
            color = WarmTerracotta,
            radius = 40f,
            center = Offset(width - 100f, 150f),
            style = Stroke(width = 4f)
        )
    }
}