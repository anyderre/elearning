package com.sorbSoft.CabAcademie.model.linkedin;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfilePicture{

	@JsonProperty("displayImage")
	private String displayImage;

	public void setDisplayImage(String displayImage){
		this.displayImage = displayImage;
	}

	public String getDisplayImage(){
		return displayImage;
	}

	@Override
 	public String toString(){
		return 
			"ProfilePicture{" + 
			"displayImage = '" + displayImage + '\'' + 
			"}";
		}
}