package com.replicacia.inputmodels.pojos;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class EntityType {

    @XmlAttribute(name = "Name")
    private String name;

    @XmlAttribute(name = "BaseType")
    private String baseType;

    @XmlElement(name = "Key")
    private Key key;

    @XmlElement(name = "Property")
    private List<Property> properties;

    @XmlElement(name = "NavigationProperty")
    private List<NavigationProperty> navigationProperties = new ArrayList<NavigationProperty>();

	/**
	 * @param navigationProperty the navigationProperty to set
	 */
	public void setNavigationProperty(NavigationProperty navigationProperty) {
		this.navigationProperties.add(navigationProperty);
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public List<NavigationProperty> getNavigationProperties() {
        return navigationProperties;
    }

    public void setNavigationProperties(List<NavigationProperty> navigationProperties) {
        this.navigationProperties = navigationProperties;
    }

    public String getBaseType() {
        return baseType;
    }

    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    public static EntityType CreateEntity(Key key, List<Property> properties, String name) {
        EntityType entityType = new EntityType();
        entityType.setKey(key);
        entityType.setName(name);
        entityType.setProperties(properties);
        return entityType;
    }

}
