package com.replicacia.validation;

import com.replicacia.util.GenUtils;

import java.util.List;

public class ComplexTypeValidations {
    public String validateComplexTypeName(final String className, final List<String> errMessages) {
        return ClassValidations.validateClassName(className, errMessages);
    }
}
