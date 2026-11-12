package com.example.donavida.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TelefonoTest {

    @Test
    fun fijo_seAgrupaComo2_3_2_2() {
        assertEquals("91 301 72 00", formatearTelefono("913017200"))
    }

    @Test
    fun movil_seAgrupaComo3_2_2_2() {
        assertEquals("612 34 56 78", formatearTelefono("612345678"))
    }

    @Test
    fun numeroGratuito_seAgrupaComo3_3_3() {
        assertEquals("900 000 001", formatearTelefono("900 000 001"))
    }

    @Test
    fun siNoSonNueveCifras_seDevuelveTalCual() {
        assertEquals("112", formatearTelefono("112"))
    }
}
