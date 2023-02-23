package com.replicacia.validation;

import java.util.List;

public class EnumTypeValidations {
    public String validateEnumTypeName(final String className, final List<String> errMessages) {
        return ClassValidations.validateClassName(className, errMessages);
    }
}
