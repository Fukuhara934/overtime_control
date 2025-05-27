package overtime_control.domain.service;

import java.util.List;

import overtime_control.domain.model.MOvertime;
import overtime_control.dto.OvertimeApprovalDTO;
import overtime_control.dto.OvertimeDTO;
import overtime_control.dto.OvertimeRequestDTO;

public interface OvertimeService {
	
	//申請書の書き込み
	public void requestOvertime(MOvertime overtime);
	
	public void approvalOvertime(MOvertime overtime);
	
	public void requestUpdate(MOvertime overtime);
	
	public void reportOvertime(MOvertime overtime);
	
	//社員用
	
	public List<OvertimeDTO> getAllByUserId(Integer id);
	
	public List<OvertimeRequestDTO> getAllRequestByUserId(Integer id);
	
	public OvertimeRequestDTO requestById(Integer id);
	
	public OvertimeDTO getById(Integer id);
	public MOvertime getTest(Integer id);
	
	//承認者用
	public List<OvertimeApprovalDTO> getAllOvertimeApproval();
	
	public OvertimeApprovalDTO getOvertimeApproval(Integer id);
	
	public boolean isRequestOwner(Integer id, Integer userId);
	
	public boolean isExistsId(Integer id);
}
