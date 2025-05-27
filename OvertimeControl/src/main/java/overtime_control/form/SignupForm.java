package overtime_control.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import overtime_control.domain.model.UserRole;

@Data
public class SignupForm {
	
	@NotBlank(message = "メールアドレスは必須です")
	private String email;
	@NotBlank(message = "パスワードを入力してください")
	private String password;
	@NotBlank(message = "名字を入力してください")
	private String familyName;
	@NotBlank(message = "名前を入力してください")
	private String firstName;
	@NotNull(message = "役職を入力してください")
	private UserRole role;
	@NotNull(message = "部署を選択してください")
	private Integer departmentId;
}
