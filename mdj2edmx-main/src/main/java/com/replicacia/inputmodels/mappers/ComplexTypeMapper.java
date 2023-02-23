package com.replicacia.inputmodels.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.replicacia.inputmodels.DataTypeMapping;
import com.replicacia.inputmodels.pojos.ComplexType;
import com.replicacia.inputmodels.pojos.EnumType;
import com.replicacia.inputmodels.pojos.Property;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ComplexTypeMapper {
    /**
     * @param complexTypesById
     * @param id
     * @param classNode
     * @return returns existing complex type with this id if exists, otherwise creates one/.
     */
    public static ComplexType addComplexType(Map<String, ComplexType> complexTypesById, String id, JsonNode classNode) {
        ComplexType complexTypeFromMap = complexTypesById.get(id);
        if(complexTypeFromMap == null) {
            ComplexType complexType = new ComplexType();
            complexType.setName(StringUtils.capitalize(classNode.path("name").asText()));
            complexTypesById.put( id, complexType);
            return complexType;
        } else {
            return complexTypeFromMap;
        }
    }


    public static void extractComplexType(JsonNode classNode, String schemaName, Map<String, ComplexType> complexTypesById, Map<String, EnumType> enumTypesById, ComplexType complexType) {
        JsonNode attributeNodes = classNode.path("attributes");
        JsonNode ownedElements = classNode.path("ownedElements");
        if(attributeNodes != null) {
            extractComplexFields(attributeNodes, complexType, complexTypesById, enumTypesById, schemaName);
            extractComplexBaseType(ownedElements, complexType, complexTypesById, schemaName);
        }
    }

    public static void extractComplexBaseType(JsonNode ownedElements, ComplexType complexType, Map<String, ComplexType> complexTypesById, String schemaName) {
        if(ownedElements != null && ownedElements.isArray() && ownedElements.size() > 0) {
            for(JsonNode element : ownedElements) {
                if(element.path("_type").asText().equals("UMLGeneralization")) {
                    JsonNode target = element.path("target");
                    String baseTypeId = target.path("$ref").asText();
                    ComplexType baseTypeComplex = complexTypesById.get(baseTypeId);
                    complexType.setBaseType(schemaName + "." + baseTypeComplex.getName());
                }
            }
        }
    }

    public static void extractComplexFields(JsonNode attributeNodes, ComplexType complexType, Map<String, ComplexType> complexTypesById, Map<String, EnumType> enumTypesById, String schemaName) {
        List<Property> properties = new ArrayList<>();
        if(attributeNodes.isArray()) {
            for (JsonNode attributeNode : attributeNodes) {
                Property property = new Property();
                property.setName(StringUtils.capitalize(attributeNode.path("name").asText()));
                String type = null;
                String finalUserType = null;
                if(attributeNode.path("type").asText() != null
                        && attributeNode.path("type").asText().equals("")) {
                    JsonNode typeNode = attributeNode.path("type");
                    if(typeNode != null && typeNode.path("$ref").asText() != null
                            && !typeNode.path("$ref").asText().equals("")) {
                        if(complexTypesById.get(typeNode.path("$ref").asText()) != null) {
                            finalUserType = complexTypesById.get(typeNode.path("$ref").asText()).getName();
                        } else if(enumTypesById.get(typeNode.path("$ref").asText()) != null) {
                            finalUserType = enumTypesById.get(typeNode.path("$ref").asText()).getName();
                        }
                    }
                }
                String userType = finalUserType != null ? finalUserType : attributeNode.path("type").asText();
                String edmxDataType = Arrays.stream(DataTypeMapping.values()).filter(dtm -> userType.toUpperCase().equalsIgnoreCase(dtm.name())).map(DataTypeMapping::getEdmxDataType).findFirst().orElse(null);
                if(edmxDataType == null) {
                    for(String dtype : DataTypeMapping.collectionTypesSupported.keySet()) {
                        if(dtype.equalsIgnoreCase(userType)) {
                            edmxDataType = DataTypeMapping.collectionTypesSupported.get(dtype);
                            break;
                        }
                    }
                }
                if(edmxDataType == null) {
                    for (ComplexType ctype : complexTypesById.values()) {
                        if (ctype.getName().toUpperCase().equals(userType.toUpperCase())) {
                            edmxDataType = schemaName + "." + ctype.getName();
                            break;
                        }
                    }
                }
                if(edmxDataType == null) {
                    for (EnumType enumType : enumTypesById.values()) {
                        if (enumType.getName().toUpperCase().equals(userType.toUpperCase())) {
                            edmxDataType = schemaName + "." + enumType.getName();
                            break;
                        }
                    }
                }
                if(edmxDataType == null) {
                    String collectionType = DataTypeMapping.isCollectionType(userType);
                    if(collectionType != null) {
                        String userTypeStripped = userType.toLowerCase().replaceAll("[^a-z0-9A-Z]", "").replace(collectionType.toLowerCase(), "");
                        for (EnumType enumType : enumTypesById.values()) {
                            if (enumType.getName().equalsIgnoreCase(userTypeStripped)) {
                                edmxDataType = String.format("Collection(%s)", schemaName + "." + enumType.getName());
                                break;
                            }
                        }
                    }
                }
                if(edmxDataType != null && !edmxDataType.isEmpty()) {
                    type = edmxDataType;
                }
                property.setType(type);
                properties.add(property);
            }
        }
        complexType.setProperties(properties);
    }
}
