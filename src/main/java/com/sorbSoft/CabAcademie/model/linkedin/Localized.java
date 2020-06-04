package com.sorbSoft.CabAcademie.model.linkedin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Localized{

	@JsonProperty("en_US")
	private String enUS;

	public void setEnUS(String enUS){
		this.enUS = enUS;
	}

	public String getEnUS(){
		return enUS;
	}

	@Override
 	public String toString(){
		return 
			"Localized{" + 
			"en_US = '" + enUS + '\'' + 
			"}";
		}
}