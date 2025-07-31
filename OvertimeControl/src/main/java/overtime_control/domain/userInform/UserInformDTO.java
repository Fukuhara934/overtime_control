package overtime_control.domain.userInform;

import lombok.AllArgsConstructor;
import lombok.Data;
import overtime_control.domain.model.UserRole;

@Data
@AllArgsConstructor
public class UserInformDTO {
	private String familyName;
	private String firstName;
	private String departmentName;
	private UserRole role;
}
