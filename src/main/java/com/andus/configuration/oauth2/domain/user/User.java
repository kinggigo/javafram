package com.andus.configuration.oauth2.domain.user;

import java.util.Date;

import lombok.Data;

@Data
public class User {
	
	private Long id;
	
	private String userName;
	
	private String password;
	
	private String userType;

	private Date regDate = new Date();
}