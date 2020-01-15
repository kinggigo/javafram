package com.andus.configuration.oauth2.mapper.user;

import org.apache.ibatis.annotations.Mapper;

import com.andus.configuration.oauth2.domain.user.User;

@Mapper
public interface UserMapper {
	User findByUsername(String username);
}