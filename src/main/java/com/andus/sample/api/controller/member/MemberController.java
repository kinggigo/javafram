package com.andus.sample.api.controller.member;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.andus.common.exception.BusinessException;
import com.andus.sample.api.domain.member.Member;
import com.andus.sample.api.service.member.MemberService;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

@Controller
@Validated
@Slf4j
@RequestMapping(value = "/sample/api/member")
public class MemberController {
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
	private MemberService sampleService;
	
	@GetMapping(path="/{code}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(code = HttpStatus.OK)
    public @ResponseBody
    Member find(@NotNull @PathVariable(name = "code") Integer code) {
		
		log.info("inbound member request. code={}", code);
		
		Member member = sampleService.getMember(code);
		
		if(member == null) {
			throw new BusinessException(messageSource.getMessage("error.member.find", new String[]{code.toString()}, Locale.getDefault()));
		}
		
		return member;
	}
	
//	@PreAuthorize("#oauth2.hasScope('read')")
//	@GetMapping(path="/{code}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseStatus(code = HttpStatus.OK)
//    public @ResponseBody
//    Member find(@AuthenticationPrincipal OAuth2Authentication authentication, @PathVariable(name = "code") Integer code) {
//		
//		log.info("inbound member request. code={}", code);
//		
//		String username = authentication.getUserAuthentication().getPrincipal().toString();
//        Set<String> scopes = authentication.getOAuth2Request().getScope();
//        log.info("Member's username = {}", username);
//        log.info("Client scope info = {}", scopes);
//		
//		return sampleService.getMember(code);
//	}
}
