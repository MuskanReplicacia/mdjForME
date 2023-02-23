package com.replicacia.util;

public class Practice {

	public static void main(String[] args) {
		String fileName = "metadata.mdj";
		System.out.println("Input mdj file name: "+ fileName);

		fileName = fileName.substring( 0, fileName.lastIndexOf(".")) + ".xml";
		System.out.println("Output file name after changing ext: "+ fileName );
	}

}
