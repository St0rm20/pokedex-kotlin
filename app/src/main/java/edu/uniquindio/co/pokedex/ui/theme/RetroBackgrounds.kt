package edu.uniquindio.co.pokedex.ui.theme

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** Fondo de "campo" verde con puntos, como el del mockup. */
fun Modifier.fieldBackground(): Modifier = drawBehind {
    drawRect(PokedexField)
    val step = 14.dp.toPx()
    var y = 0f
    while (y < size.height) {
        var x = 0f
        while (x < size.width) {
            drawCircle(Color(0xFFB8CF5E), radius = 1.6.dp.toPx(), center = Offset(x, y))
            drawCircle(Color(0xFF93AB30), radius = 1.2.dp.toPx(), center = Offset(x + step / 2, y + step / 2))
            x += step
        }
        y += step
    }
}

/** Fondo crema con rayas horizontales de la pantalla de detalle. */
fun Modifier.stripedBackground(): Modifier = drawBehind {
    drawRect(Color(0xFFEFE6C4))
    val period = 8.dp.toPx()
    val stripe = 2.dp.toPx()
    var y = 6.dp.toPx()
    while (y < size.height) {
        drawRect(Color(0xFFE6DBB2), topLeft = Offset(0f, y), size = Size(size.width, stripe))
        y += period
    }
}

/** Fondo celeste a cuadros del recuadro del sprite. */
fun Modifier.checkerBackground(): Modifier = drawBehind {
    drawRect(Color(0xFFE6F4FD))
    val tile = 16.dp.toPx()
    var row = 0
    var y = 0f
    while (y < size.height) {
        var col = 0
        var x = 0f
        while (x < size.width) {
            if ((row + col) % 2 == 0) {
                drawRect(Color(0xFFBFE3FA), topLeft = Offset(x, y), size = Size(tile, tile))
            }
            x += tile
            col++
        }
        y += tile
        row++
    }
}

/** Aro exterior sin ocupar espacio, equivalente al box-shadow "0 0 Npx color" del mockup. */
fun Modifier.outerRing(color: Color, width: androidx.compose.ui.unit.Dp, cornerRadius: androidx.compose.ui.unit.Dp = 0.dp): Modifier =
    drawBehind {
        val w = width.toPx()
        drawRoundRect(
            color = color,
            topLeft = Offset(-w / 2, -w / 2),
            size = Size(size.width + w, size.height + w),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius.toPx() + w / 2),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = w)
        )
    }
