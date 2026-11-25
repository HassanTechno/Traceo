package com.hassantechno.traceo.data

import com.hassantechno.traceo.phone.PhoneNumber
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ServiceLinkTest {
    private val iranian = PhoneNumber(
        e164 = "+989123456789",
        countryCode = "98",
        nationalNumber = "9123456789",
        isIranian = true,
    )
    private val international = PhoneNumber(
        e164 = "+14155552671",
        countryCode = "",
        nationalNumber = "14155552671",
        isIranian = false,
    )

    @Test
    fun whatsappUsesDigitsWithoutPlus() {
        val service = serviceById("whatsapp")!!
        assertEquals("whatsapp://send?phone=989123456789", service.appUri(iranian))
        assertEquals("https://wa.me/989123456789", service.webUri(iranian))
        assertEquals("https://wa.me/14155552671", service.webUri(international))
    }

    @Test
    fun telegramUsesPhoneParameter() {
        val service = serviceById("telegram")!!
        assertEquals("tg://resolve?phone=989123456789", service.appUri(iranian))
        assertNull(service.webUri(iranian))
    }

    @Test
    fun signalAndViberEncodeThePlusSign() {
        assertEquals(
            "sgnl://signal.me/#p/%2B989123456789",
            serviceById("signal")!!.appUri(iranian),
        )
        assertEquals(
            "viber://chat?number=%2B989123456789",
            serviceById("viber")!!.appUri(iranian),
        )
    }

    @Test
    fun servicesWithoutPhoneLinkReturnNull() {
        val instagram = serviceById("instagram")!!
        assertNull(instagram.appUri(iranian))
        assertNull(instagram.webUri(iranian))
    }

    @Test
    fun directLinkCatalogueIsNotEmpty() {
        assertEquals(4, directLinkServices.size)
    }
}
