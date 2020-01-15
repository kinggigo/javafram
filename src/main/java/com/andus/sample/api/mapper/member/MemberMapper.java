package com.andus.sample.api.mapper.member;

import org.apache.ibatis.annotations.Mapper;

import com.andus.sample.api.domain.member.Member;

@Mapper
public interface MemberMapper {
	Member findByCode(Integer code);
}
