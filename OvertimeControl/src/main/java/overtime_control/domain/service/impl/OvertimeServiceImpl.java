package overtime_control.domain.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overtime_control.domain.model.MOvertime;
import overtime_control.domain.service.OvertimeService;
import overtime_control.dto.OvertimeApprovalDTO;
import overtime_control.dto.OvertimeDTO;
import overtime_control.dto.OvertimeRequestDTO;
import overtime_control.infrastructure.repository.OvertimeRepository;

@Service
public class OvertimeServiceImpl implements OvertimeService {

	@Autowired
	OvertimeRepository overtimeRepository;

	//申請時
	public void requestOvertime(MOvertime ovetime) {
		overtimeRepository.insertRequest(ovetime);
	}

	//承認時
	public void approvalUpdate(MOvertime overtime) {
		overtimeRepository.updateApproval(overtime);
	}

	//修正時
	public void requestUpdate(MOvertime overtime) {
		overtimeRepository.updateRequest(overtime);
	}

	//報告時
	public void reportUpdate(MOvertime overtime) {
		overtimeRepository.updateReport(overtime);
	}

	//社員用
	public List<OvertimeDTO> getAllByUserId(Integer id) {
		List<MOvertime> overtimes = overtimeRepository.selectReportedByUserId(id);
		return overtimes.stream().map(overtime -> new OvertimeDTO(overtime)).collect(Collectors.toList());
	}

	public List<OvertimeRequestDTO> getAllRequestByUserId(Integer id) {
		List<MOvertime> overtimes = overtimeRepository.selectRequestByUserId(id);
		return overtimes.stream().map(overtime -> new OvertimeRequestDTO(overtime)).collect(Collectors.toList());
	}

	public OvertimeDTO getOvertimeById(Integer id) {
		MOvertime overtime = overtimeRepository.selectById(id);
		return new OvertimeDTO(overtime);
	}

	//報告作成時のId確認用
	public OvertimeRequestDTO getRequestById(Integer id) {
		MOvertime overtime = overtimeRepository.findById(id);
		return new OvertimeRequestDTO(overtime);
	}

	//承認者用
	public List<OvertimeApprovalDTO> getAllApproval() {
		List<MOvertime> overtimes = overtimeRepository.findAllApproval();
		return overtimes.stream().map(overtime -> new OvertimeApprovalDTO(overtime)).collect(Collectors.toList());
	}

	public OvertimeApprovalDTO getApprovalById(Integer id) {
		MOvertime overtime = overtimeRepository.findById(id);
		return new OvertimeApprovalDTO(overtime);
	}

	//idとユーザーが同じか確認用
	public boolean isRequestOwner(Integer id, Integer userId) {
		return overtimeRepository.isRequestOwner(id, userId);
	}

	//idの存在確認用
	public boolean isExistsId(Integer id) {
		return overtimeRepository.isExistsId(id);
	}
}
