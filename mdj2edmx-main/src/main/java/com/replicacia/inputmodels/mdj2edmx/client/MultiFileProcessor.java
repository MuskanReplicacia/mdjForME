package com.replicacia.inputmodels.mdj2edmx.client;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import com.replicacia.inputmodels.MDJToEDMXTransformer;

public class MultiFileProcessor {

	public static void main(String[] args) {
		
		//String[] fileNames = Paths.get("mdj").toString()
		System.err.println(" WILL IMPLEMENT MULTI FILE PROCESSING AFTER FIXING NAV PROP ISSUES ");
		String filename = "logistics.mdj";
		
		Map<String, String> messages = processSingleFile(filename);
        
        System.out.println("Conversion is complete. You will see errors, if there are any");
        
        for (String keyName : messages.keySet()) {
			System.out.println( keyName + " : " + messages.get(keyName) );
		}

	}
	
	private static Map<String, String> processSingleFile( String filename){
		// another input for relationship fields conflicting with field names
    	MDJToEDMXTransformer reader = new MDJToEDMXTransformer();
        Map<String, String> messages = reader.execute("mdj" + File.separatorChar + filename);
        
        return messages;
	}

}
