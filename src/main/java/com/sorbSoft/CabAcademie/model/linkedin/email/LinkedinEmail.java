package com.sorbSoft.CabAcademie.model.linkedin.email;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LinkedinEmail{

	@JsonProperty("elements")
	private List<ElementsItem> elements;

	public void setElements(List<ElementsItem> elements){
		this.elements = elements;
	}

	public List<ElementsItem> getElements(){
		return elements;
	}

	@Override
 	public String toString(){
		return 
			"LinkedinEmail{" + 
			"elements = '" + elements + '\'' + 
			"}";
		}
}