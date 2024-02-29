package com.example.expensetrack.ui.screens.shared

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.expensetrack.ui.theme.Theme

@Composable
fun AppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {},
    modifier: Modifier = Modifier,
    navigateBack: (() -> Unit)? = null,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = navigateBack?.let { goBack ->
            {
                IconButton(
                    onClick = goBack,
                    content = {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = null,
                        )
                    }
                )
            }
        },
        title = {
            Text(
                text = title,
                color = Theme.colors.onBackground,
            )
        },
        actions = {
            actions()
            Spacer(modifier = Modifier.width(12.dp))
        },
        backgroundColor = Theme.colors.background,
    )
}

@Composable
fun AppBarButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonDefaults.textButtonColors(
            backgroundColor = Theme.colors.primary.copy(alpha = 0.4f),
        ),
        contentPadding = PaddingValues(4.dp),
        shape = Theme.shapes.medium,
        content = {
            Text(
                text = text.uppercase(),
                style = Theme.typography.caption,
            )
        }
    )
}

@Composable
fun AppBarIconButton(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        content = {
            Icon(
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(CircleShape),
                imageVector = icon,
                contentDescription = null,
            )
        }
    )
}
