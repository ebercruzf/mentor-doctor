package com.olinnova.mentordoctor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.CrossOrigin;


@EnableCaching
@EnableScheduling
@CrossOrigin(origins = "http://localhost:4200")
@SpringBootApplication(scanBasePackages = {
		"com.olinnova.mentordoctor",
		"com.olinnova.mentordoctor.api.service.impl"

})
public class MentorDoctorApplication {

	public static void main(String[] args) {
		SpringApplication.run(MentorDoctorApplication.class, args);
	}

}
