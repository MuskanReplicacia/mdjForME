package com.replicacia.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class GenUtilsTest {

    @Test
    public void emptyString_nullCase() {
        assertTrue(GenUtils.emptyString(null));
    }

    @Test
    public void emptyString_emptyCase() {
        assertTrue(GenUtils.emptyString(""));
    }

    @Test
    public void emptyString_falseCase(){
        assertFalse(GenUtils.emptyString("abc"));
    }

    @Test
    public void isAlphaNumeric_nullCase() {
        assertFalse(GenUtils.isAlphaNumeric(null));
    }

    @Test
    public void isAlphaNumeric_emptyCase() {
        assertTrue(GenUtils.isAlphaNumeric(""));
    }

    @Test
    public void isAlphaNumeric_specialCharacters() {
        assertFalse(GenUtils.isAlphaNumeric("abc sd"));
    }

    @Test
    public void isAlphaNumeric_validCase() {
        assertTrue(GenUtils.isAlphaNumeric("abcsd"));
    }

    @Test
    public void Capitalize_nullCase() {
        assertNull(GenUtils.capitalize(null));
    }

    @Test
    public void Capitalize_emptyCase() {
        assertEquals("", GenUtils.capitalize(""));
    }

    @Test
    public void Capitalize_validCase() {
        assertEquals("Abcd", GenUtils.capitalize("abcd"));
    }
}
