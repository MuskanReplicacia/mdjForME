package com.replicacia.inputmodels;

import java.io.File;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
    	String filename = "Trippin_new.mdj";
    	// another input for relationship fields conflicting with field names
    	MDJToEDMXTransformer transformer = new MDJToEDMXTransformer();
        Map<String, String> messages = transformer.execute("mdj" + File.separatorChar + filename);
        
        System.out.println("Conversion is complete. You will see errors, if there are any");
        
        for (String keyName : messages.keySet()) {
			System.out.println( keyName + " : " + messages.get(keyName) );
		}
    }
}
