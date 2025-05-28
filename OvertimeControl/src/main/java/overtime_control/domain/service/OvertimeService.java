package overtime_control.domain.service;

import java.util.List;

import overtime_control.domain.model.MOvertime;
import overtime_control.dto.OvertimeApprovalDTO;
import overtime_control.dto.OvertimeDTO;
import overtime_control.dto.OvertimeRequestDTO;

public interface OvertimeService {
	
	//申請
	public void requestOvertime(MOvertime overtime);
	//承認
	public void approvalUpdate(MOvertime overtime);
	//修正
	public void requestUpdate(MOvertime overtime);
	//報告
	public void reportUpdate(MOvertime overtime);
	
	//社員用
	
	public List<OvertimeDTO> getAllByUserId(Integer id);
	
	public List<OvertimeRequestDTO> getAllRequestByUserId(Integer id);
	
	public OvertimeRequestDTO getRequestById(Integer id);
	
	public OvertimeDTO getOvertimeById(Integer id);
	
	//承認者用
	public List<OvertimeApprovalDTO> getAllApproval();
	
	public OvertimeApprovalDTO getApprovalById(Integer id);
	
	public boolean isRequestOwner(Integer id, Integer userId);
	
	public boolean isExistsId(Integer id);
}
