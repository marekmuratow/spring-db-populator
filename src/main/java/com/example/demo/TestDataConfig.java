package com.example.demo;

import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

@Configuration
@PropertySource("classpath:db/db.properties")
@ComponentScan
public class TestDataConfig {

	@Value("${driverClassName}")
	private String driverClassName;

	@Value("${url}")
	private String url;

	@Value("${username}")
	private String username;

	@Value("$password}") // TODO encrypt
	private String password;

	@Value("classpath:db/test-data.sql")
	private Resource dataScript;

	@Value("classpath:db/schema.sql")
	private Resource schemaScript;

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Lazy
	@Bean
	public DataSource dataSource() {
		try {
			SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

			@SuppressWarnings("unchecked")
			Class<? extends Driver> driver = (Class<? extends Driver>) Class.forName(driverClassName);

			dataSource.setDriverClass(driver);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);

			DatabasePopulatorUtils.execute(databasePopulator(), dataSource);

			return dataSource;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Bean
	public DataSourceInitializer datasourceInitializer(final DataSource dataSource) {
		final DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
		dataSourceInitializer.setDataSource(dataSource);
		dataSourceInitializer.setDatabasePopulator(databasePopulator());
		return dataSourceInitializer;
	}

	private DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(schemaScript);
		populator.addScript(dataScript);
		return populator;
	}

	@Bean
	JdbcTemplate jdbcTemplate() {
		return new JdbcTemplate(dataSource());
	}

}
