package com.replicacia.inputmodels;

import java.util.Map;

public interface ConfigurationReader {
    //read the configuration information from the source
    Map<String, String> execute(String filename);
}

// enumType, complexType, entityType, associations, navigations, property, members, types
// enumType, complexType, entityType, property, member, type -> capitalize first letter, truncate spaces
     //error conditions -> null or empty strings, special characters(should only contain alphanumeric characters)