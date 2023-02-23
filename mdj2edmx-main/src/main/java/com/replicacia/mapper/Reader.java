package com.replicacia.mapper;

import java.util.List;

public interface Reader {

	/** Every mapper will have different input for readClasses(). So, it is not possible to define a generic method **/
	List readClasses();
	
}
