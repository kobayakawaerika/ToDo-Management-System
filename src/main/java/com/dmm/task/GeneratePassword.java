package com.dmm.task;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePassword {

	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		String rawPassword = "admin";
		
		String password = getEncodePassword(rawPassword);
		
		System.out.println(password);
		
	}
	
	private static String getEncodePassword(String rawPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(rawPassword);
	}

}
