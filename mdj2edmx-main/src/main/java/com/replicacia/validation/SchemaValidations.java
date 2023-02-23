package com.replicacia.validation;

import com.replicacia.util.GenUtils;

import java.util.List;
import java.util.Map;

public class SchemaValidations {
    public static final String DEFAULT_SCHEMA_NAME = "Myproject";

    public static String validateSchemaName(final String name, List<String> errMessageMap) {
        if(GenUtils.emptyString(name)) {
            errMessageMap.add("ModelName is missing(Optional): Using default name \"myproject\"");
            return DEFAULT_SCHEMA_NAME;
        }
        if(!GenUtils.isAlphaNumeric(name)) {
            errMessageMap.add(String.format("ModelName %s has special characters : We support alphanumeric characters( a to z, A to Z, 0 to 9) , please remove special characters, for unblocking you we will use default name \"myproject\"", name));
            return DEFAULT_SCHEMA_NAME;
        }
        return GenUtils.capitalize(name);
    }
}
