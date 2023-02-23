package com.replicacia.inputmodels;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MDJ2EDMSTransformer {

	static final Logger log = LogManager.getLogger(MDJ2EDMSTransformer.class.getName());
	
	public static void main(String[] args) {
		try {
			// Read command line arguments - only argument is filename
			String fileName = "Person.mdj";
			String currentFolderPath = new File(".").getCanonicalPath();
			File file = new File(currentFolderPath, "/mdj/"+ fileName );
			
			log.info( "Absolute path fo input json file: " + file.getAbsolutePath() );
			log.info( "Absolute path fo input json file: " + currentFolderPath );
			
			// 2. Generate input model specific code
			MDJToEDMXTransformer reader = new MDJToEDMXTransformer();
	        Map<String, String> messages = reader.execute( file.getAbsolutePath() );
	        for (String keyName : messages.keySet()) {
	        	log.info(  keyName + " : " + messages.get(keyName) );
			}
			
			
		} catch (IOException e1) {
			log.error( e1.getMessage() );
		}

	}
	
}
