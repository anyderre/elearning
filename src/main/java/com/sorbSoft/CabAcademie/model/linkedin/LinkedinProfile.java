package com.sorbSoft.CabAcademie.model.linkedin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LinkedinProfile{

	@JsonProperty("localizedLastName")
	private String localizedLastName;

	@JsonProperty("profilePicture")
	private ProfilePicture profilePicture;

	@JsonProperty("firstName")
	private FirstName firstName;

	@JsonProperty("lastName")
	private LastName lastName;

	@JsonProperty("id")
	private String id;

	@JsonProperty("localizedFirstName")
	private String localizedFirstName;
}