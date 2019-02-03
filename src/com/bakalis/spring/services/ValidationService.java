package com.bakalis.spring.services;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

	public boolean isNumeric(String s){
		/*
		try{
			Integer.parseInt(s);
		}catch(NumberFormatException ex){
			return false;
		}
		return true;
		*/
		return s.matches("-?\\d+(\\.\\d+)?");
	}
	
	public boolean isEmptyString(String s){
		if(s.length()>0)
			return true;
		else
			return false;
	}
}
