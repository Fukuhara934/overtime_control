package overtime_control.infrastructure.repository;


import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import overtime_control.domain.model.MOvertime;
import overtime_control.domain.model.OvertimeStatus;


@Mapper
public interface OvertimeRepository {
	public void insertRequest(MOvertime overtime);          // 新規申請
    public void updateApproval(MOvertime overtime);         // 承認処理
    public void updateRequest(MOvertime overtime);			 // 修正処理
    public void updateReport(MOvertime overtime);           // 報告処理 
    public void deleteRequest(Integer id);
    
    //役職あり
    public List<MOvertime> findAllApproval();
    public List<MOvertime> findByDateBetween(LocalDateTime startDate,
    										 LocalDateTime finishDate,
    										 int limit, int offset);
    public List<MOvertime> findAllByDateBetween(LocalDateTime startDate,
    											LocalDateTime finishDate);
    
    public int countApproverOvertimeInPeriodandStatus(LocalDateTime startDate,
    												  LocalDateTime finishDate,
    												  OvertimeStatus status);
    
    //役職なし
    public List<MOvertime> selectRequestByUserId(Integer userId);
    public List<MOvertime> selectReportedByUserId(Integer userId);
    public List<MOvertime> findByUserIdAndDateBetween(Integer userId,
    												  LocalDateTime startDate,
    												  LocalDateTime finishDate,
    												  int limit, int offset);
    
    public int countByUserIdAndPeriodAndStatus(Integer userId, LocalDateTime startDate,
			  								   LocalDateTime finishDate, OvertimeStatus status);
    public MOvertime selectById(Integer id);
    
    //報告作成時確認
    public MOvertime findById(Integer id);   
    public Boolean isRequestOwner(Integer id, Integer userId, Integer status);
    public Boolean isExistsId(Integer id);
}
