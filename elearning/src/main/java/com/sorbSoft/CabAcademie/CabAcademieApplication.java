package com.sorbSoft.CabAcademie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class CabAcademieApplication{

	public static void main(String[] args) {
//		ApplicationContext appContext = SpringApplication.run(CabAcademieApplication.class, args);
//
//		InitServices initServices= (InitServices) appContext.getBean("initServices");
//		initServices.init();

		SpringApplication.run(CabAcademieApplication.class, args);
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}


}
