package com.replicacia.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.replicacia.inputmodels.Stereotype;

import java.util.List;
import java.util.Map;

public class EntityTypeValidations {
    public static String validateEntityTypeName(final String entityName, final List<String> messages) {
        return ClassValidations.validateClassName(entityName, messages);
    }

    public static void validateBaseType(final JsonNode umlClass, final List<String> errMessages,
                                        Map<String, JsonNode> entityNodeByIdMap,
                                        Map<String, JsonNode> enumNodeByIdMap,
                                        Map<String, JsonNode> complexNodeByIdMap, Map<String, JsonNode> anonymousNodeById) {
        JsonNode ownedElements = umlClass.path("ownedElements");
        if (ownedElements != null && ownedElements.isArray() && ownedElements.size() > 0) {
            for (JsonNode element : ownedElements) {
                if (element.path("_type").asText().equals("UMLGeneralization") ||
                        element.path("_type").asText().equals("UMLDependency")) {
                    String sourceStereoType = umlClass.path("stereotype").asText();
                    if ( ClassValidations.validateStereotype(sourceStereoType, errMessages, false) != Stereotype.ENTITYTYPE) {
                        errMessages.add(String.format("Please check the stereotype(REQUIRED):" +
                                " If you want to use inheritance, the source and target stereotype must be either empty" +
                                " or entitytype. Here the source %s stereotype %s is not entitytype nor empty.", umlClass.path("name").asText(), sourceStereoType));
                    }
                    JsonNode target = element.path("target");
                    String targetId = target.path("$ref").asText();
                    JsonNode targetNode = entityNodeByIdMap.get(targetId);
                    if (targetNode == null) {
                        targetNode = enumNodeByIdMap.get(targetId);
                        targetNode  = targetNode != null ? targetNode: complexNodeByIdMap.get(targetId);
                        targetNode = targetNode!= null ? targetNode: anonymousNodeById.get(targetId);
                        errMessages.add(String.format("Please check the stereotype(REQUIRED):" +
                                " If you want to use inheritance, the source and target stereotype must be either empty" +
                                " or entitytype. Here the target %s stereotype %s is not entitytype nor empty.", targetNode.path("name").asText(),
                                targetNode.path("stereotype").asText()));
                    }

                    if (targetId.equals(umlClass.path("_id").asText())) {
                        errMessages.add(String.format("Source umlClass and Target umlClass cannot be same." +
                                " Source is %s, Target is also %s.", targetNode.path("name").asText(),
                                targetNode.path("name").asText()));
                    }

                }
            }
        }
    }
}
