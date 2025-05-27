package overtime_control.dto;

import java.time.LocalDateTime;

import lombok.Data;
import overtime_control.domain.model.MOvertime;
import overtime_control.domain.model.MUser;
import overtime_control.domain.model.OvertimeStatus;
import overtime_control.domain.model.UserRole;

//このほうがわかりやすいかなと変えてみたがどうなんだろう？
@Data
public class OvertimeApprovalDTO {
	private Integer id;
	private UserInfo user;
	private DepartmentInfo department;
	private RequestInfo request;
	private OvertimeStatus status;
	
	
	//user情報
	@Data
	public static class UserInfo {
		private String familyName;
		private String firstName;
		private UserRole role;
	}
	
	//部署情報
	@Data
	public static class DepartmentInfo {
		private String departmentName;
	}
	
	//申請情報
	@Data
	public static class RequestInfo {
		private LocalDateTime startTime;
		private LocalDateTime finishTime;
		private String reason;
	}
	
	public OvertimeApprovalDTO(MOvertime overtime) {
		this.id = overtime.getId();

		// ユーザー情報
		MUser user = overtime.getUser();
		UserInfo userInfo = new UserInfo();
		userInfo.setFamilyName(user.getFamilyName());
		userInfo.setFirstName(user.getFirstName());
		userInfo.setRole(user.getRole());
		this.user = userInfo;

		// 部署情報
		DepartmentInfo deptInfo = new DepartmentInfo();
		this.department = deptInfo;

		// 申請情報
		RequestInfo requestInfo = new RequestInfo();
		requestInfo.setStartTime(overtime.getRequestStartTime());
		requestInfo.setFinishTime(overtime.getRequestFinishTime());
		requestInfo.setReason(overtime.getReason());
		this.request = requestInfo;
		
		this.status = overtime.getStatus();
	}
}
