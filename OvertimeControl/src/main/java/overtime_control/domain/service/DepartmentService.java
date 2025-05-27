package overtime_control.domain.service;

import java.util.List;

import overtime_control.domain.model.Department;

public interface DepartmentService {
	public List<Department> getAllDepartment();
	
	public Department findById(Integer id);
}
