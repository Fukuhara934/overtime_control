package overtime_control.domain.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overtime_control.domain.model.MOvertime;
import overtime_control.domain.model.OvertimeStatus;
import overtime_control.domain.service.OvertimeService;
import overtime_control.dto.OvertimeApprovalDTO;
import overtime_control.dto.OvertimeDTO;
import overtime_control.dto.OvertimeRequestDTO;
import overtime_control.infrastructure.repository.OvertimeRepository;

@Service
public class OvertimeServiceImpl implements OvertimeService {

	//命名規則何を行うか＋条件＋andで条件を足していく
	
	
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

	//削除時
	public void requestDelete(Integer id) {
		overtimeRepository.deleteRequest(id);
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
	
	//期間を指定して検索
	public List<OvertimeDTO> getUserOvertimeInPeriod(Integer id, LocalDateTime startDate,
													 LocalDateTime finishDate, int limit, int offset) {
		List<MOvertime> overtimes = overtimeRepository.findByUserIdAndDateBetween(id, startDate, finishDate, limit, offset);
		return overtimes.stream().map(overtime -> new OvertimeDTO(overtime)).collect(Collectors.toList());
	}
	
	@Override
	public int countByUserIdAndPeriodAndStatus(Integer userId, LocalDateTime start, LocalDateTime finish, OvertimeStatus status) {
		return overtimeRepository.countByUserIdAndPeriodAndStatus(userId, start, finish, status);
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
	//期間指定して全ユーザーの残業を検索
	public List<OvertimeDTO> getApprovelOvertimeInPeriod(Integer id, LocalDateTime startDate,
														 LocalDateTime finishDate,int limit, int offset) {
		List<MOvertime> overtimes = overtimeRepository.findByIdAndDateBetween(id, startDate, finishDate, limit, offset);
		return overtimes.stream().map(overtime -> new OvertimeDTO(overtime)).collect(Collectors.toList());
	}
	
	@Override
	public List<OvertimeDTO> getApproverOvertimeInPeriod(Integer approverId, LocalDateTime start, LocalDateTime finish, int limit, int offset) {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public int countApproverOvertimeInPeriodandStatus(Integer approverId, LocalDateTime start, LocalDateTime finish, OvertimeStatus status) {
		// TODO 自動生成されたメソッド・スタブ
		return 0;
	}
	

	//idとユーザーが同じか確認用
	public boolean isRequestOwner(Integer id, Integer userId, Integer status) {
		return overtimeRepository.isRequestOwner(id, userId, status);
	}

	//idの存在確認用
	public boolean existsById(Integer id) {
		return overtimeRepository.isExistsId(id);
	}

}
