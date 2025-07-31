package overtime_control.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import overtime_control.domain.model.MUser;

@Mapper
public interface UserRepository {
	//全てのユーザを取得
	public List<MUser> findAll();

	public MUser findById(Integer id);

	//ログイン用
	public MUser findByEmail(String email);

	//ユーザーの追加
	public void insert(MUser user);

	//ユーザーの更新
	public void update(MUser user);
	
	//パスワードの更新
	public void updatePassword(MUser user);
	
	//Eメールの変更
	public void updateEmail(String email, Integer id);

	//ユーザーの削除
	public void delete(Integer id);
}
