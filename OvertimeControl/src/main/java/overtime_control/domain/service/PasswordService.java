package overtime_control.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public String hashPassword(String rawPassword) {
		return passwordEncoder.encode(rawPassword);
	}

}
