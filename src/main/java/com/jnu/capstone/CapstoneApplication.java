package com.jnu.capstone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.jnu.capstone.entity.User;
import com.jnu.capstone.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CapstoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public CommandLineRunner testInsert(UserRepository userRepository) {
//		return args -> {
//			User user = new User();
//			user.setUserId(1);
//			user.setEmail("test@example.com");
//			user.setPword("pass123");
//			user.setNickname("tester");
//			user.setCampus("전남대학교");
//			user.setDepartment("인공지능학부");
//			user.setStudentNum(2222222);
//
//			userRepository.save(user);xp
//			System.out.println("사용자 저장 완료!");
//		};
//	}
}