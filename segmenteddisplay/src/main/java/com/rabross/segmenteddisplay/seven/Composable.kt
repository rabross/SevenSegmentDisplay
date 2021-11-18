package com.rabross.segmenteddisplay.seven

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rabross.segmenteddisplay.delimiter.BinaryDecoder as DelimiterBinaryDecoder
import com.rabross.segmenteddisplay.Led
import com.rabross.segmenteddisplay.SingleColorLed
import com.rabross.segmenteddisplay.delimiter.Delimiter

@Preview
@Composable
fun SevenSegmentDisplayPreview() {
    Surface {
        SevenSegmentDisplay()
    }
}

@Preview
@Composable
fun DigitalClockDisplayPreview() {
    Surface {
        DigitalClockSevenSegmentDisplay()
    }
}

@Composable
fun SevenSegmentDisplay(
    modifier: Modifier = Modifier,
    segmentScale: Int = 3,
    led: Led = SingleColorLed(Color.Red, Color.DarkGray.copy(alpha = 0.3f)),
    decoder: Decoder = BinaryDecoder()
) {
    Canvas(modifier = modifier
        .aspectRatio((1f + segmentScale + 1f) / (1f + segmentScale + 1f + segmentScale + 1f), true)
        .size(100.dp)) {

        val scaleWidth = 1 + segmentScale + 1
        val scaleHeight = 1 + segmentScale + 1 + segmentScale + 1

        val segmentWidthByWidth = size.width / scaleWidth
        val segmentWidthByHeight = size.height / scaleHeight

        val segmentWidth = if (scaleHeight * segmentWidthByWidth < size.height) segmentWidthByWidth else segmentWidthByHeight
        val segmentLength = segmentScale * segmentWidth

        val horizontalSegmentSize = Size(segmentLength, segmentWidth)
        val verticalSegmentSize = Size(segmentWidth, segmentLength)

        val aOffset = Offset(segmentWidth, 0f)
        val bOffset = Offset(segmentLength + segmentWidth, segmentWidth)
        val cOffset = Offset(segmentLength + segmentWidth, segmentLength + segmentWidth + segmentWidth)
        val dOffset = Offset(segmentWidth, segmentLength + segmentWidth + segmentLength + segmentWidth)
        val eOffset = Offset(0f, segmentLength + segmentWidth + segmentWidth)
        val fOffset = Offset(0f, segmentWidth)
        val gOffset = Offset(segmentWidth, segmentLength + segmentWidth)

        drawHorizontalSegment(led.signal(decoder.a), aOffset, horizontalSegmentSize)
        drawVerticalSegment(led.signal(decoder.b), bOffset, verticalSegmentSize)
        drawVerticalSegment(led.signal(decoder.c), cOffset, verticalSegmentSize)
        drawHorizontalSegment(led.signal(decoder.d), dOffset, horizontalSegmentSize)
        drawVerticalSegment(led.signal(decoder.e), eOffset, verticalSegmentSize)
        drawVerticalSegment(led.signal(decoder.f), fOffset, verticalSegmentSize)
        drawHorizontalSegment(led.signal(decoder.g), gOffset, horizontalSegmentSize)
    }
}

private fun DrawScope.drawHorizontalSegment(color: Color, offset: Offset, size: Size) {
    val radius = size.minDimension / 2
    val centerX: Float = offset.x + size.width / 2
    val centerY: Float = offset.y + size.height / 2
    val spacing = radius / 10
    val hexagonPath = Path()
    hexagonPath.moveTo(centerX, centerY + radius - spacing)
    hexagonPath.lineTo(centerX - size.width/2, centerY + radius - spacing)
    hexagonPath.lineTo(centerX - size.width/2 - radius + spacing, centerY)
    hexagonPath.lineTo(centerX - size.width/2, centerY - radius + spacing)
    hexagonPath.lineTo(centerX + size.width/2, centerY - radius + spacing)
    hexagonPath.lineTo(centerX + size.width/2 + radius - spacing, centerY)
    hexagonPath.lineTo(centerX + size.width/2, centerY + radius - spacing)
    hexagonPath.moveTo(centerX, centerY + radius - spacing)
    drawPath(hexagonPath, color)
}

private fun DrawScope.drawVerticalSegment(color: Color, offset: Offset, size: Size) {
    val radius = size.minDimension / 2
    val centerX: Float = offset.x + size.width / 2
    val centerY: Float = offset.y + size.height / 2
    val spacing = radius / 10
    val hexagonPath = Path()
    hexagonPath.moveTo(centerX, centerY + radius + size.height/2 - spacing)
    hexagonPath.lineTo(centerX - radius + spacing, centerY + size.height/2)
    hexagonPath.lineTo(centerX - radius + spacing, centerY - size.height/2)
    hexagonPath.lineTo(centerX, centerY - radius - size.height/2 + spacing)
    hexagonPath.lineTo(centerX + radius - spacing, centerY - size.height/2)
    hexagonPath.lineTo(centerX + radius - spacing, centerY + size.height/2)
    hexagonPath.moveTo(centerX, centerY + radius + size.height/2 - spacing)
    drawPath(hexagonPath, color)
}

@Composable
fun DigitalClockSevenSegmentDisplay(
    modifier: Modifier = Modifier,
    hourFirst: Int = 0,
    hourSecond: Int = 0,
    minuteFirst: Int = 0,
    minuteSecond: Int = 0,
    secondFirst: Int = 0,
    secondSecond: Int = 0,
    delimiterSignal: Int = 0
) {
    Row(modifier = modifier) {
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = BinaryDecoder(hourFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = BinaryDecoder(hourSecond)
        )
        Delimiter(modifier = Modifier.weight(1f).padding(4.dp),
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = BinaryDecoder(minuteFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = BinaryDecoder(minuteSecond)
        )
        Delimiter(modifier = Modifier.weight(1f).padding(4.dp),
            decoder = DelimiterBinaryDecoder(delimiterSignal)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = BinaryDecoder(secondFirst)
        )
        SevenSegmentDisplay(
            modifier = Modifier.weight(1f).padding(4.dp),
            decoder = BinaryDecoder(secondSecond)
        )
    }
}