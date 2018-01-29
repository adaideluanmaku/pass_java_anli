package com.medicom.passlan.inter.imp.sys;

import org.springframework.stereotype.Service;

import com.medicom.passlan.inter.ObjectInfaceImpl;

@Service
public class RedisClieanImpl extends ObjectInfaceImpl{
	
	
	public void cleanKeyAll(){
		this.cleanalldate();
		
	}
	
	

}
