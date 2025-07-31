package overtime_control.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserProfileForm {
	private Integer id;
	
	@NotBlank(message = "必須項目です")
	private String firstName;
	
	@NotBlank(message = "必須項目です")
	private String familyName;
	
	@NotBlank(message = "必須項目です")
	@Email(message = "メールアドレスを入力してください")
	private String email;
	
	@NotNull(message = "必須項目です")
	private Integer departmentId;
}
