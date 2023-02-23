package com.replicacia.inputmodels.pojos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class NavigationProperty {

    @XmlAttribute(name = "Name")
    private String name;

    @XmlAttribute(name = "Type")
    private String type;

//    @XmlAttribute(name = "Nullable")
//    private boolean nullable;

    @XmlAttribute(name = "Partner")
    private String partner;

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

//    public boolean isNullable() {
//        return nullable;
//    }
//
//    public void setNullable(boolean nullable) {
//        this.nullable = nullable;
//    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
}
