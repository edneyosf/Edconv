package edneyosf.edconv.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay

@Composable
fun VibratingIcon(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    amplitude: Float = 2f,
    rotation: Float = 4f,
    duration: Int = 80,
    interval: Long = 1000,
    content: @Composable () -> Unit
) {
    val transition = rememberInfiniteTransition()
    var isVibrating by remember { mutableStateOf(value = true) }

    LaunchedEffect(Unit) {
        while (enabled) {
            isVibrating = true
            delay(timeMillis = interval)
            isVibrating = false
            delay(timeMillis = interval)
        }
    }

    val offsetX = if (isVibrating) {
        transition.animateFloat(
            initialValue = -amplitude,
            targetValue = amplitude,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = duration, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        ).value
    } else 0f

    val rotation = if (isVibrating) {
        transition.animateFloat(
            initialValue = -rotation,
            targetValue = rotation,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = duration * 2, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        ).value
    } else 0f

    Box(
        modifier = modifier.graphicsLayer {
            translationX = offsetX
            rotationZ = rotation
        }
    ) {
        content()
    }
}