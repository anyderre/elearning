package com.sorbSoft.CabAcademie.model.linkedin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FirstName{

	@JsonProperty("localized")
	private Localized localized;

	@JsonProperty("preferredLocale")
	private PreferredLocale preferredLocale;

	public void setLocalized(Localized localized){
		this.localized = localized;
	}

	public Localized getLocalized(){
		return localized;
	}

	public void setPreferredLocale(PreferredLocale preferredLocale){
		this.preferredLocale = preferredLocale;
	}

	public PreferredLocale getPreferredLocale(){
		return preferredLocale;
	}

	@Override
 	public String toString(){
		return 
			"FirstName{" + 
			"localized = '" + localized + '\'' + 
			",preferredLocale = '" + preferredLocale + '\'' + 
			"}";
		}
}