package com.andus.common.dto.error;

import com.andus.common.code.ErrorCode;

import lombok.Data;

@Data
public class ErrorRes {
	
	private ErrorCode code;
    private String message;
    
    public ErrorRes(ErrorCode code, String message) {
    	
        this.code = code;
        this.message = message;
    }
}
