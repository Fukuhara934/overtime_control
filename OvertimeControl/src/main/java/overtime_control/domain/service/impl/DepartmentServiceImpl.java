package overtime_control.domain.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import overtime_control.domain.model.Department;
import overtime_control.domain.service.DepartmentService;
import overtime_control.infrastructure.repository.DepartmentRepository;

@Service
public class DepartmentServiceImpl implements DepartmentService {
	
	@Autowired
	DepartmentRepository departmentRepository;
	
	@Override
	public List<Department> getAllDepartment() {
		return departmentRepository.findAll();
	}
	
	@Override
	public Department findById(Integer id) {
		return departmentRepository.findById(id);
	}
}
