package io.github.thatusualguy.ejournal.presentation.component

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.thatusualguy.ejournal.presentation.navigation.NavRoute

@Composable
fun TopBar(
    navController: NavController?,
    onNavigationIconClick: () -> Unit,
    title: String
) {
    if (navController == null)
        return

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute == null
        || currentRoute == NavRoute.Login.route
        || currentRoute == NavRoute.Signup.route
    ) {
        return
    }

    TopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = onNavigationIconClick) {
                Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = null,
                )
            }
        },
    )
}
