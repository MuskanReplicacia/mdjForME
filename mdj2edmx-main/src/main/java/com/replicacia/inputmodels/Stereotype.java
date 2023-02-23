package com.replicacia.inputmodels;

public enum Stereotype {

    COMPLEXTYPE("complextype"), ENUMTYPE("enumtype"), ENTITYTYPE("entitytype"), ANONYMOUS("anonymous");

    public String getValue() {
        return value;
    }

    private final String value;

    Stereotype(final String value) {
        this.value = value;
    }
}
