package com.andus.sample.api.service.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.andus.sample.api.domain.member.Member;
import com.andus.sample.api.mapper.member.MemberMapper;

@Service
public class MemberService {
	
	@Autowired
	private MemberMapper memberMapper;

	public Member getMember(Integer code) {
        return memberMapper.findByCode(code);
    }
}
