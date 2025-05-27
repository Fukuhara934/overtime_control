package overtime_control.domain.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MOvertime {
	/*申請書部分*/
	private Integer id;
	/*部署名*/
	private Department department;
	/*申請者名*/
	private MUser user;
	/*勤務時間*/
	private WorkPattern pattern;
	/*残業予定時間*/
	private LocalDateTime requestStartTime;
	private LocalDateTime requestFinishTime;
	private String reason;
	
	/*承認者部分*/
	private MUser approver;
	private LocalDateTime approvalTime;
	private String rejectReason;
	
	/*報告書部分*/
	private LocalDateTime reportStartTime;
	private LocalDateTime reportFinishTime;
	private Integer breaktime;
	private String report;
	private LocalDateTime submitTime;
	
	private OvertimeStatus status;
}
