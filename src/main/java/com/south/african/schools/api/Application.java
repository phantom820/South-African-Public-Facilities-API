package com.south.african.schools.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SuppressWarnings(
		{ "checkstyle:hideutilityclassconstructor",
		  "checkstyle:filetabcharacter",
		  "checkstyle:missingjavadoctype" })
@SpringBootApplication
public class Application {
	public static void main(final String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
