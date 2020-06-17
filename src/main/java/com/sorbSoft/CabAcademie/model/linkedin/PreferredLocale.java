package com.sorbSoft.CabAcademie.model.linkedin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PreferredLocale{

	@JsonProperty("country")
	private String country;

	@JsonProperty("language")
	private String language;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setLanguage(String language){
		this.language = language;
	}

	public String getLanguage(){
		return language;
	}

	@Override
 	public String toString(){
		return 
			"PreferredLocale{" + 
			"country = '" + country + '\'' + 
			",language = '" + language + '\'' + 
			"}";
		}
}