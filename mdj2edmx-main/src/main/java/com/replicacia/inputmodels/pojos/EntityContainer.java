package com.replicacia.inputmodels.pojos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class EntityContainer {

    @XmlAttribute(name = "Name")
    private String name;

    @XmlElement(name = "EntitySet")
    private List<EntitySet> entitySet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EntitySet> getEntitySet() {
        return entitySet;
    }

    public void setEntitySet(List<EntitySet> entitySet) {
        this.entitySet = entitySet;
    }
}
