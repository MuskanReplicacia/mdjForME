package com.replicacia.inputmodels;

import com.replicacia.inputmodels.pojos.Property;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum DataTypeMapping {
	/*
	// https://olingo.apache.org/javadoc/odata4/org/apache/olingo/commons/api/edm/EdmPrimitiveType.html
	
	EdmPrimitiveType is a primitive type as defined in the Entity Data Model (EDM).
	There are methods to convert EDM primitive types from and to Java objects, respectively. The following Java types are supported:
	EDM primitive type	Java types
	Binary	byte[], Byte[]
	Boolean	Boolean
	Byte	Short, Byte, Integer, Long, BigInteger
	Date	Calendar, Date, Timestamp, Time, Long
	DateTimeOffset	Timestamp, Calendar, Date, Time, Long
	Decimal	BigDecimal, BigInteger, Double, Float, Byte, Short, Integer, Long
	Double	Double, Float, BigDecimal, Byte, Short, Integer, Long
	Duration	BigDecimal, BigInteger, Double, Float, Byte, Short, Integer, Long
	Guid	UUID
	Int16	Short, Byte, Integer, Long, BigInteger
	Int32	Integer, Byte, Short, Long, BigInteger
	Int64	Long, Byte, Short, Integer, BigInteger
	SByte	Byte, Short, Integer, Long, BigInteger
	Single	Float, Double, BigDecimal, Byte, Short, Integer, Long
	String	String
	TimeOfDay	Calendar, Date, Timestamp, Time, Long
	*/
	
	//    Edm.Decimal=double
//#Below were not used until now or not tested - this is not used until now; meaning that no edmx file used until now was not given with this data type
//    Edm.Boolean=boolean
//    Edm.DateTimeOffset=Date
//    Edm.Duration=long
//    Edm.Binary=byte[]
//    Edm.Single=short
//    Edm.SByte=short

    SHORT("Edm.Int16"),
    INT("Edm.Int32"),
    LONG("Edm.Int64"),
    DOUBLE("Edm.Decimal"),
    BIGDECIMAL("Edm.Decimal"),
    DECIMAL("Edm.Decimal"),
    FLOAT("Edm.Single"),
    SINGLE("Edm.Single"),
    STRING("Edm.String"),
    DATE("Edm.Date"),
    TIME("Edm.TimeOfDay"),
    TimeOfDay("Edm.TimeOfDay"),
    DateTimeOffset("Edm.DateTimeOffset"),
    DURATION("Edm.Duration"),
    BINARY("Edm.Binary"),
    BOOLEAN("Edm.Boolean"),
    INTEGER("Edm.Int32"),
    BIGINT("Edm.Int64"),
    INT32("Edm.Int32"),
    INT16("Edm.Int16"),
    INT64("Edm.Int64");

    public static final Map<String, String> collectionTypesSupported  = new HashMap<>();
    public static String isCollectionType(String type) {

        List<String> typesSupported = Arrays.asList("List", "Set", "Collection");
        for(String typeSup : typesSupported)
                if(type!= null && !type.isEmpty() && type.toLowerCase().contains(typeSup.toLowerCase()))
                        return typeSup;
        return null;
    }
    static {
        for(DataTypeMapping typeMapping:
            DataTypeMapping.values()) {
            collectionTypesSupported.put("List<"  + typeMapping.name()+ ">", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("List("  + typeMapping.name()+ ")", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("List["  + typeMapping.name()+ "]", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("List{"  + typeMapping.name()+ "}", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("Collection<"  + typeMapping.name()+ ">", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("Collection("  + typeMapping.name()+ ")", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("Collection["  + typeMapping.name()+ "]", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("Collection{"  + typeMapping.name()+ "}", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("Set<"  + typeMapping.name()+ ">", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("Set("  + typeMapping.name()+ ")", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("Set["  + typeMapping.name()+ "]", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
            collectionTypesSupported.put("Set{"  + typeMapping.name()+ "}", String.format("Collection(%s)", typeMapping.getEdmxDataType()));
        }
    }


	//Need datatypes to know
//    BigDecimal
//    Icon
//    URL

    private String edmxDataType;

    public static String isPrimitiveType(String edmxDataType) {
        for(DataTypeMapping dataTypeMapping : DataTypeMapping.values()){
            if(edmxDataType.toLowerCase().equals(dataTypeMapping.getEdmxDataType().replace(".", "").toLowerCase()))
                return dataTypeMapping.getEdmxDataType();
        }
        return null;
    }

    public static String extractCollectionType(String type) {
        String collectionType = isCollectionType(type);
        return type.toLowerCase().replaceAll("[^a-z0-9A-Z]", "").replace(collectionType.toLowerCase(), "");
    }


    public String getEdmxDataType() {
        return edmxDataType;
    }

    DataTypeMapping(String edmxDataType) {
        this.edmxDataType = edmxDataType;
    }
}



