package com.replicacia.validation;

import com.replicacia.util.GenUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SchemaValidationsTest{

    @Test
    public void testValidateSchemaName_nullCase() {
        String modelName = null;
        List<String> errMessages = new ArrayList<>();
        String schemaName = SchemaValidations.validateSchemaName(modelName, errMessages);
        assertEquals(schemaName, SchemaValidations.DEFAULT_SCHEMA_NAME);
        assertEquals(errMessages.get(0), "ModelName is missing(Optional): Using default name \"myproject\"" );
    }

    @Test
    public void testValidateSchemaName_emptyCase() {
        String modelName = "";
        List<String> errMessages = new ArrayList<>();;
        String schemaName = SchemaValidations.validateSchemaName(modelName, errMessages);
        assertEquals(schemaName, SchemaValidations.DEFAULT_SCHEMA_NAME);
        assertEquals(errMessages.get(0), "ModelName is missing(Optional): Using default name \"myproject\"");
    }

    @Test
    public void testValidateSchemaName_specialCharacters() {
        String modelName = "abc def";
        List<String> errMessages = new ArrayList<>();
        String schemaName = SchemaValidations.validateSchemaName(modelName, errMessages);
        assertEquals(schemaName, SchemaValidations.DEFAULT_SCHEMA_NAME);
        assertEquals(errMessages.get(0),
                "ModelName abc def has special characters : We support alphanumeric characters( a to z, A to Z, 0 to 9) , please remove special characters, for unblocking you we will use default name \"myproject\"");
    }

    @Test
    public void testValidateSchemaName_validCase(){
        String modelName = "abc";
        List<String> errMessages = new ArrayList<>();
        String schemaName = SchemaValidations.validateSchemaName(modelName, errMessages);
        assertEquals(schemaName, GenUtils.capitalize(modelName));
        assertTrue(errMessages.isEmpty());
    }


}