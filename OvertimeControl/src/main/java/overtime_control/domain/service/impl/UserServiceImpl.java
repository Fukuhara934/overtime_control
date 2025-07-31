package overtime_control.domain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overtime_control.domain.model.MUser;
import overtime_control.domain.service.UserService;
import overtime_control.infrastructure.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	//ログイン用
	@Override
	public MUser getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public List<MUser> getAllUsers() {
		return userRepository.findAll();
	}

	@Override
	public MUser getUserById(Integer id) {
		return userRepository.findById(id);
	}

	@Override
	public void createUser(MUser user) {
		userRepository.insert(user);
	}

	@Override
	public void updateUser(MUser user) {
		userRepository.update(user);
	}
	
	@Override
	public void updatePassword(MUser user) {
		userRepository.updatePassword(user);
	}
	
	@Override
	public void updateEmail(String email, Integer id) {
		userRepository.updateEmail(email, id);
	}

	@Override
	public void deleteUser(Integer id) {
		userRepository.delete(id);
	}
}
