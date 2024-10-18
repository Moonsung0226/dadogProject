package com.keduit.dadog;

import com.keduit.dadog.constant.Role;
import com.keduit.dadog.entity.User;
import com.keduit.dadog.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DadogApplicationTests {
	@Autowired
	private UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("어드민 생성")
	public void adminCreate(){
		User user = userRepository.findByUserId("moon");
		System.out.println(user.getUserNo());
		user.setRole(Role.ADMIN);
		userRepository.save(user);
	}

}
