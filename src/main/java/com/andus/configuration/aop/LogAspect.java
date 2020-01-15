package com.andus.configuration.aop;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("within(com.andus.sample.*..*)")
    public Object logging(ProceedingJoinPoint pjp) throws Throwable {
    	
    	Gson gson = new GsonBuilder().setPrettyPrinting().create();
    	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    	
    	Object[] objArr = pjp.getArgs();
		if (objArr != null && objArr.length > 0) {
			for (Object obj : objArr) {
				log.info("[REQUEST] \n [{}][{}][{}] : \n {}", request.getProtocol(), request.getMethod(), request.getServletPath(), gson.toJson(obj).toString());
			}
		}
    	
        log.info("start - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
        
        Object result = pjp.proceed();
        
        log.info("finished - " + pjp.getSignature().getDeclaringTypeName() + " / " + pjp.getSignature().getName());
        
        log.info("[RESPONSE] \n [{}] : \n {} ", request.getServletPath(), gson.toJson(result));
        
        return result;
    }
}