package com.replicacia.util;

import com.replicacia.inputmodels.exception.CustomException;
import org.apache.commons.lang3.StringUtils;

public class GenUtils {
    public static boolean emptyString(final String name) {
        return StringUtils.isEmpty(name);
    }
    public static boolean isAlphaNumeric(final String name) {
        return name!=null && name.matches("^[a-zA-Z0-9]*$");

    }
    public static String capitalize(final String name) {
        return StringUtils.capitalize(name);
    }
}
