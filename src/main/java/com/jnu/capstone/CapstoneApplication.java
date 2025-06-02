package com.jnu.capstone;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileInputStream;
import java.io.IOException;
@SpringBootApplication
public class CapstoneApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@PostConstruct
	public void initFirebase() {
		try {
			FileInputStream serviceAccount =
					new FileInputStream("src/main/resources/firebase/firebase-service-key.json");

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
				System.out.println("✅ Firebase 초기화 성공");
			}
		} catch (IOException e) {
			System.err.println("❌ Firebase 초기화 실패: " + e.getMessage());
		}
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