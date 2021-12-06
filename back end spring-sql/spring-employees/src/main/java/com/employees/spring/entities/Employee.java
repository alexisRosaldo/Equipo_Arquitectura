package com.employees.spring.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * Clase utilizada para representar un empleado y sus caracteristicas.
 * @author Alexis Rosaldo
 * @version 1.0, 24/11/2021
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Employee {
	
	/** 
     * @value id Identificador del empleado.
     * @value name Nombre del empleado.
     * @value email Correo del empleado.
     * @value address Direccion del empleado.
     * @value phone Telefono del empleado.
     */
	private Integer id;
	private String name;
	private String email;
	private String address;
	private Integer phone;
	
	/** 
     *Crea un objeto de tipo Employee (empleado).
     */
	public Employee() {
		
	}

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getPhone() {
		return phone;
	}

	public void setPhone(Integer phone) {
		this.phone = phone;
	}
	
	
	/** 
     *Devuelve una cadena de texto con el identificador(id), nombre(name), correo(email), direccion(address) y telefono(phone) de un empleado(Employee).
     *@return Retorna una cadena de texto con los atributos de un empleado (Employee).
     */
	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", email=" + email + ", address=" + address + ", phone="
				+ phone + "]";
	}
}
