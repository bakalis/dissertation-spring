package com.bakalis.spring.services;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Service
public class ErrorLoggingService {

	protected String error = null;
	

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	
	public boolean errorExists(){
		if(error==null)
			return false;
		else
			return true;
	}
	
	public void reset(){
		this.setError(null);
	}
	//Given a model Object, it passes the Error
	//to it if there is one
	public void passErrorMessageToModel(Model model){  
		if(this.errorExists()){
			model.addAttribute("error", this.getError());
			this.reset();
		}
	}
	//Given a RedirectAttributes Object, it passes the Error
	//to it if there is one. Used to pass the error during Redirections
	public void passErrorMessageToRedirectAttributes(RedirectAttributes redirectAttributes){   
		
		if(this.errorExists()){
			redirectAttributes.addFlashAttribute("error", this.getError());
			this.reset();
		}
	}
	
	
	
}
