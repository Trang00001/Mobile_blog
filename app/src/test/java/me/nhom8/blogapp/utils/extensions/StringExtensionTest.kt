package me.nhom8.blogapp.utils.extensions

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StringExtensionTest {
    @Test
    fun stringExtension_isEmail_ReturnsTrue() {
        val email = "email@gmail.com"
        assertTrue(email.isEmail())
    }

    @Test
    fun stringExtension_isEmail_ReturnsFalse() {
        val email = "email@gmailcom"
        assertFalse(email.isEmail())
    }
}
