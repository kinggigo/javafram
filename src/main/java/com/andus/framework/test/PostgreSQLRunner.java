package com.andus.framework.test;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PostgreSQLRunner implements ApplicationRunner {

    @Autowired
    DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Value("${spring.profiles.active}") String active;

    @Override
    public void run(ApplicationArguments args) throws Exception {
    	
//    	BCryptPasswordEncoder bcr = new BCryptPasswordEncoder();
//    	String result = bcr.encode("1234");  
//    	System.out.println("¾ÏÈ£ === " + result);
    	
//    	log.debug("debug");
    	
//    	System.out.print(active);
    	
//        try(Connection connection = dataSource.getConnection()){
//            System.out.println(connection);
//            String URL = connection.getMetaData().getURL();
//            System.out.println(URL);
//            String User = connection.getMetaData().getUserName();
//            System.out.println(User);
//
//            Statement statement = connection.createStatement();
//            String sql = "CREATE TABLE ACCOUNT(" +
//                    "ID INTEGER NOT NULL," +
//                    "NAME VARCHAR(255)," +
//                    "PRIMARY KEY(ID))";
//            statement.executeUpdate(sql);
//        }
//
//        jdbcTemplate.execute("INSERT INTO ACCOUNT VALUES(3, 'saelobi')");
    }
}