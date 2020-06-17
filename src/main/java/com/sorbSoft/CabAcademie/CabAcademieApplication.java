package com.sorbSoft.CabAcademie;

import com.sorbSoft.CabAcademie.Services.InitService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EntityScan(basePackages = "com.sorbSoft.CabAcademie")
@EnableJpaRepositories(basePackages="com.sorbSoft.CabAcademie")
@EnableAsync
public class CabAcademieApplication{

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(CabAcademieApplication.class, args);
		InitService initService = (InitService) applicationContext.getBean("initService");
		initService.init();
	}

	@PostConstruct
	void started() {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}
}
