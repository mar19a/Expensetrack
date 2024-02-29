package com.example.expensetrack.ui.screens.shared

import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import coil.compose.AsyncImage

@Composable
fun Avatar(
    model: Any?,
    modifier: Modifier = Modifier,
    placeholder: Painter? = null,
    shape: Shape = CircleShape,
    @FloatRange(from = 0.0, to = 1.0) size: Float = 0.5f,
    onClick: (() -> Unit)? = null,
) {
    BoxWithConstraints(
        modifier = modifier,
        content = {
            AsyncImage(
                model = model,
                error = placeholder,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = shape,
                    )
                    .clip(shape)
                    .background(Color.LightGray)
                    .size(min(maxWidth, maxHeight) * size)
                    .run {
                        onClick?.let {
                            clickable {
                                it()
                            }
                        } ?: Modifier
                    },
                contentDescription = null,
            )
        }
    )
}
