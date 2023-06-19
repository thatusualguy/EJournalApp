package io.github.thatusualguy.ejournal.domain.repo

import android.content.Context
import android.preference.PreferenceManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

enum class AppTheme {
    Light,
    Dark,
    System
}

private object PreferencesKeys {
    const val APP_THEME = "app_theme"
}


data class SettingsState(
    val themeMode: AppTheme = AppTheme.System,
    val systemModeDark: Boolean = false,
    val overrideModeDark: Boolean? = null
)

object Settings {
    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState>
        get() = _state

    fun setAppTheme(context: Context, mode: AppTheme) {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreference.edit().putInt(PreferencesKeys.APP_THEME, mode.ordinal).apply()
        _state.update { it.copy(themeMode = mode) }
//        if (mode == AppTheme.System)
//            _state.update { it.copy(overrideModeDark = null) }
//        else
//            _state.update { it.copy(overrideModeDark = mode == AppTheme.Dark) }
    }

    fun getAppTheme(context: Context): StateFlow<SettingsState> {
        val sharedPreference = PreferenceManager.getDefaultSharedPreferences(context)
        val value = sharedPreference.getInt(PreferencesKeys.APP_THEME, AppTheme.System.ordinal)
        _state.update { it.copy(themeMode = AppTheme.values()[value]) }

//        if (state.value.themeMode == AppTheme.System)
//            _state.update { it.copy(overrideModeDark = null) }
//        else
//            _state.update { it.copy(overrideModeDark = state.value.themeMode == AppTheme.Dark) }
        return state
    }

    fun setSystemTheme(isDarkTheme: Boolean) {
        _state.update { it.copy(systemModeDark = isDarkTheme) }
    }

}