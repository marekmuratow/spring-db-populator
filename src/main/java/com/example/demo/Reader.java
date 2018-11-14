package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Reader {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public void readAllData() {

		List<Book> result = jdbcTemplate.query("SELECT * FROM BOOK",
				(rs, rowNum) -> new Book(rs.getLong("id"), rs.getString("title"), rs.getString("description")));

		result.stream().forEach(System.out::println);
	}

}
