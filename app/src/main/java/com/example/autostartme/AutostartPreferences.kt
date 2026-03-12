package com.example.autostartme

import android.content.Context
import android.content.SharedPreferences

class AutostartPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("autostart_apps", Context.MODE_PRIVATE)

    fun getApps(): Set<String> {
        return prefs.getStringSet(KEY_APPS, emptySet()) ?: emptySet()
    }

    fun addApp(packageName: String) {
        val apps = getApps().toMutableSet()
        apps.add(packageName)
        prefs.edit().putStringSet(KEY_APPS, apps).apply()
    }

    fun removeApp(packageName: String) {
        val apps = getApps().toMutableSet()
        apps.remove(packageName)
        prefs.edit().putStringSet(KEY_APPS, apps).apply()
    }

    fun isAppAdded(packageName: String): Boolean = getApps().contains(packageName)

    companion object {
        private const val KEY_APPS = "apps"
    }
}
