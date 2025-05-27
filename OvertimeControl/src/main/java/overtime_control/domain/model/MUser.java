package overtime_control.domain.model;

import lombok.Data;

@Data
public class MUser {
	private Integer id;
	private String email;
	private String password;
	private String familyName;
	private String firstName;
	/*社員・次長・課長・部長*/
	private UserRole role;
	private Department department;
}
