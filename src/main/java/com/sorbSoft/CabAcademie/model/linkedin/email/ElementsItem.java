package com.sorbSoft.CabAcademie.model.linkedin.email;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ElementsItem{

	@JsonProperty("handle")
	private String handle;

	@JsonProperty("handle~")
	private Handle handle2;

}