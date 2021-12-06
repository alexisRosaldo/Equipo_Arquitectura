package com.employees.spring;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * Clase usada para configurar la bade de datos con Mysql y las sentencias SQL.
 * @author Alexis Rosaldo
 * @version 1.0, 24/11/2021
 */
@Configuration
@ComponentScan
public class AppConfiguration {

	/** 
     * Configura la base de datos.
     * @return Retorna un objeto de tipo DriverManagerDataSource que contiene la configuracion de la base de datos.
     */
    @Bean
    public DriverManagerDataSource dataSource()
    {
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
    	dataSource.setUrl("jdbc:mysql://localhost:3306/employees-spring");
    	dataSource.setUsername("root");
		dataSource.setPassword("");
        return dataSource;
    }
    
    /** 
     * Metodo usado para configurar las sentencias SQL mediante la plantilla con un conjunto basico de operaciones JDBC, que permite el uso de 
     * parametros con nombre en lugar del tradicional '?' marcadores de posicion.
     * @param dataSource Recibe como entrada un objeto de tipo DriverManagerDataSource con los datos ingresados en el constructor DriverManagerDataSource.
     * @return Devuelve un objeto de tipo NamedParameterJdbcTemplate.
     */
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DriverManagerDataSource dataSource)
    {
        return new NamedParameterJdbcTemplate(dataSource);
    }
  
}
