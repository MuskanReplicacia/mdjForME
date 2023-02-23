package com.replicacia.inputmodels;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.replicacia.inputmodels.exception.CustomException;
import com.replicacia.inputmodels.pojos.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;


public class EDMXConfigurationWriter  {
    public EDMXConfigurationWriter() {
        super();
    }
    
    public void write(Edmx edmx, String fileName) throws JAXBException, FileNotFoundException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Edmx.class);

        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

	    // it is assumed that file ext is ".mdj" for this IMPL
        fileName = fileName.substring( 0, fileName.lastIndexOf(".")) + ".xml";
        
        marshaller.marshal(edmx, new FileOutputStream(fileName));
    }
    
    private String getFileExtention(String fileName) {
		String fileExt = fileName.substring( fileName.lastIndexOf(".")+1, fileName.length() );
		return fileExt;
	}
}


