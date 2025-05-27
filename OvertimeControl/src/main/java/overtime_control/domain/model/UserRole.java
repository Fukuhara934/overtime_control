package overtime_control.domain.model;

import java.util.Arrays;

public enum UserRole {
	EMPLOYEE(1, "社員", "ROLE_EMPLOYEE"),
	SUPERVISOR(2, "次長", "ROLE_SUPERVISOR"),
	MANAGER(3, "課長", "ROLE_MANAGER"),
	DIRECTOR(4, "部長", "ROLE_DIRECTOR");

	private final int id;
	private final String RoleName;
	private final String securityRole;

	UserRole(int id, String RoleName, String securityRole) {
		this.id = id;
		this.RoleName = RoleName;
		this.securityRole = securityRole;
	}
	
	public int getId() {
		return id;
	}
	
	public String getRoleName() {
		return RoleName;
	}
	
	public String getSecurityRole() {
		return securityRole;
	}
	
	public static UserRole fromId(int id) {
		return Arrays.stream(values())
				.filter(role -> role.id == id)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("役職が適当ではありません: " + id));
	}
}
