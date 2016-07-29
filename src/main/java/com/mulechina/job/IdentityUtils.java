package com.mulechina.job;

import java.util.UUID;

public class IdentityUtils {
	public static String generatorUUID(String name){
		return UUID.randomUUID().toString();
	}
}
