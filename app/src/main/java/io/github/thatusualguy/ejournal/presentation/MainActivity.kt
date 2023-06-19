package io.github.thatusualguy.ejournal.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.thatusualguy.ejournal.domain.repo.AppTheme
import io.github.thatusualguy.ejournal.domain.repo.Settings
import io.github.thatusualguy.ejournal.domain.repo.SettingsState
import io.github.thatusualguy.ejournal.presentation.navigation.MyNavGraph
import io.github.thatusualguy.ejournal.presentation.theme.EJournalTheme
import java.util.logging.Logger

class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()

            val settings = Settings.getAppTheme(LocalContext.current).collectAsState()

//            val isSystemDarkMode = isSystemInDarkTheme()
//            LaunchedEffect(isSystemDarkMode){
//                    Settings.setSystemTheme(isSystemDarkMode)
//            }
//                        val isSystemDarkMode = isSystemInDarkTheme()
//
//            Settings.setSystemTheme(isSystemInDarkTheme())
//            LaunchedEffect(isSystemInDarkTheme()){
//                    Settings.setSystemTheme(isSystemDarkMode)
//            }

            EJournalTheme (darkTheme = isAppDarkTheme()) {
//            EJournalTheme (darkTheme = isDisplayDarkTheme(settings.value)) {
//            EJournalTheme (darkTheme = settings.value.overrideModeDark ?: settings.value.systemModeDark) {
                MyNavGraph(navController = navController)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

//        val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
////        Log.e("MEME","config "+currentNightMode.toString())
//        when (currentNightMode) {
//            Configuration.UI_MODE_NIGHT_NO -> {
//                Settings.setSystemTheme(false)
//
//            } // Night mode is not active, we're using the light theme.
//            Configuration.UI_MODE_NIGHT_YES -> {
//                Settings.setSystemTheme(true)
//
//            } // Night mode is active, we're using dark theme.
//        }

    }
}

sealed class BackPress {
    object Idle : BackPress()
    object InitialTouch : BackPress()
}


@Composable
fun isAppDarkTheme(): Boolean {
    val settings by Settings.getAppTheme(LocalContext.current).collectAsState()

    return when (settings.themeMode) {
        AppTheme.Dark -> true
        AppTheme.Light -> false
        AppTheme.System -> isSystemInDarkTheme()
    }
}


@Composable
fun isDisplayDarkTheme(settings: SettingsState): Boolean {

//    Log.e("MEME","${settings.overrideModeDark.toString()} ?: ${settings.systemModeDark}")

    return settings.overrideModeDark ?: settings.systemModeDark
}