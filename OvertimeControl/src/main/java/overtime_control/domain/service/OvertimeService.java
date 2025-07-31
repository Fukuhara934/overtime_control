package overtime_control.domain.service;

import java.time.LocalDateTime;
import java.util.List;

import overtime_control.domain.model.MOvertime;
import overtime_control.domain.model.OvertimeStatus;
import overtime_control.dto.OvertimeApprovalDTO;
import overtime_control.dto.OvertimeDTO;
import overtime_control.dto.OvertimeRequestDTO;

public interface OvertimeService {
	
	// --- CRUD操作 ---
	void requestOvertime(MOvertime overtime);
	void approvalUpdate(MOvertime overtime);
	void requestUpdate(MOvertime overtime);
	void reportUpdate(MOvertime overtime);
	void requestDelete(Integer id);
	
	// --- 社員用 ---
	List<OvertimeDTO> getAllByUserId(Integer userId);
	List<OvertimeRequestDTO> getAllRequestByUserId(Integer userId);
	OvertimeRequestDTO getRequestById(Integer requestId);
	OvertimeDTO getOvertimeById(Integer id);

	List<OvertimeDTO> getUserOvertimeInPeriod(Integer userId, LocalDateTime start, LocalDateTime finish, int limit, int offset);
	public int countByUserIdAndPeriodAndStatus(Integer userId, LocalDateTime start, LocalDateTime finish, OvertimeStatus status);

	// --- 承認者用 ---
	List<OvertimeApprovalDTO> getAllApproval();
	OvertimeApprovalDTO getApprovalById(Integer id);
	public List<OvertimeDTO> getCSVOvertimeInPeriod(LocalDateTime start, LocalDateTime finish);
	List<OvertimeDTO> getApproverOvertimeInPeriod(LocalDateTime start, LocalDateTime finish, int limit, int offset);
	public int countApproverOvertimeInPeriodandStatus(LocalDateTime start, LocalDateTime finish, OvertimeStatus status);

	// --- バリデーション・確認用 ---
	boolean isRequestOwner(Integer overtimeId, Integer userId, Integer status);
	boolean existsById(Integer id);
}
