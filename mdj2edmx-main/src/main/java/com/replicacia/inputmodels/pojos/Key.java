package com.replicacia.inputmodels.pojos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Key {

	@XmlElement(name = "PropertyRef")
    private PropertyRef propertyRef;

    public PropertyRef getPropertyRef() {
        return propertyRef;
    }

    public void setPropertyRef(PropertyRef propertyRef) {
        this.propertyRef = propertyRef;
    }

    public static Key createKey(PropertyRef propertyRef) {
        Key key = new Key();
        key.setPropertyRef(propertyRef);
        return key;
    }
}
