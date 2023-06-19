package io.github.thatusualguy.ejournal.presentation.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.thatusualguy.ejournal.R
import io.github.thatusualguy.ejournal.domain.models.NavigationItem
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.abs


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScaffoldDrawer(
    title: String,
    navigationItems: List<NavigationItem>,
    scope: CoroutineScope,
    navController: NavHostController?,
    scaffoldState: ScaffoldState,
    content: @Composable () -> Unit
) {
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navController = navController,
                onNavigationIconClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                },
                title
            )
        },
        drawerContent = drawerBody(
            navController = navController,
            navigationItems = navigationItems,
            onNavigate = {
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        ),
        drawerShape = MaterialTheme.shapes.large.copy(topStart = CornerSize(0), bottomStart = CornerSize(0)),
        drawerGesturesEnabled = true,
        drawerBackgroundColor = MaterialTheme.colors.background,
    ) {
        content()
    }
}


@Composable
fun drawerBody(
    navController: NavHostController?,
    navigationItems: List<NavigationItem>,
    onNavigate: () -> Unit = {}
): @Composable ColumnScope.() -> Unit {
    val backStackEntry = navController?.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()
    return {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp, top = 32.dp, end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .aspectRatio(1f)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier.matchParentSize(),
                    painter = painterResource(R.drawable.fspo_sign),
                    contentDescription = "",
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            navigationItems.forEach { item ->
                val selected = item.route == backStackEntry?.value?.destination?.route

                DrawerEntry(
                    label = item.name,
                    icon = item.icon,
                    selected = selected,
                    level = item.level,
                    onClick = {
                        coroutineScope.launch {
                            onNavigate.invoke()
                        }
                        item.route?.let { navController?.navigate(it) }
                    })
            }
        }
    }
}

@Composable
fun DrawerEntry(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    level: Int = 1,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .padding(start = 16.dp * abs((level - 1)))
            .clickable { onClick() },
        backgroundColor = if (selected) MaterialTheme.colors.primary else MaterialTheme.colors.surface,
        elevation = 1.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(
                top = 8.dp,
                bottom = 8.dp,
                start = 16.dp,
                end = 16.dp
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
//                tint = if (selected) White else DarkBlue
            )

            Text(
                modifier = Modifier.padding(start = 24.dp),
                text = label,
            )
        }
    }
}

@Preview
@Composable
fun DrawerEntryPreview() {
    val item = NavigationItem(
        name = "Дашборд",
        route = "home",
        icon = Icons.Rounded.Home,
    )

    Column {
        DrawerEntry(
            label = item.name,
            icon = item.icon,
            selected = true,
            level = 1,
            onClick = { })

        DrawerEntry(
            label = item.name,
            icon = item.icon,
            selected = false,
            level = 1,
            onClick = { })
        DrawerEntry(
            label = item.name,
            icon = item.icon,
            selected = true,
            level = 2,
            onClick = { })

        DrawerEntry(
            label = item.name,
            icon = item.icon,
            selected = false,
            level = 2,
            onClick = { })

    }
}

@Preview
@Composable
fun DrawerBodyPreview() {
    val navigationItems = listOf(
        NavigationItem(
            name = "Home",
            route = "home",
            icon = Icons.Rounded.Home,
        ),
        NavigationItem(
            name = "Create",
            route = "add",
            icon = Icons.Rounded.AddCircle,
        ),
        NavigationItem(
            name = "Settings",
            route = "settings",
            icon = Icons.Rounded.Settings,
        ),
    )

    EJournalTheme {
        Column {
            drawerBody(navController = null, navigationItems = navigationItems)
        }
    }
}

@Preview
@Composable
fun ScaffoldDrawerPreview() {
    val navigationItems = listOf(
        NavigationItem(
            name = "Home",
            route = "home",
            icon = Icons.Rounded.Home,
        ),
        NavigationItem(
            name = "Create",
            route = "add",
            icon = Icons.Rounded.AddCircle,
        ),
        NavigationItem(
            name = "Settings",
            route = "settings",
            level = 2,
            icon = Icons.Rounded.Settings,
        ),
    )

    EJournalTheme {
        ScaffoldDrawer(
            title = "Preview",
            navigationItems = navigationItems,
            scope = rememberCoroutineScope(),
            navController = null,
            scaffoldState = rememberScaffoldState()
        ) {
            Text(text = "Content text")
        }
    }
}