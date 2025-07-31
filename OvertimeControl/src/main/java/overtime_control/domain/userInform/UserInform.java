package overtime_control.domain.userInform;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class UserInform extends User {
	private Integer id;
	private String familyName;
	private String firstName;
	private String departmentName;
	private Integer departmentId;

	public UserInform (String userName, String password, Collection<? extends GrantedAuthority> authorities,
			Integer id, String familyName, String firstName, String departmentName, Integer departmentId) {
		super(userName, password, authorities);
		this.id = id;
		this.familyName = familyName;
		this.firstName = firstName;
		this.departmentName = departmentName;
		this.departmentId = departmentId;
	}
	
	public Integer getId() {
		return id;
	}

	public String getFamilyName() {
		return familyName;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public String getDepartmentName() {
		return departmentName;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}
	
}
