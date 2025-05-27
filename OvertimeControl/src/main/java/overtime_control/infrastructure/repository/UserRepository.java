package overtime_control.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import overtime_control.domain.model.MUser;

@Mapper
public interface UserRepository {
	//全てのユーザを取得
	public List<MUser> findAll();

	public MUser findById(@Param("id") Integer id);

	//ログイン用
	public MUser findByEmail(@Param("email") String email);

	//ユーザーの追加
	public void insert(MUser user);

	//ユーザーの更新
	public void update(MUser user);

	//ユーザーの削除
	public void delete(@Param("id") Integer id);
}
