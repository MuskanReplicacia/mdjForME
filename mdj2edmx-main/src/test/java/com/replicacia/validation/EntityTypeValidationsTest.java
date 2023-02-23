package com.replicacia.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.replicacia.inputmodels.mappers.ClassMappers.distributeNodesToTheirStereoTypes;

public class EntityTypeValidationsTest {
    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonNode getFileContents(final String fileName) throws IOException {
        return  mapper.readTree(new File(fileName));
    }
    @Test
    public void testBaseTypeValidUmlGeneralization_Validation() throws IOException {
        JsonNode parentNode = getFileContents("src/test/resources/mdj_basetype_valid.mdj");
        JsonNode childrenNodes = parentNode.path("ownedElements")
                .path(0)
                .path("ownedElements");
        Map<String, JsonNode> entityNodeById = new HashMap<>();
        Map<String, JsonNode> complexNodeById = new HashMap<>();
        Map<String, JsonNode> enumNodeById = new HashMap<>();
        Map<String, JsonNode> anonymousNodeById = new HashMap<>();
        List<String> errMessages = new ArrayList<>();
        distributeNodesToTheirStereoTypes(childrenNodes, errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        for (int i = 0; i< childrenNodes.size(); i++) {
            EntityTypeValidations.validateBaseType(childrenNodes.get(i), errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        }
        Assert.assertTrue(errMessages.isEmpty());
    }

    @Test
    public void testBaseTypeValidUmlDependency_Validation() throws IOException {
        JsonNode parentNode = getFileContents("src/test/resources/mdj_basetypeUmlDependency_valid.mdj");
        JsonNode childrenNodes = parentNode.path("ownedElements")
                .path(0)
                .path("ownedElements");
        Map<String, JsonNode> entityNodeById = new HashMap<>();
        Map<String, JsonNode> complexNodeById = new HashMap<>();
        Map<String, JsonNode> enumNodeById = new HashMap<>();
        Map<String, JsonNode> anonymousNodeById = new HashMap<>();
        List<String> errMessages = new ArrayList<>();
        distributeNodesToTheirStereoTypes(childrenNodes, errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        for (int i = 0; i< childrenNodes.size(); i++) {
            EntityTypeValidations.validateBaseType(childrenNodes.get(i), errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        }
        Assert.assertTrue(errMessages.isEmpty());
    }

    @Test
    public void testBaseTypeSourceEntityType_Validation() throws IOException {
        JsonNode parentNode = getFileContents("src/test/resources/mdj_basetypeSourceStereotypeEntityType_valid.mdj");
        JsonNode childrenNodes = parentNode.path("ownedElements")
                .path(0)
                .path("ownedElements");
        Map<String, JsonNode> entityNodeById = new HashMap<>();
        Map<String, JsonNode> complexNodeById = new HashMap<>();
        Map<String, JsonNode> enumNodeById = new HashMap<>();
        Map<String, JsonNode> anonymousNodeById = new HashMap<>();
        List<String> errMessages = new ArrayList<>();
        distributeNodesToTheirStereoTypes(childrenNodes, errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        for (int i = 0; i< childrenNodes.size(); i++) {
            EntityTypeValidations.validateBaseType(childrenNodes.get(i), errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        }
        Assert.assertTrue(errMessages.isEmpty());
    }

    @Test
    public void testBaseTypeSourceTargetSame_Validation() throws IOException {
        JsonNode parentNode = getFileContents("src/test/resources/mdj_basetypeSourceTargetSame_invalid.mdj");
        JsonNode childrenNodes = parentNode.path("ownedElements")
                .path(0)
                .path("ownedElements");
        Map<String, JsonNode> entityNodeById = new HashMap<>();
        Map<String, JsonNode> complexNodeById = new HashMap<>();
        Map<String, JsonNode> enumNodeById = new HashMap<>();
        Map<String, JsonNode> anonymousNodeById = new HashMap<>();
        List<String> errMessages = new ArrayList<>();
        distributeNodesToTheirStereoTypes(childrenNodes, errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        for (int i = 0; i< childrenNodes.size(); i++) {
            EntityTypeValidations.validateBaseType(childrenNodes.get(i), errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        }
        Assert.assertEquals("Source umlClass and Target umlClass cannot be same." +
                " Source is X, Target is also X.", errMessages.get(0));
    }

    @Test
    public void testBaseTypeSourceStereoTypeEnumTargetStereoComplex_Validation() throws IOException {
        JsonNode parentNode = getFileContents("src/test/resources/mdj_basetypeSourceStereoEnumTargetStereoComplex_invalid.mdj");
        JsonNode childrenNodes = parentNode.path("ownedElements")
                .path(0)
                .path("ownedElements");
        Map<String, JsonNode> entityNodeById = new HashMap<>();
        Map<String, JsonNode> complexNodeById = new HashMap<>();
        Map<String, JsonNode> enumNodeById = new HashMap<>();
        Map<String, JsonNode> anonymousNodeById = new HashMap<>();
        List<String> errMessages = new ArrayList<>();
        distributeNodesToTheirStereoTypes(childrenNodes, errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        for (int i = 0; i< childrenNodes.size(); i++) {
            EntityTypeValidations.validateBaseType(childrenNodes.get(i), errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        }
        Assert.assertEquals("Please check the stereotype(REQUIRED):" +
                " If you want to use inheritance, the source and target stereotype must be either empty" +
                " or entitytype. Here the source X stereotype EnumType is not entitytype nor empty.", errMessages.get(0));
        Assert.assertEquals("Please check the stereotype(REQUIRED):" +
                " If you want to use inheritance, the source and target stereotype must be either empty" +
                " or entitytype. Here the target Y stereotype ComplexType is not entitytype nor empty.", errMessages.get(1));
    }

    @Test
    public void testBaseTypeSourceStereoTypeRandomTargetStereoComplex_Validation() throws IOException {
        JsonNode parentNode = getFileContents("src/test/resources/mdj_basetypeSourceStereoRandomStringTargetStereoComplex_invalid.mdj");
        JsonNode childrenNodes = parentNode.path("ownedElements")
                .path(0)
                .path("ownedElements");
        Map<String, JsonNode> entityNodeById = new HashMap<>();
        Map<String, JsonNode> complexNodeById = new HashMap<>();
        Map<String, JsonNode> enumNodeById = new HashMap<>();
        Map<String, JsonNode> anonymousNodeById = new HashMap<>();
        List<String> errMessages = new ArrayList<>();
        distributeNodesToTheirStereoTypes(childrenNodes, errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        for (int i = 0; i< childrenNodes.size(); i++) {
            EntityTypeValidations.validateBaseType(childrenNodes.get(i), errMessages, entityNodeById, enumNodeById, complexNodeById, anonymousNodeById);
        }
        Assert.assertEquals("Please check the stereotype(REQUIRED):" +
                " If you want to use inheritance, the source and target stereotype must be either empty" +
                " or entitytype. Here the source X stereotype random is not entitytype nor empty.", errMessages.get(1));
        Assert.assertEquals("Please check the stereotype(REQUIRED):" +
                " If you want to use inheritance, the source and target stereotype must be either empty" +
                " or entitytype. Here the target Y stereotype ComplexType is not entitytype nor empty.", errMessages.get(2));
    }

}