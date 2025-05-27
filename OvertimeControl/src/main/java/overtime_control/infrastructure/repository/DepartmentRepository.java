package overtime_control.infrastructure.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import overtime_control.domain.model.Department;

@Mapper
public interface DepartmentRepository {
	
	public List<Department> findAll();
	
	public Department findById(Integer id);
}
