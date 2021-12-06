package com.employees.spring.controllers;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.employees.spring.daos.MySqlConnectorImpl;
import com.employees.spring.entities.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Clase con anotacion RestController que posee los metodos que reciben los endpoints y tambien generan la respuesta completa HTTP.
 * @author Alexis Rosaldo
 * @version 1.0, 24/11/2021
 */
@RestController
public class EmployeeController {
	
	@Autowired
	private MySqlConnectorImpl mySqlConnectorImpl;
	
	/** 
     * Metodo usado para listar los empleados alojados en la base de datos a partir de una consulta qque se recibe en un endpoint 
     * y es devuelto con la lista de empelados en formato json junto a las cabeceras, cuerpo y el estatus de la peticion.
     * @return Retorna un objeto de tipo ResponseEntity que representa la respuesta HTTP completa: codigo de estado, encabezados y cuerpo.
     */
	@CrossOrigin(origins = "http://localhost:8083")
	@GetMapping(value = "/employees")
	ResponseEntity<List<Employee>> listEmployees() {
		
		// Datos
		List<Employee> employees = new ArrayList<>();
		employees = mySqlConnectorImpl.listEmployees();
		HttpStatus status = HttpStatus.OK;
		
		// Headers
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("ExampleName", "ExampleKey");
		
		// Constructor respuesta
		ResponseEntity<List<Employee>> response = new ResponseEntity<List<Employee>>(employees, responseHeaders, status);
		
		return response;
	}
	
	/** 
     * Metodo usado para crear un empleado en la base de datos a partir de un objeto empleado que se recibe de un endpoint 
     * y es devuelto en formato json junto a las cabeceras, cuerpo y el estatus de la peticion.
     * @param headers Cabecera de la peticion http.
     * @param body Cuerpo de la peticion http.
     * @return Retorna un objeto de tipo ResponseEntity que representa la respuesta HTTP completa: codigo de estado, encabezados y cuerpo.
     */
	@CrossOrigin(origins = "http://localhost:8083")
	@PostMapping(value = "/employees")
	ResponseEntity<Employee> createEmployee(@RequestHeader Map<String, String> headers, @RequestBody String body) {
		
		// Muestra los headers de entrada
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		}
		
		HttpStatus status = null;
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Employee employeeOut = null;
		
		try {
			Employee employeeIn = objectMapper.readValue(body, Employee.class);
			System.out.println(employeeIn.toString());
			Map<String, Object> result = mySqlConnectorImpl.createEmployee(employeeIn);
			
			int code = (int) result.get("code");
			System.out.println(code);
			
			employeeOut = (Employee) result.get("employee");
			System.out.println(employeeOut);
			
			status = HttpStatus.CREATED;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = HttpStatus.BAD_REQUEST;
		}
		
		// Headers
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("ExampleName", "ExampleKey");

		// Constructor respuesta
		ResponseEntity<Employee> response = new ResponseEntity<Employee>(employeeOut, responseHeaders, status);
		
		return response;
	}
	
	/** 
     * Metodo usado para actualizar un empleado en la base de datos a partir del identificador que se recibe de un endpoint 
     * y es devuelto en formato json junto a las cabeceras, cuerpo y el estatus de la peticion.
     * @param id Identificador del empleado.
     * @param headers Cabecera de la peticion http.
     * @param body Cuerpo de la peticion http.
     * @return Retorna un objeto de tipo ResponseEntity que representa la respuesta HTTP completa: codigo 
     * de estado, encabezados y cuerpo.
     */
	@CrossOrigin(origins = "http://localhost:8083")
	@PutMapping(value = "/employees/{id}")
	ResponseEntity<Employee> putEmployee(@PathVariable(value = "id") String id, @RequestHeader Map<String, String> headers, @RequestBody String body) {
		
		// Muestra los headers de entrada
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
		}
		
		HttpStatus status = null;
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		Employee employeeOut = null;
		
		try {
			Employee employeeIn = objectMapper.readValue(body, Employee.class);
			employeeIn.setId(Integer.valueOf(id));
			Map<String, Object> result = mySqlConnectorImpl.updateEmployee(employeeIn);
			
			int code = (int) result.get("code");
			System.out.println(code);
			
			employeeOut = (Employee) result.get("employee");
			System.out.println(employeeOut);
			
			status = HttpStatus.OK;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = HttpStatus.BAD_REQUEST;
		}
		
		// Headers
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("ExampleName", "ExampleKey");

		// Constructor respuesta
		ResponseEntity<Employee> response = new ResponseEntity<Employee>(employeeOut, responseHeaders, status);
		
		return response;
	}
	
	/** 
     * Metodo que es usado para eliminar un empleado de la base de datos a partir del identificador que se recibe de un endpoint 
     * y es devuelto en formato json junto a las cabeceras, cuerpo y el estatus de la peticion.
     * @param id Identificador del empleado.
     * @param headers Cabecera de la peticion http.
     * @return Retorna un objeto de tipo ResponseEntity que representa la respuesta HTTP completa: codigo 
     * de estado, encabezados y cuerpo.
     */
	@CrossOrigin(origins = "http://localhost:8083")
	@DeleteMapping(value = "/employees/{id}")
	ResponseEntity<Employee> deleteEmployee(@PathVariable(value = "id") String id, @RequestHeader Map<String, String> headers) {
		
		System.out.println("id: " + id);
		
		int code = mySqlConnectorImpl.deleteEmployee(Integer.valueOf(id));
		
		HttpStatus status = null;
		
		if (code == 1) {
			status = HttpStatus.OK;
		}
		else {
			status = HttpStatus.NOT_FOUND;
		}
		
		// Headers
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("ExampleName", "ExampleKey");
		
		//objeto devuelto nulo
		Employee employee = new Employee();
		
		// Constructor respuesta
		ResponseEntity<Employee> response = new ResponseEntity<Employee>(employee, responseHeaders, status);
		
		return response;
	}
}
