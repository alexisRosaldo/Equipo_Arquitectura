package com.employees.spring.daos;

import java.util.List;
import java.util.Map;

import com.employees.spring.entities.Employee;

/**
 * Interface utilizada para para crear los contratos con Mysql.
 * @author Alexis Rosaldo
 * @version 1.0, 24/11/2021
 */
public interface MysqlConnector {
	
	/**
     * Devuelve la lista de empleados alojados en la base de datos.
     * @return Retorna la lista de empleados.
     */
	public List<Employee> listEmployees();
	
	/**
     * Crea un empleado que sera alojado en un nuevo registro de la base de datos.
     * @param employee Recibe un objeto de tipo Empleado.
     * @return Retorna la una mapa con el contenido de un empleado y el valor de retorno de la consulta sql.
     */
	public Map<String, Object> createEmployee(Employee employee);
	
	/**
     * Actualiza un empleado alojado en un registro de la base de datos.
     * @param employee Recibe un objeto de tipo Empleado.
     * @return Retorna un mapa con el contenido de un empleado y el valor de retorno de la consulta sql.
     */
	public Map<String, Object> updateEmployee(Employee employee);
	
	/**
     * Elimina un empleado alojado en un registro de la base de datos.
     * @param id Recibe el identificador de un Empleado.
     * @return Retorna un valor numerico que especifica la consulta sql.
     */
	public int deleteEmployee(Integer id);
}
