package com.sorbSoft.CabAcademie.model.linkedin.email;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Handle{

	@JsonProperty("emailAddress")
	private String emailAddress;

	public void setEmailAddress(String emailAddress){
		this.emailAddress = emailAddress;
	}

	public String getEmailAddress(){
		return emailAddress;
	}

	@Override
 	public String toString(){
		return 
			"Handle{" + 
			"emailAddress = '" + emailAddress + '\'' + 
			"}";
		}
}