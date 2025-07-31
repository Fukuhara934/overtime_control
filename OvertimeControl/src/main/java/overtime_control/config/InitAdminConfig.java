package overtime_control.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.model.Department;
import overtime_control.domain.model.MUser;
import overtime_control.domain.model.UserRole;
import overtime_control.domain.service.DepartmentService;
import overtime_control.domain.service.UserService;

@Configuration
@Slf4j
public class InitAdminConfig {
	//これに関しての記述が英語でしかないため十分な理解ができていない。
	//kindleが最近無料なそうなのでいい本がないか見繕ってみる
	
	@Bean
	CommandLineRunner createInitialAdmin(UserService userService,
										 PasswordEncoder encoder,
										 DepartmentService departmentService) {
		//余談ですがここのargsはrun(String args){のargsのことです。
		return args -> {
			if(userService.getUserByEmail("admin@example.com") == null) {
				Department department = departmentService.findById(1);
				
				if(department == null) {
					System.out.println("部署が見つかりませんでした。");
					return;
				}
				MUser admin = new MUser();
				admin.setEmail("admin@example.com");
				admin.setPassword(encoder.encode("admin"));
				admin.setFamilyName("初期");
				admin.setFirstName("管理者");
				admin.setRole(UserRole.ADMIN);
				admin.setDepartment(department);
				
				userService.createUser(admin);
				System.out.println("初期管理者を作成しました");
			} else {
				System.out.println("初期管理者はすでに作成されています");
			}
		};
	};
}
