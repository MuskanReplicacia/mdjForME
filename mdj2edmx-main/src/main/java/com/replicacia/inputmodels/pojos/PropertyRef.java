package com.replicacia.inputmodels.pojos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyRef {

    @XmlAttribute(name = "Name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static PropertyRef createPropertyRef(String name) {
        PropertyRef propertyRef = new PropertyRef();
        propertyRef.setName(name);
        return propertyRef;
    }
}
