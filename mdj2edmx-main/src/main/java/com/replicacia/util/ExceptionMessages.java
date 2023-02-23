package com.replicacia.util;

public class ExceptionMessages {
	public static final String EMPTY_STRING = "value is empty, please pass valid value";

	private ExceptionMessages() {}

	public static final String MISSING_KEY = "Key field is mandatory for a class";
	public static final String INVALID_FILE = "Invalid file. The input file does not seem to be of expected type";
	public static final String DEFAULT_SCHEMA_NAME = "mycompany.myproject";
	public static final String MISSING_SCHEMA_NAME_OPTIONAL = "Missing Schema name. Default schema name used:" + DEFAULT_SCHEMA_NAME;
	public static final String EDMX_VERSION = "4.0";
	public static final String FIELD_TYPES = "FIELD_TYPE";
	public static final String FIELD_TYPE_VALUE_MISSING = "One of the Field Data Type Assignment is missing";
	public static final String MISSING_PROPERTY_TYPE = "Type is not defined for Property: ";
	public static final String NOT_IMPLEMENTED = "This feature is not yet implemented. Consult support team for more details.";
	
	public static final String ID_FIELD_IN_ENUM = "Id field cannot be present in enum type";

	public static final String INVALID_ALPHA_NUMERIC= "Name contains special characters other than alphanumeric characters. AlphaNumeric characters are a to z, A to Z, 0 to 9, Please correct it and Try Again";
}
