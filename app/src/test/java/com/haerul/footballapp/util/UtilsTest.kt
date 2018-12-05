package com.haerul.footballapp.util

import org.junit.Assert.assertEquals
import org.junit.Test

class UtilsTest {

    @Test
    fun testGetStringDate() {
        assertEquals("Wed, 28 Feb 2018", getStringDate("2018-02-28"))
    }

    @Test
    fun testGetStringTime() {
        assertEquals("08:00 PM", getStringTime("20:00:00+00:00"))
    }
}