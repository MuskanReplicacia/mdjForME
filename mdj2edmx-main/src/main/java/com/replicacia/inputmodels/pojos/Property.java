package com.replicacia.inputmodels.pojos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Property {

    @XmlAttribute(name = "Name")
    private String name;

    @XmlAttribute(name = "Type")
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static Property createProperty(String name, String type) {
        Property property  = new Property();
        property.setName(name);
        property.setType(type);
        return property;
    }
}
