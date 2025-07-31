package overtime_control.dto;

import lombok.Data;
import overtime_control.domain.model.Department;

@Data
public class UserProfileDTO {
	private String firstName;
	private String familyName;
	private String email;
	private Department department;
}
