package com.replicacia.inputmodels.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.replicacia.inputmodels.DataTypeMapping;
import com.replicacia.inputmodels.pojos.*;
import com.replicacia.util.ExceptionMessages;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class EntityTypeMapper {
    /**
     * @param eType
     * @param entityTypesById
     * @param id
     * @param classNode
     * @return returns existing entity type with this id. An entitytype is added while adding Nav Prop if the source class does not exist
     */
    public static EntityType addEntityType(Map<String, EntityType> entityTypesById, String id, JsonNode classNode) {
        EntityType eTypeFromMap = entityTypesById.get(id);
        if (eTypeFromMap == null) {
            EntityType eType = new EntityType();
            eType.setName(StringUtils.capitalize(classNode.path("name").asText()));
            entityTypesById.put(id, eType);
            return eType;
        } else {
            return eTypeFromMap;
        }
    }

    /**
     * @param messages
     * @param entityType
     * @param eTypeName
     * @param attributeNodes
     * @param complexTypesById
     * @param enumTypesById
     * @param schemaName
     */
    public static void extractFields(Map<String, String> messages, EntityType entityType, String eTypeName,
                                     JsonNode attributeNodes, Map<String, ComplexType> complexTypesById, Map<String, EnumType> enumTypesById, String schemaName) {
        List<Property> properties = new ArrayList<>();
        boolean isKeyPresent = false;
        if (attributeNodes.isArray()) {
            for (JsonNode attributeNode : attributeNodes) {
                Property property = new Property();
                String propertyName = StringUtils.capitalize(attributeNode.path("name").asText());
                property.setName(propertyName);
                String finalUserType = null;
                if (attributeNode.path("type").asText() != null
                        && attributeNode.path("type").asText().equals("")) {
                    JsonNode typeNode = attributeNode.path("type");
                    if (typeNode != null && typeNode.path("$ref").asText() != null
                            && !typeNode.path("$ref").asText().equals("")) {
                        if (complexTypesById.get(typeNode.path("$ref").asText()) != null) {
                            finalUserType = complexTypesById.get(typeNode.path("$ref").asText()).getName();
                        } else if (enumTypesById.get(typeNode.path("$ref").asText()) != null) {
                            finalUserType = enumTypesById.get(typeNode.path("$ref").asText()).getName();
                        }
                    }
                }
                String userType = finalUserType != null ? finalUserType : attributeNode.path("type").asText();
                String edmxDataType = Arrays.stream(DataTypeMapping.values()).filter(dtm -> userType.toUpperCase().equalsIgnoreCase(dtm.name())).map(DataTypeMapping::getEdmxDataType).findFirst().orElse(null);
                if (edmxDataType == null) {
                    for (String type : DataTypeMapping.collectionTypesSupported.keySet()) {
                        if (type.equalsIgnoreCase(userType)) {
                            edmxDataType = DataTypeMapping.collectionTypesSupported.get(type);
                            break;
                        }
                    }
                }
                if (edmxDataType == null) {
                    for (ComplexType complexType : complexTypesById.values()) {
                        if (complexType.getName().equalsIgnoreCase(userType)) {
                            edmxDataType = schemaName + "." + complexType.getName();
                            break;
                        }
                    }
                }
                if (edmxDataType == null) {
                    for (EnumType enumType : enumTypesById.values()) {
                        if (enumType.getName().equalsIgnoreCase(userType)) {
                            edmxDataType = schemaName + "." + enumType.getName();
                            break;
                        }
                    }
                }
                if (edmxDataType == null) {
                    String collectionType = DataTypeMapping.isCollectionType(userType);
                    if (collectionType != null) {
                        String userTypeStripped = DataTypeMapping.extractCollectionType(userType);
                        for (ComplexType ctype : complexTypesById.values()) {
                            if (ctype.getName().toUpperCase().equals(userTypeStripped.toUpperCase())) {
                                edmxDataType = String.format("Collection(%s)", schemaName + "." + ctype.getName());
                                break;
                            }
                        }
                        if (edmxDataType == null) {
                            for (EnumType enumType : enumTypesById.values()) {
                                if (enumType.getName().equalsIgnoreCase(userTypeStripped)) {
                                    edmxDataType = String.format("Collection(%s)", schemaName + "." + enumType.getName());
                                    break;
                                }
                            }
                        }
                    }
                }
                if (edmxDataType == null || edmxDataType.trim().isEmpty()) {
                    messages.put(eTypeName, ExceptionMessages.MISSING_PROPERTY_TYPE + propertyName);
                } else
                    property.setType(edmxDataType);

                if (attributeNode.path("isID").asBoolean()) {
                    PropertyRef propertyRef = new PropertyRef();
                    propertyRef.setName(propertyName);
                    Key key = new Key();
                    key.setPropertyRef(propertyRef);
                    entityType.setKey(key);
                    isKeyPresent = true;
                }
                properties.add(property);
            }
        }
        if (!isKeyPresent) {
            // add missing key message with class name
            messages.put(eTypeName, ExceptionMessages.MISSING_KEY);
        }
        entityType.setProperties(properties);
    }

    public static void extractEntityBaseType(EntityType entityType, JsonNode ownedElements, Map<String, EntityType> classNodesMapById, String schemaName) {
        if (ownedElements != null && ownedElements.isArray() && ownedElements.size() > 0) {
            for (JsonNode element : ownedElements) {
                if (element.path("_type").asText().equals("UMLGeneralization")) {
                    JsonNode target = element.path("target");
                    String baseTypeId = target.path("$ref").asText();
                    EntityType baseType = classNodesMapById.get(baseTypeId);
                    entityType.setBaseType(schemaName + "." + baseType.getName());
                }
            }
        }
    }

    public static boolean cardinalityMultiple(final String multiplicity) {
        return "0..*".equals(multiplicity) || "1..*".equals(multiplicity);
    }

    public static boolean cardinalityZero(final String multiplicity) {
        return "0".equals(multiplicity) || "0..0".equals(multiplicity);
    }

    public static boolean cardinalityOne(final String multiplicity) {
        return "0..1".equals(multiplicity) || "1".equals(multiplicity) || "1..1".equals(multiplicity) || "".equals(multiplicity);
    }

    /**
     * @param navigationProperties
     * @param schemaName
     * @param classNodesMapById
     * @param umlAssociatedNodes
     */
    public static void extractRelationships(List<NavigationProperty> navigationProperties, String schemaName,
                                            Map<String, EntityType> classNodesMapById,
                                            List<JsonNode> umlAssociatedNodes) {
        if (umlAssociatedNodes != null) {
            for (JsonNode associationNode : umlAssociatedNodes) {
                NavigationProperty navigationProperty = new NavigationProperty();
                navigationProperty.setName(StringUtils.capitalize(associationNode.path("name").asText()));
                JsonNode end1 = associationNode.path("end1");
                EntityType associatedEntity1 = classNodesMapById.get(end1.path("reference").path("$ref").asText());
                String multiplicity1 = end1.path("multiplicity").asText();
                JsonNode end2 = associationNode.path("end2");
                EntityType associatedEntity2 = classNodesMapById.get(end2.path("reference").path("$ref").asText());
                String multiplicity2 = end2.path("multiplicity").asText();
                if (cardinalityZero(multiplicity1) || cardinalityZero(multiplicity2)) {
                    continue;
                }
                if (cardinalityOne(multiplicity1) && cardinalityOne(multiplicity2)) {
                    navigationProperty.setType(schemaName + "." + associatedEntity2.getName());
                }
                if (navigationProperty.getType() == null && cardinalityMultiple(multiplicity2)) {
                    navigationProperty.setType(String.format("Collection(%s)", schemaName + "." + associatedEntity2.getName()));
                }
                if (cardinalityMultiple(multiplicity1)) {
                    NavigationProperty navigationProperty1 = new NavigationProperty();
                    navigationProperty1.setName(StringUtils.capitalize(associationNode.path("name").asText()));
                    navigationProperty1.setType(String.format("Collection(%s)", schemaName + "." + associatedEntity1.getName()));
                    associatedEntity2.setNavigationProperty(navigationProperty);
                }
                navigationProperties.add(navigationProperty);
            }
        }
    }

    public static void extractClass(JsonNode classNode, String schemaName,
                                    Map<String, String> messages,
                                    Map<String, EntityType> classNodesMapById, EntityType entityType, Map<String, ComplexType> complexTypesById, Map<String, EnumType> enumTypesById) {
        /** Extracts properties/fields of the class	**/
        JsonNode attributeNodes = classNode.path("attributes");
        JsonNode ownedElements = classNode.path("ownedElements");
        extractFields(messages, entityType, entityType.getName(), attributeNodes, complexTypesById, enumTypesById, schemaName);
        extractEntityBaseType(entityType, ownedElements, classNodesMapById, schemaName);
        /** Extract relationships **/
        JsonNode assocParentNode = classNode.path("ownedElements");
        List<JsonNode> umlAssociatedNodes = StreamSupport.stream(assocParentNode.spliterator(), true)
                .filter(ownedElement -> "UMLAssociation".equalsIgnoreCase(
                        ownedElement.path("_type").asText()))
                .collect(Collectors.toList());

        List<NavigationProperty> navigationProperties = new ArrayList<>();
        extractRelationships(navigationProperties, schemaName, classNodesMapById, umlAssociatedNodes);
        entityType.setNavigationProperties(navigationProperties);
    }

    public static void extractNavigationPropertiesOfBaseTypes(Map<String, List<NavigationProperty>> entityTypeSet, EntityType eType, String schemaName) {
        String baseType = eType.getBaseType() != null ? eType.getBaseType().replace(schemaName+".", ""): null;
        if(baseType != null)
            eType.getNavigationProperties().addAll(entityTypeSet.get(baseType));
    }

    public static void extractNavigationPropertyOfBaseEntityForInheritedEntity(String schemaName, Map<String, EntityType> entityTypesById) {
        Map<String, Queue<EntityType>> entityBaseTypeMap = new HashMap<>();
        Map<String, List<NavigationProperty>> navigationPropertyMap = entityTypesById.values().stream().
                collect(Collectors.toMap(EntityType::getName, EntityType::getNavigationProperties));
        for(EntityType entityType : entityTypesById.values()) {
            if(entityBaseTypeMap.get(entityType.getBaseType()) == null) {
                entityBaseTypeMap.put(entityType.getBaseType(), new LinkedList<>());
            }
            entityBaseTypeMap.get(entityType.getBaseType()).add(entityType);
        }
        /** Extracts class information and adds to EntityType **/
        Queue<Queue<EntityType>> entityTypeQueue = new LinkedList<>();
        entityTypeQueue.add(entityBaseTypeMap.get(null));
        while(!entityTypeQueue.isEmpty()) {
            Queue<EntityType> level = entityTypeQueue.poll();
            while(!level.isEmpty()) {
                EntityType eType = level.poll();
                if(eType.getBaseType() != null) {
                    extractNavigationPropertiesOfBaseTypes(navigationPropertyMap, eType, schemaName);
                }
                if(entityBaseTypeMap.get(schemaName + "." + eType.getName()) != null)
                    entityTypeQueue.add(entityBaseTypeMap.get(schemaName + "." + eType.getName()));
            }
        }
    }

    public static List<EntityType> createCollectionTablesForCollectionTypeFields(Map<String, EntityType> entityTypesById, Map<String, EnumType> enumTypesByName, Map<String, ComplexType> complexTypesByName, String schemaName) {
        List<EntityType> entityTypeList = new ArrayList<>();
        for(EntityType etype : entityTypesById.values()) {
            for(Property property : etype.getProperties()) {
                if(DataTypeMapping.isCollectionType(property.getType()) != null) {
                    Key key = Key.createKey(PropertyRef.createPropertyRef("Id"));
                    List<Property> properties = new ArrayList<>();
                    properties.add(Property.createProperty("Id", DataTypeMapping.INT.getEdmxDataType()));
                    properties.add(etype.getProperties().stream().
                            filter(property1 -> property1.getName().equals
                                    (etype.getKey().getPropertyRef().getName())).collect(Collectors.toList()).get(0));
                    String collectionOf = DataTypeMapping.extractCollectionType(property.getType());
                    if(DataTypeMapping.isPrimitiveType(collectionOf) != null) {
                        properties.add(Property.createProperty( StringUtils.capitalize(property.getName()), DataTypeMapping.isPrimitiveType(collectionOf)));
                        entityTypeList.add(EntityType.CreateEntity(key, properties,
                                StringUtils.capitalize(etype.getName()) + StringUtils.capitalize(property.getName())));
                        continue;
                    }
                    ComplexType complexType =complexTypesByName.get(StringUtils.lowerCase(collectionOf));
                    if(complexType != null) {
                        properties.addAll(complexType.getProperties());
                        entityTypeList.add(EntityType.CreateEntity(key, properties, StringUtils.capitalize(etype.getName()) + StringUtils.capitalize(property.getName())));
                        continue;
                    }
                    EnumType enumType = enumTypesByName.get(StringUtils.lowerCase(collectionOf));
                    if(enumType != null) {
                        properties.add(Property.createProperty(StringUtils.capitalize(enumType.getName()), schemaName+ "." + enumType.getName()));
                        entityTypeList.add(EntityType.CreateEntity(key, properties,
                                StringUtils.capitalize(etype.getName()) + StringUtils.capitalize(enumType.getName())));
                    }
                }
            }
        }
        return entityTypeList;
    }
}
