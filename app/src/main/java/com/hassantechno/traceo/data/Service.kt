package com.hassantechno.traceo.data

import androidx.annotation.DrawableRes
import com.hassantechno.traceo.phone.PhoneNumber
import java.net.URLEncoder

enum class ServiceCategory { IRANIAN, INTERNATIONAL }

/** Services that have a documented deep link for opening a chat by phone. */
enum class PhoneLink { WHATSAPP, TELEGRAM, SIGNAL, VIBER }

data class Service(
    val id: String,
    val name: String,
    @DrawableRes val iconRes: Int,
    val category: ServiceCategory,
    val description: String,
    val packageName: String,
    /** ARGB color stored as Long so the model stays free of Compose types. */
    val accent: Long,
    val accentContent: Long = 0xFFFFFFFF,
    val phoneLink: PhoneLink? = null,
    val popular: Boolean = false,
    val note: String? = null,
) {
    val supportsPhoneLink: Boolean get() = phoneLink != null

    /** A scheme that opens the installed app directly on the chat screen. */
    fun appUri(phone: PhoneNumber): String? = when (phoneLink) {
        PhoneLink.WHATSAPP -> "whatsapp://send?phone=${phone.digits}"
        PhoneLink.TELEGRAM -> "tg://resolve?phone=${phone.digits}"
        PhoneLink.SIGNAL -> "sgnl://signal.me/#p/${encode(phone.e164)}"
        PhoneLink.VIBER -> "viber://chat?number=${encode(phone.e164)}"
        null -> null
    }

    /** A browser fallback for services that support one. */
    fun webUri(phone: PhoneNumber): String? = when (phoneLink) {
        PhoneLink.WHATSAPP -> "https://wa.me/${phone.digits}"
        PhoneLink.SIGNAL -> "https://signal.me/#p/${encode(phone.e164)}"
        else -> null
    }
}

private fun encode(value: String): String =
    URLEncoder.encode(value, Charsets.UTF_8.name()).replace("+", "%20")
