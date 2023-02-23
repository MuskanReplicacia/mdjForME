package com.replicacia.inputmodels.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.replicacia.inputmodels.pojos.EnumType;
import com.replicacia.inputmodels.pojos.Member;
import com.replicacia.util.ExceptionMessages;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnumTypeMapper {
    public static void extractEnumMembers(JsonNode attributeNodes, EnumType enumType, Map<String, String> messages) {
        List<Member> members = new ArrayList<>();
        boolean isKeyPresent = false;
        int ordinal = 0;
        if (attributeNodes.isArray()) {
            for (JsonNode attributeNode : attributeNodes) {
                Member member = new Member();
                String memberName = attributeNode.path("name").asText();
                member.setName(memberName);
                member.setValue(ordinal+ "");
                ordinal++;
                if (attributeNode.path("isID").asBoolean()) {
                    messages.put(memberName, ExceptionMessages.ID_FIELD_IN_ENUM);
                }
                members.add(member);
            }
        }
        enumType.setMembers(members);
    }

    public static void extractEnum(JsonNode classNode, String schemaName, Map<String, EnumType> enumTypesById, EnumType enumType, Map<String, String> messages) {
        enumType.setName( classNode.path("name").asText() );

        /** Extracts properties/fields of the class	**/
        JsonNode attributeNodes = classNode.path("attributes");
        if(attributeNodes != null) {
            extractEnumMembers(attributeNodes, enumType, messages);
        }
    }

    public static EnumType addEnumType(Map<String, EnumType> enumTypesById, String id, JsonNode classNode) {
        EnumType enumTypeFromMap = enumTypesById.get(id);
        if(enumTypeFromMap == null) {
            EnumType enumType = new EnumType();
            enumType.setName(StringUtils.capitalize(classNode.path("name").asText()));
            enumTypesById.put(id, enumType);
            return enumType;
        } else {
            return enumTypeFromMap;
        }
    }

}
