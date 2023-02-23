package com.replicacia.inputmodels.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.replicacia.inputmodels.Stereotype;
import com.replicacia.inputmodels.pojos.ComplexType;
import com.replicacia.inputmodels.pojos.EntityType;
import com.replicacia.inputmodels.pojos.EnumType;
import com.replicacia.validation.ClassValidations;

import java.util.List;
import java.util.Map;

public class ClassMappers {
    public static void distributeNodesToXmlEntityTheirStereoTypes(JsonNode childrenNodes, Map<String, EntityType> entityTypesById, Map<String, ComplexType> complexTypesById, Map<String, EnumType> enumTypesById) {
        for (int i = 0; i < childrenNodes.size(); i++) {
            JsonNode classNode = childrenNodes.get(i);
            if ("UMLClass".equalsIgnoreCase(classNode.path("_type").asText())) {
                String id = classNode.path("_id").asText();
                String stereoType =  classNode.path("stereotype").asText();
                if(Stereotype.COMPLEXTYPE.getValue().equalsIgnoreCase(stereoType)) {
                    ComplexTypeMapper.addComplexType(complexTypesById, id, classNode);
                } else if(Stereotype.ENUMTYPE.getValue().equalsIgnoreCase(stereoType)) {
                    EnumTypeMapper.addEnumType(enumTypesById, id, classNode);
                } else {
                    EntityTypeMapper.addEntityType(entityTypesById, id, classNode);
                }
            }
        }
    }

    public static void distributeNodesToTheirStereoTypes(JsonNode childrenNodes,
                                                         List<String> errMessage,
                                                         Map<String, JsonNode> entityNodesById,
                                                         Map<String, JsonNode> enumNodesById,
                                                         Map<String, JsonNode> complexNodesById,
                                                         Map<String, JsonNode> anonymousNodesById) {
        for (int i = 0; i < childrenNodes.size(); i++) {
            JsonNode classNode = childrenNodes.get(i);
            if ("UMLClass".equalsIgnoreCase(classNode.path("_type").asText())) {
                String id = classNode.path("_id").asText();
                String stereoType =  classNode.path("stereotype").asText();
                switch(ClassValidations.validateStereotype(stereoType, errMessage, true)) {
                    case COMPLEXTYPE:
                        complexNodesById.put(id, classNode);
                        break;
                    case ENUMTYPE:
                        enumNodesById.put(id, classNode);
                        break;
                    case ENTITYTYPE:
                        entityNodesById.put(id, classNode);
                        break;
                    case ANONYMOUS:
                        anonymousNodesById.put(id, classNode);
                          break;
                }
            }
        }
    }
}
