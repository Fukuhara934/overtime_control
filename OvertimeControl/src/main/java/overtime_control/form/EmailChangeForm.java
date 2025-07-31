package overtime_control.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailChangeForm {
	@NotBlank(message = "必須項目です")
	private String newEmail;
	@NotBlank(message = "必須項目です")
	private String password;
}
