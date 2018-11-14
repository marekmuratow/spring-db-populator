package com.example.demo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestDataConfig.class);
		Reader reader = context.getBean(Reader.class);
		reader.readAllData();
		context.close();
	}

}
