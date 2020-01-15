package com.andus.common.exception;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.andus.common.code.ErrorCode;
import com.andus.common.dto.error.CommonRes;
import com.andus.common.dto.error.ErrorRes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerExceptionHandler {
	
	@Autowired
	MessageSource messageSource;

	@ResponseStatus(HttpStatus.BAD_REQUEST) 
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public CommonRes handleBusinessException(HttpServletRequest request, HttpServletResponse response, Object handler, BusinessException ex) {
		
		log.warn(ex.getMessage(), ex);
    	
        return new CommonRes(new ErrorRes(ErrorCode.BAD_REQUEST, ex.getMessage()));
    }
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) 
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public CommonRes handleException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		
		log.warn(ex.getMessage(), ex);
    	
        return new CommonRes(new ErrorRes(ErrorCode.INTERNAL_ERROR, messageSource.getMessage("error.server", null, Locale.getDefault())));
    }
}