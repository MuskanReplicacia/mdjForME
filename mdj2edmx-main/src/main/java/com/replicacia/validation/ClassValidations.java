package com.replicacia.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.replicacia.inputmodels.Stereotype;
import com.replicacia.util.GenUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ClassValidations {

    public static String validateClassName(final String className, List<String> errMessage) {
        if(GenUtils.emptyString(className)) {
            errMessage.add("class name is missing(REQUIRED): for few classes name is missing, it is a mandatory field. Please provide class name, use ALPHANUMERIC characters");
            return "";
        }
        if(!GenUtils.isAlphaNumeric(className)) {
            errMessage.add(String.format("classname must only have alphanumeric(a to z, A to Z, 0 to 9) characters(REQUIRED) : %s has special characters, please remove special characters and try again.", className));
            return "";
        }
        return GenUtils.capitalize(className);
    }

    public static Stereotype validateStereotype(final String stereotype, final List<String> messages, boolean insertMessage) {
        Stereotype stereo = null;
        if(!StringUtils.isEmpty(stereotype)) {
            if(Stereotype.COMPLEXTYPE.getValue().equalsIgnoreCase(stereotype)) {
                stereo = Stereotype.COMPLEXTYPE;
            } else if(Stereotype.ENUMTYPE.getValue().equalsIgnoreCase(stereotype)) {
                stereo = Stereotype.ENUMTYPE;
            } else if(Stereotype.ENTITYTYPE.getValue().equalsIgnoreCase(stereotype)) {
                stereo = Stereotype.ENTITYTYPE;
            } else  {
                if(insertMessage)
                    messages.add(String.format("Invalid Stereotype(REQUIRED) : %s,Please provide valid values for stereotype ( ENUMTYPE for enums, COMPLEXTYPE for embeddable( umlclass without id field)., if you dont specify anything it takes entityType", stereotype));
                stereo = Stereotype.ANONYMOUS;
            }
        } else {
            stereo = Stereotype.ENTITYTYPE;
        }
        return stereo;
    }

    public static void validateUmlClassNodesForUniqueNames(final JsonNode nodeArray, final List<String> errMessages) {
        if(nodeArray != null && nodeArray.isArray() && nodeArray.size() > 0) {
            final HashMap<String, Integer> freqMap = new HashMap<>();
            for (int i = 0; i < nodeArray.size(); i++) {
                JsonNode node = nodeArray.get(i);
                if ("UMLClass".equalsIgnoreCase(node.path("_type").asText())) {
                    String propertyValue = node.path("name").asText().toLowerCase();
                    if (StringUtils.isEmpty(propertyValue))
                        continue;
                    if (!freqMap.containsKey(propertyValue)) {
                        freqMap.put(propertyValue, 0);
                    }
                    freqMap.put(propertyValue, freqMap.get(propertyValue)+1);
                }
            }

            for(String key : freqMap.keySet()) {
                final int times = freqMap.get(key);
                if(times > 1) {
                    errMessages.add(String.format("Name field must be unique for every umlclass (REQUIRED) : Found %s classes with %s name. Please check and assign unique name.", times, key));
                }
            }
            freqMap.clear();
        }
    }
}
