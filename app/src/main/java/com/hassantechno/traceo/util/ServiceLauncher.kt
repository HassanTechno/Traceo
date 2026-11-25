package com.hassantechno.traceo.util

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.hassantechno.traceo.data.Service
import com.hassantechno.traceo.phone.PhoneNumber

enum class LaunchResult {
    OPENED_APP,
    OPENED_BROWSER,
    OPENED_STORE,
    FAILED,
}

fun isServiceInstalled(context: Context, service: Service): Boolean {
    val manager = context.packageManager
    if (manager.getLaunchIntentForPackage(service.packageName) != null) return true
    return try {
        manager.getPackageInfo(service.packageName, 0)
        true
    } catch (_: Exception) {
        false
    }
}

/**
 * Tries the direct chat link first, then the app, then a web fallback and
 * finally the store. Every step is guarded because packages and stores are
 * not guaranteed to exist on every device.
 */
fun launchService(context: Context, service: Service, phone: PhoneNumber?): LaunchResult {
    val deepLink = phone?.let(service::appUri)
    if (deepLink != null && context.tryStart(Intent(Intent.ACTION_VIEW, Uri.parse(deepLink)))) {
        return LaunchResult.OPENED_APP
    }

    val webLink = phone?.let(service::webUri)
    if (webLink != null && context.tryStart(Intent(Intent.ACTION_VIEW, Uri.parse(webLink)))) {
        return LaunchResult.OPENED_BROWSER
    }

    val launchIntent = context.packageManager.getLaunchIntentForPackage(service.packageName)
    if (launchIntent != null && context.tryStart(launchIntent)) {
        return LaunchResult.OPENED_APP
    }

    if (openStore(context, service.packageName)) return LaunchResult.OPENED_STORE
    return LaunchResult.FAILED
}

fun openStore(context: Context, packageName: String): Boolean {
    val candidates = listOf(
        "bazaar://details?id=$packageName",
        "myket://details?id=$packageName",
        "market://details?id=$packageName",
        "https://play.google.com/store/apps/details?id=$packageName",
    )
    for (url in candidates) {
        if (context.tryStart(Intent(Intent.ACTION_VIEW, Uri.parse(url)))) return true
    }
    return false
}

fun copyToClipboard(context: Context, label: String, text: String): Boolean {
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        ?: return false
    manager.setPrimaryClip(ClipData.newPlainText(label, text))
    return true
}

fun readClipboardText(context: Context): String? {
    val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
        ?: return null
    val clip = manager.primaryClip ?: return null
    if (clip.itemCount == 0) return null
    return clip.getItemAt(0).coerceToText(context)?.toString()?.trim()?.takeIf { it.isNotEmpty() }
}

fun shareText(context: Context, text: String, title: String): Boolean {
    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    return context.tryStart(Intent.createChooser(sendIntent, title))
}

fun openUrl(context: Context, url: String): Boolean =
    context.tryStart(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

private fun Context.tryStart(intent: Intent): Boolean = try {
    if (this !is Activity) intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(intent)
    true
} catch (_: Exception) {
    false
}
