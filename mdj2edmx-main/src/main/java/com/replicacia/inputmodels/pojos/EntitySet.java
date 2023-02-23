package com.replicacia.inputmodels.pojos;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class EntitySet {

    @XmlAttribute(name = "Name")
    private String name;

    @XmlAttribute(name = "EntityType")
    private String entityType;

    @XmlElement(name = "NavigationPropertyBinding")
    private List<NavigationPropertyBinding> navigationPropertyBinding;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public List<NavigationPropertyBinding> getNavigationPropertyBinding() {
        return navigationPropertyBinding;
    }

    public void setNavigationPropertyBinding(List<NavigationPropertyBinding> navigationPropertyBinding) {
        this.navigationPropertyBinding = navigationPropertyBinding;
    }
}
