package com.replicacia.inputmodels.pojos;

import java.util.List;
import java.util.concurrent.CompletionException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Schema {
	
	@XmlAttribute(name="xmlns")
	private String localNs = "http://docs.oasis-open.org/odata/ns/edm";
	
    @XmlAttribute(name = "Namespace")
    private String namespace;

    @XmlElement(name = "EntityType")
    private List<EntityType> entityType;

    @XmlElement(name = "EntityContainer")
    private EntityContainer entityContainer;

    @XmlElement(name = "ComplexType")
    private List<ComplexType> complexType;

    @XmlElement(name = "EnumType")
    private List<EnumType> enumType;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public List<EntityType> getEntityType() {
        return entityType;
    }

    public void setEntityType(List<EntityType> entityType) {
        this.entityType = entityType;
    }

    public EntityContainer getEntityContainer() {
        return entityContainer;
    }

    public void setEntityContainer(EntityContainer entityContainer) {
        this.entityContainer = entityContainer;
    }

    public List<ComplexType> getComplexType() {
        return complexType;
    }

    public void setComplexType(List<ComplexType> complexType) {
        this.complexType = complexType;
    }

    public List<EnumType> getEnumType() {
        return enumType;
    }

    public void setEnumType(List<EnumType> enumType) {
        this.enumType = enumType;
    }
}
