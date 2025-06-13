package overtime_control.dto;

import java.time.LocalDateTime;

import lombok.Data;
import overtime_control.domain.model.MOvertime;
import overtime_control.domain.model.MUser;
import overtime_control.domain.model.OvertimeStatus;
import overtime_control.domain.model.UserRole;
import overtime_control.domain.model.WorkPattern;

@Data
public class OvertimeDTO {
    private Integer id;
    private Integer displayNumber;
    private UserInfo user;
    private DepartmentInfo department;
    private RequestInfo request;
    private ApprovalInfo approval;
    private ReportInfo report;
    private OvertimeStatus status;

    @Data
    public static class UserInfo {
        private String familyName;
        private String firstName;
        private UserRole role;
    }

    @Data
    public static class DepartmentInfo {
        private String departmentName;
    }

    @Data
    public static class RequestInfo {
        private WorkPattern pattern;
        private LocalDateTime startTime;
        private LocalDateTime finishTime;
        private String reason;

        
        public RequestInfo() {
            this.pattern = WorkPattern.NONE;
        }
    }

    @Data
    public static class ApprovalInfo {
        private String approverName;
        private LocalDateTime approvalTime;
        private String rejectReason;
    }

    @Data
    public static class ReportInfo {
        private LocalDateTime startTime;
        private LocalDateTime finishTime;
        private Integer breaktime;
        private String content;
        private LocalDateTime submitTime;
    }

    public OvertimeDTO() {
        this.user = new UserInfo();
        this.department = new DepartmentInfo();
        this.request = new RequestInfo();
        this.approval = new ApprovalInfo();
        this.report = new ReportInfo();
        this.displayNumber = getDisplayNumber();
    }

    public OvertimeDTO(MOvertime overtime) {
        this.id = overtime.getId();
        this.displayNumber = overtime.getDisplayNumber();

        // ユーザー情報
        MUser user = overtime.getUser();
        if (user != null) {
            UserInfo userInfo = new UserInfo();
            userInfo.setFamilyName(user.getFamilyName());
            userInfo.setFirstName(user.getFirstName());
            userInfo.setRole(user.getRole());
            this.user = userInfo;
        } else {
            this.user = new UserInfo();
        }

        // 部署情報
        if (overtime.getDepartment() != null) {
            DepartmentInfo deptInfo = new DepartmentInfo();
            deptInfo.setDepartmentName(overtime.getDepartment().getDepartmentName());
            this.department = deptInfo;
        } else {
            this.department = new DepartmentInfo();
        }

        // 申請情報
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setPattern(overtime.getPattern());
        requestInfo.setStartTime(overtime.getRequestStartTime());
        requestInfo.setFinishTime(overtime.getRequestFinishTime());
        requestInfo.setReason(overtime.getReason());
        this.request = requestInfo;

        // 承認情報
        MUser approver = overtime.getApprover();
        if (approver != null || overtime.getApprovalTime() != null) {
            ApprovalInfo approvalInfo = new ApprovalInfo();
            approvalInfo.setApproverName(ApproverFullName(approver));
            approvalInfo.setApprovalTime(overtime.getApprovalTime());
            approvalInfo.setRejectReason(overtime.getRejectReason());
            this.approval = approvalInfo;
        } else {
            this.approval = new ApprovalInfo();
        }

        // 報告情報
        if (overtime.getReportStartTime() != null || overtime.getReportFinishTime() != null
                || overtime.getBreaktime() != null || overtime.getReport() != null
                || overtime.getSubmitTime() != null) {
            ReportInfo reportInfo = new ReportInfo();
            reportInfo.setStartTime(overtime.getReportStartTime());
            reportInfo.setFinishTime(overtime.getReportFinishTime());
            reportInfo.setBreaktime(overtime.getBreaktime());
            reportInfo.setContent(overtime.getReport());
            reportInfo.setSubmitTime(overtime.getSubmitTime());
            this.report = reportInfo;
        } else {
            this.report = new ReportInfo();
        }

        // 状態
        this.status = overtime.getStatus();
    }

    private String ApproverFullName(MUser approver) {
        if (approver == null) {
            return null;
        }
        return approver.getFamilyName() + approver.getFirstName();
    }
}
