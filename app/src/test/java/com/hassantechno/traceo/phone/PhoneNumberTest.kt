package com.hassantechno.traceo.phone

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PhoneNumberTest {
    @Test
    fun normalizesIranianNationalNumber() {
        assertEquals("+989123456789", normalizeIranPhone("09123456789"))
    }

    @Test
    fun normalizesInternationalPrefixes() {
        assertEquals("+989123456789", normalizeIranPhone("+989123456789"))
        assertEquals("+989123456789", normalizeIranPhone("00989123456789"))
        assertEquals("+989123456789", normalizeIranPhone("989123456789"))
    }

    @Test
    fun normalizesPersianAndArabicIndicDigits() {
        assertEquals("+989123456789", normalizeIranPhone("۰۹۱۲۳۴۵۶۷۸۹"))
        assertEquals("+989123456789", normalizeIranPhone("٠٩١٢٣٤٥٦٧٨٩"))
    }

    @Test
    fun ignoresCommonSeparators() {
        assertEquals("+989123456789", normalizeIranPhone("+98 912-345-6789"))
    }

    @Test
    fun rejectsInvalidNumbers() {
        assertNull(normalizeIranPhone(""))
        assertNull(normalizeIranPhone("02123456789"))
        assertNull(normalizeIranPhone("0912345678"))
        assertNull(normalizeIranPhone("091234567890"))
        assertNull(normalizeIranPhone("+98912345678"))
        assertNull(normalizeIranPhone("09hello456789"))
    }

    @Test
    fun acceptsInternationalNumbersWithExplicitPrefix() {
        val us = normalizePhone("+1 (415) 555-2671")
        assertNotNull(us)
        assertEquals("+14155552671", us?.e164)
        assertFalse(us!!.isIranian)

        val uk = normalizePhone("00447911123456")
        assertEquals("+447911123456", uk?.e164)
        assertFalse(uk!!.isIranian)
    }

    @Test
    fun rejectsForeignNumberWithoutCountryCode() {
        assertNull(normalizePhone("14155552671"))
    }

    @Test
    fun formatsIranianNumber() {
        val phone = normalizePhone("0912 345 6789")!!
        assertEquals("+98 912 345 6789", phone.formatted)
        assertEquals("0912 345 6789", phone.localFormatted)
        assertEquals("989123456789", phone.digits)
        assertTrue(phone.isIranian)
    }
}
