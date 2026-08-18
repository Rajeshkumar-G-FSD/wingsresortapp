package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBorder
import com.example.ui.theme.ResortCardDark
import com.example.ui.theme.ResortGold
import com.example.ui.theme.ResortGoldLight
import com.example.ui.theme.ResortTealDeep
import kotlinx.coroutines.delay

/**
 * Text load animation that creates a smooth staggered typewriter or fade-glide reveal.
 */
@Composable
fun AnimatedLoadText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onBackground,
    delayMs: Long = 0,
    animateTypewriter: Boolean = false
) {
    var visibleText by remember(text) { mutableStateOf(if (animateTypewriter) "" else text) }
    var isVisible by remember { mutableStateOf(!animateTypewriter) }

    LaunchedEffect(text) {
        if (delayMs > 0) delay(delayMs)
        if (animateTypewriter) {
            visibleText = ""
            text.forEachIndexed { index, _ ->
                delay(20)
                visibleText = text.substring(0, index + 1)
            }
        } else {
            isVisible = true
        }
    }

    if (animateTypewriter) {
        Text(
            text = visibleText,
            style = style,
            color = color,
            modifier = modifier
        )
    } else {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(450, easing = FastOutSlowInEasing)) +
                    slideInVertically(
                        initialOffsetY = { 20 },
                        animationSpec = tween(450, easing = FastOutSlowInEasing)
                    ),
            modifier = modifier
        ) {
            Text(
                text = text,
                style = style,
                color = color
            )
        }
    }
}

/**
 * Shimmer placeholder box for smooth loading transitions
 */
@Composable
fun ShimmerBox(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp)
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerFloat"
    )

    val shimmerColors = listOf(
        ResortTealDeep.copy(alpha = 0.6f),
        ResortGold.copy(alpha = 0.25f),
        ResortTealDeep.copy(alpha = 0.6f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(brush)
    )
}

/**
 * Luxury styled card with subtle golden border and glass-like elevation
 */
@Composable
fun LuxuryCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = ResortCardDark,
    borderColor: Color = CardBorder,
    shape: RoundedCornerShape = RoundedCornerShape(20.dp),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .border(width = 1.dp, color = borderColor, shape = shape),
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        content()
    }
}

/**
 * Luxury status badge with animated pulse or VIP rating
 */
@Composable
fun LuxuryBadge(
    text: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    backgroundColor: Color = ResortGold.copy(alpha = 0.15f),
    contentColor: Color = ResortGoldLight,
    borderColor: Color = ResortGold.copy(alpha = 0.4f)
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(backgroundColor)
            .border(0.8.dp, borderColor, RoundedCornerShape(100.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier
                        .size(13.dp)
                        .padding(end = 4.dp)
                )
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 0.4.sp
                ),
                color = contentColor
            )
        }
    }
}

/**
 * Animated live pulse dot for Realtime DB connection status
 */
@Composable
fun LiveStatusPulse(
    isLive: Boolean = true,
    text: String = "Firebase Live",
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val alphaAnim by transition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val pulseColor = if (isLive) Color(0xFF00E676) else Color(0xFFFFB74D)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(RoundedCornerShape(50.dp))
            .background(Color(0xFF0A1F1C))
            .border(1.dp, Color(0x33D4AF37), RoundedCornerShape(50.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(pulseColor.copy(alpha = alphaAnim))
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp
            ),
            color = if (isLive) Color(0xFFB9F6CA) else Color(0xFFFFE082),
            modifier = Modifier.padding(start = 6.dp)
        )
    }
}
