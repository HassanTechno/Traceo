package com.hassantechno.traceo.phone

/**
 * A phone number that has been cleaned up and converted to the E.164 shape.
 *
 * Traceo only handles the number on the device. This model deliberately has no
 * Android dependencies so it can be covered by fast JVM unit tests.
 */
data class PhoneNumber(
    val e164: String,
    val countryCode: String,
    val nationalNumber: String,
    val isIranian: Boolean,
) {
    /** Digits only, suitable for services such as WhatsApp. */
    val digits: String get() = e164.removePrefix("+")

    /** A readable Iranian mobile number, e.g. `+98 912 345 6789`. */
    val formatted: String
        get() = if (isIranian && nationalNumber.length == 10) {
            "+98 ${nationalNumber.substring(0, 3)} ${nationalNumber.substring(3, 6)} ${nationalNumber.substring(6)}"
        } else {
            e164
        }

    /** Local Iranian display form, e.g. `0912 345 6789`. */
    val localFormatted: String
        get() = if (isIranian && nationalNumber.length == 10) {
            "0${nationalNumber.substring(0, 3)} ${nationalNumber.substring(3, 6)} ${nationalNumber.substring(6)}"
        } else {
            e164
        }
}

private const val IRAN_CODE = "98"
private const val MIN_E164_DIGITS = 7
private const val MAX_E164_DIGITS = 15

/**
 * Accepts Iranian mobile numbers in local or international form and any
 * international number that begins with `+` or `00`.
 *
 * Examples that normalize successfully:
 *  - `0912 345 6789`
 *  - `۰۹۱۲۳۴۵۶۷۸۹`
 *  - `+98 912 345 6789`
 *  - `0098 912 345 6789`
 *  - `+1 (415) 555-2671`
 */
fun normalizePhone(raw: String): PhoneNumber? {
    val input = raw.trim()
    if (input.isEmpty()) return null

    val digits = StringBuilder()
    var plusCount = 0

    for (character in input) {
        when {
            character.isDigit() -> {
                val value = character.digitToIntOrNull() ?: return null
                digits.append(('0'.code + value).toChar())
            }
            character == '+' -> {
                // A plus sign is only valid before the first digit.
                if (digits.isNotEmpty() || plusCount > 0) return null
                plusCount++
            }
            character.isWhitespace() || character in SEPARATORS -> Unit
            else -> return null
        }
    }

    val rawDigits = digits.toString()
    if (rawDigits.length < MIN_E164_DIGITS) return null

    val explicitInternational = plusCount == 1 || rawDigits.startsWith("00")
    val internationalDigits = if (rawDigits.startsWith("00")) rawDigits.drop(2) else rawDigits

    // Iranian mobile numbers: 9xx xxx xxxx (10 national digits).
    val iranNational = when {
        internationalDigits.startsWith(IRAN_CODE) && internationalDigits.length == 12 ->
            internationalDigits.drop(2)
        internationalDigits.startsWith("0") && internationalDigits.length == 11 ->
            internationalDigits.drop(1)
        internationalDigits.length == 10 ->
            internationalDigits
        else -> null
    }

    if (iranNational != null && iranNational.length == 10 && iranNational.startsWith("9")) {
        val belongsToIran = !explicitInternational ||
            rawDigits.startsWith(IRAN_CODE) ||
            rawDigits.startsWith("00$IRAN_CODE")
        if (belongsToIran) {
            return PhoneNumber(
                e164 = "+$IRAN_CODE$iranNational",
                countryCode = IRAN_CODE,
                nationalNumber = iranNational,
                isIranian = true,
            )
        }
    }

    // Country code 98 belongs to Iran, so an incomplete +98 number must not
    // silently become some other international number.
    if (internationalDigits.startsWith(IRAN_CODE)) return null

    if (!explicitInternational) return null
    if (internationalDigits.length !in MIN_E164_DIGITS..MAX_E164_DIGITS) return null
    if (internationalDigits.startsWith("0")) return null
    if (internationalDigits.all { it == '0' }) return null

    return PhoneNumber(
        e164 = "+$internationalDigits",
        countryCode = "",
        nationalNumber = internationalDigits,
        isIranian = false,
    )
}

/** Backwards-compatible helper used by the original tests. */
fun normalizeIranPhone(raw: String): String? =
    normalizePhone(raw)?.takeIf { it.isIranian }?.e164

private val SEPARATORS = setOf('-', '(', ')', '.', '/', '\u00A0', '\u202F')
