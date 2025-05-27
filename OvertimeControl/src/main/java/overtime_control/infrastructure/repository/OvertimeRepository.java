package overtime_control.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import overtime_control.domain.model.MOvertime;

@Mapper
public interface OvertimeRepository {
	void insertRequest(MOvertime overtime);          // 新規申請
    void updateApproval(MOvertime overtime);         // 承認処理
    void updateRequest(MOvertime overtime);			 // 修正処理
    void updateReport(MOvertime overtime);           // 報告処理 
    
    //役職あり
    public List<MOvertime> findAllApproval();
    
    //役職なし
    public List<MOvertime> selectByUserIdRequest(Integer userId);
    
    public List<MOvertime> selectByUserIdReported(Integer userId);
    
    public MOvertime selectById(Integer id);
    
    //報告作成時確認
    public MOvertime findById(Integer id);
    
    public Boolean isRequestOwner(Integer id, Integer userId);
    
    public Boolean isExistsId(Integer id);
}
