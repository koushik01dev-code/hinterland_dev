package com.hinterland.app.components.lang

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.LocaleList
import java.util.Locale
import androidx.core.content.edit

class LocaleManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun setLanguage(languageCode: String) {
        prefs.edit { putString(KEY_LANGUAGE, languageCode) }
    }

    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun updateContextLocale(context: Context): Context {
        val locale = Locale(getLanguage())
        Locale.setDefault(locale)
        val resources = context.resources
        val config = resources.configuration
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocales(LocaleList(locale))
            context.createConfigurationContext(config)
        } else {
            @Suppress("DEPRECATION")
            run {
                config.locale = locale
                @Suppress("DEPRECATION")
                resources.updateConfiguration(config, resources.displayMetrics)
                context
            }
        }
    }

    companion object {
        private const val PREFS_NAME = "locale_prefs"
        private const val KEY_LANGUAGE = "language_code"
        private const val DEFAULT_LANGUAGE = "en"

        fun wrap(context: Context): Context {
            return LocaleManager(context).updateContextLocale(context)
        }
    }
}


