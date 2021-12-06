package com.employees.spring.daos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import com.employees.spring.entities.Employee;

/**
 * Clase utilizada para implementar los contratos de la interfaz MysqlConnector.
 * Posee los métodos para poder interactuar y accesar con la base de datos Mysql.
 * @author Alexis Rosaldo
 * @version 1.0, 24/11/2021
 */
@Service
public class MySqlConnectorImpl implements MysqlConnector {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MySqlConnectorImpl.class);
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	/**
     * Devuelve la lista de empleados alojados en la base de datos.
     * @return Retorna la lista de empleados.
     */
	@Override
	public List<Employee> listEmployees() {

		// LOGGER.info("Iniciando getUsers");

		List<Employee> listEmployees = new ArrayList<>();

		try {
			List<Map<String, Object>> result = namedParameterJdbcTemplate.queryForList("select * from employees",
					new HashMap<String, Object>());
			// LOGGER.info(result.toString());

			for (Map<String, Object> map : result) {
				listEmployees.add(createEmployee(map));
			}
		} catch (EmptyResultDataAccessException e) {
			// LOGGER.info("No se encontro ningun resultado");
		}

		// LOGGER.info("Terminando getUsers");
		return listEmployees;
	}
	
	/**
     * Devuelve una lista empleados a partir del mapa que se recibe como entrada.
     * @param map Recibe un mapa con el contenido de los empleados.
     * @return Retorna una la lista de empleados.
     */
	private Employee createEmployee(Map<String, Object> map) {
		Employee employee = new Employee();
		employee.setId((Integer) map.get("id"));
		employee.setName((String) map.get("name"));
		employee.setEmail((String) map.get("email"));
		employee.setAddress((String) map.get("address"));
		employee.setPhone((Integer) map.get("phone"));
		return employee;
	}
	
	private static final String GET_LAST = "SELECT * from employees WHERE name = :name AND email = :email AND address = :address AND phone = :phone ORDER BY id DESC LIMIT 1";
	private static final String POST = "INSERT INTO employees (id, name, email, address, phone) VALUES (null, :name, :email, :address, :phone)";
	
	/**
     * Crea un empleado que será alojado en un nuevo registro de la base de datos.
     * @param employee Recibe un objeto de tipo Empleado.
     * @return Retorna una mapa con el contenido de un empleado y el resultado de la consulta sql.
     */
	@Override
	public Map<String, Object> createEmployee(Employee employee) {
		LOGGER.info("Iniciando Create Employee");
		
		Map<String, Object> result = new HashMap<>();
		
		Map<String, Object> query = new HashMap<>();
		query.put("name", employee.getName());
		query.put("email", employee.getEmail());
		query.put("address", employee.getAddress());
		query.put("phone", employee.getPhone());
		
		// LOGGER.info("El mapa del query es: {}", query.toString());
		try {
			LOGGER.info("Iniciando Query");
			int code = namedParameterJdbcTemplate.update(POST, query);
			result.put("code", code);
			
			Map<String, Object> employeeMap = namedParameterJdbcTemplate.queryForMap(GET_LAST, query);
			Employee employeeOut = createEmployee(employeeMap);
			result.put("employee", employeeOut);
			
		} catch (DuplicateKeyException e) {
			LOGGER.info("El registro ya existe: {}", e);
		} catch (DataAccessException e) {
			LOGGER.info("El registro fallo: {}", e);
		}
		catch (Exception e) {
			LOGGER.info("Ocurrio un error: {}", e);
		}

		// LOGGER.info("Terminando postUser");
		return result;
	}
	
	private static final String GET = "SELECT * from employees WHERE id = :id";
	private static final String PUT = "UPDATE employees SET name = :name, email = :email, address = :address, phone= :phone WHERE id = :id";

	/**
     * Actualiza un empleado alojando en un registro en la base de datos.
     * @param employee Recibe un objeto de tipo Empleado.
     * @return Retorna la una mapa con el contenido de un empleado.
     */
	@Override
	public Map<String, Object> updateEmployee(Employee employee) {
		Map<String, Object> result = new HashMap<>();
		
		Map<String, Object> query = new HashMap<>();
		query.put("id", employee.getId());
		query.put("name", employee.getName());
		query.put("email", employee.getEmail());
		query.put("address", employee.getAddress());
		query.put("phone", employee.getPhone());
		
		int code = 0;

		try {
			LOGGER.info("Iniciando Query");
			code = namedParameterJdbcTemplate.update(PUT, query);

			Map<String, Object> employeeMap = namedParameterJdbcTemplate.queryForMap(GET, query);
			Employee employeeOut = createEmployee(employeeMap);
			result.put("employee", employeeOut);
		} catch (DuplicateKeyException e) {
			// LOGGER.info("El registro ya existe: {}", e);
			code = -1;
		} catch (DataAccessException e) {
			// LOGGER.info("El registro fallo: {}", e);
		}
		
		result.put("code", code);

		// LOGGER.info("Terminando postUser");
		return result;
	}
	
	private static final String DELETE = "DELETE from employees WHERE id = :id";

	
	/**
     * Elimina un empleado alojando en un registro en la base de datos.
     * @param id Recibe el identificador de un Empleado.
     * @return Retorna un valor numerico que especifica la consulta sql.
     */
	@Override
	public int deleteEmployee(Integer id) {
		int result = 0;
		Map<String, Object> query = new HashMap<>();
		query.put("id", id);

		try {
			LOGGER.info("Iniciando Query");
			result = namedParameterJdbcTemplate.update(DELETE, query);
		} catch (DuplicateKeyException e) {
			LOGGER.info("El registro ya existe: {}", e);
			result = -1;
		} catch (DataAccessException e) {
			LOGGER.info("Error al acceder a la base de datos: {}", e);
		}
		// LOGGER.info("Terminando postUser");
		return result;

	}

}
