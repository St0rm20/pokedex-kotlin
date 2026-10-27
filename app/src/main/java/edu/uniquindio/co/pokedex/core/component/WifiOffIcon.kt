package edu.uniquindio.co.pokedex.core.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.unit.Dp

private const val WIFI_OFF_PATH =
    "M3 9a14 14 0 0 1 6-3M21 9a14 14 0 0 0-5-3M6 13a9 9 0 0 1 4-2M18 13a9 9 0 0 0-2-2" +
        "M10 17a4 4 0 0 1 4 0M12 21h.01M3 3l18 18"

/** Icono de wifi tachado del mockup (viewBox 24 x 24). */
@Composable
fun WifiOffIcon(size: Dp, color: Color, strokeWidth: Float, modifier: Modifier = Modifier) {
    val path = remember { PathParser().parsePathString(WIFI_OFF_PATH).toPath() }
    Canvas(modifier = modifier.size(size)) {
        val factor = this.size.width / 24f
        scale(factor, pivot = androidx.compose.ui.geometry.Offset.Zero) {
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Square, join = StrokeJoin.Miter)
            )
        }
    }
}
