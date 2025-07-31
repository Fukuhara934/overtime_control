package overtime_control.domain.service;

import java.util.List;

import overtime_control.domain.model.MUser;

public interface UserService {
	//loginユーザーの情報取得
	public MUser getUserByEmail(String email);

	public List<MUser> getAllUsers();

	public MUser getUserById(Integer id);

	public void createUser(MUser user);

	public void updateUser(MUser user);
	
	public void updatePassword(MUser user);
	
	public void updateEmail(String email, Integer id);

	public void deleteUser(Integer id);
}
