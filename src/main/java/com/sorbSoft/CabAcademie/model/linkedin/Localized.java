package com.sorbSoft.CabAcademie.model.linkedin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Localized{

	@JsonProperty("en_US")
	private String enUS;

	@JsonProperty("ru_RU")
	private String ruRu;

}