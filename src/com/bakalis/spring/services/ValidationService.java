package com.bakalis.spring.services;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {
	
	
	public boolean isNumeric(String s){
		return s.matches("-?\\d+(\\.\\d+)?");
	}
	//Checks if a given String is an empty String or has only Whitespaces
	public boolean isEmptyString(String s){
		if(s.trim().length()>0)
			return false;
		else
			return true;
	}
}
