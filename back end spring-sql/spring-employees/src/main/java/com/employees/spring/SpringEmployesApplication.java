package com.employees.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de spring.
 * @author Alexis Rosaldo
 * @version 1.0 24/11/2021
 */
@SpringBootApplication
public class SpringEmployesApplication {
	
	/**
	 * Metodo principal para ejecutar spring.
	 * @param args Argumentos del metodo principal de spring.
	 */
	public static void main(String[] args) {
		SpringApplication.run(SpringEmployesApplication.class, args);
	}

}
