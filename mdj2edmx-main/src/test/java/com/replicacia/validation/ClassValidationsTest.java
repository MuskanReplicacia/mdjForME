package com.replicacia.validation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.replicacia.inputmodels.Stereotype;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClassValidationsTest {

    private JsonNode getFileContents(final String fileName) throws IOException {
        return  mapper.readTree(new File(fileName));
    }

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void validateEntityTypeName_nullCase() {
        String className = null;
        List<String> messages = new ArrayList<>();
        String entityName = ClassValidations.validateClassName(className, messages);
        Assert.assertEquals(entityName, "");
        Assert.assertEquals(messages.get(0), "class name is missing(REQUIRED): for few classes name is missing," +
                " it is a mandatory field. Please provide class name, use ALPHANUMERIC characters");
    }

    @Test
    public void validateEntityTypeName_emptyCase() {
        String className = "";
        List<String> messages = new ArrayList<>();
        String entityName = ClassValidations.validateClassName(className, messages);
        Assert.assertEquals(entityName, "");
        Assert.assertEquals(messages.get(0), "class name is missing(REQUIRED): for few classes name is missing," +
                " it is a mandatory field. Please provide class name, use ALPHANUMERIC characters");
    }

    @Test
    public void validateEntityTypeName_specialCharactersCase() {
        String className = "abc@";
        List<String> messages = new ArrayList<>();
        String entityName = ClassValidations.validateClassName(className, messages);
        Assert.assertEquals(entityName, "");
        Assert.assertEquals(messages.get(0), "classname must only have alphanumeric(a to z, A to Z, 0 to 9) characters(REQUIRED) : abc@ has special characters, please remove special characters and try again.");
    }

    @Test
    public void validateEntityTypeName_validCase() {
        String className = "abc";
        List<String> messages = new ArrayList<>();
        String entityName = ClassValidations.validateClassName(className, messages);
        Assert.assertEquals(entityName, "Abc");
        Assert.assertEquals(messages.size(),0);
    }

    @Test
    public void validateStereotype_nullCase() {
        String stereotype = null;
        List<String> messages = new ArrayList<>();
        Stereotype stereo = ClassValidations.validateStereotype(stereotype, messages, true);
        Assert.assertEquals(stereo, Stereotype.ENTITYTYPE);
        Assert.assertEquals(0, messages.size());
    }

    @Test
    public void validateStereotype_emptyCase() {
        String stereotype = "";
        List<String> messages = new ArrayList<>();
        Stereotype stereo = ClassValidations.validateStereotype(stereotype, messages, true);
        Assert.assertEquals(stereo, Stereotype.ENTITYTYPE);
        Assert.assertEquals(0, messages.size());
    }

    @Test
    public void validateStereotype_InvalidCase() {
        String stereotype = "functiontype";
        List<String> messages = new ArrayList<>();
        Stereotype stereo = ClassValidations.validateStereotype(stereotype, messages, true);
        Assert.assertEquals(stereo, Stereotype.ANONYMOUS);
        Assert.assertEquals(1, messages.size());
        Assert.assertEquals("Invalid Stereotype(REQUIRED) : functiontype,Please provide valid values for stereotype ( ENUMTYPE for enums, COMPLEXTYPE for embeddable( umlclass without id field)., if you dont specify anything it takes entityType", messages.get(0));
    }

    @Test
    public void validateStereotype_enumCase() {
        String stereotype = "EnUmTyPe";
        List<String> messages = new ArrayList<>();
        Stereotype stereo = ClassValidations.validateStereotype(stereotype, messages, true);
        Assert.assertEquals(stereo, Stereotype.ENUMTYPE);
        Assert.assertEquals(0, messages.size());
    }

    @Test
    public void validateStereotype_complexCase() {
        String stereotype = "CoMpLexTYPE";
        List<String> messages = new ArrayList<>();
        Stereotype stereo = ClassValidations.validateStereotype(stereotype, messages, true);
        Assert.assertEquals(stereo, Stereotype.COMPLEXTYPE);
        Assert.assertEquals(0, messages.size());
    }

    @Test
    public void validateUmlClassNodesForUniqueNames_validCase() throws IOException {
        JsonNode rootNode = getFileContents("src/test/resources/mdj_classNodeUniqueName_valid.mdj");
        List<String> errMessages = new ArrayList<>();
        JsonNode childrenNodes = rootNode.path("ownedElements")
                .path(0)
                .path("ownedElements");
        ClassValidations.validateUmlClassNodesForUniqueNames(childrenNodes, errMessages);
        assertTrue(errMessages.isEmpty());
    }

    @Test
    public void validateUmlClassNodesForUniqueNames_EmptyStringCase() throws IOException {
        JsonNode rootNode = getFileContents("src/test/resources/mdj_classNodeEmptyString_valid.mdj");
        List<String> errMessages = new ArrayList<>();
        JsonNode childrenNodes = rootNode.path("ownedElements")
                .path(0)
                .path("ownedElements");
        ClassValidations.validateUmlClassNodesForUniqueNames(childrenNodes, errMessages);
        assertTrue(errMessages.isEmpty());
    }

    @Test
    public void validateUmlClassNodesForUniqueNames_DuplicateCase() throws IOException {
        JsonNode rootNode = getFileContents("src/test/resources/mdj_classNodeDuplicateName_invalid.mdj");
        List<String> errMessages = new ArrayList<>();
        JsonNode childrenNodes = rootNode.path("ownedElements")
                .path(0)
                .path("ownedElements");
        ClassValidations.validateUmlClassNodesForUniqueNames(childrenNodes, errMessages);
        assertEquals("Name field must be unique for every umlclass (REQUIRED) : Found 2 classes with x name. Please check and assign unique name.", errMessages.get(0));
    }
}