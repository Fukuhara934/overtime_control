package overtime_control.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserEmailForm {
	@NotBlank
	private String newEmail;
	@NotBlank
	private String confirmEmail;
	@NotBlank
	private String password;
}
