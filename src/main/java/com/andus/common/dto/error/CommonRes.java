package com.andus.common.dto.error;

import lombok.Data;

@Data
public class CommonRes {
	
	private ErrorRes errorRes;
	
	public CommonRes(ErrorRes errorRes) {
        this.errorRes = errorRes;
    }
}
