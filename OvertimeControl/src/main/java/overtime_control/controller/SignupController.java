package overtime_control.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.model.Department;
import overtime_control.domain.model.MUser;
import overtime_control.domain.model.UserRole;
import overtime_control.domain.service.DepartmentService;
import overtime_control.domain.service.PasswordService;
import overtime_control.domain.service.UserService;
import overtime_control.form.SignupForm;

@Controller
@RequestMapping("/signup")
@Slf4j
public class SignupController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	UserService userService;
	
	@Autowired
	PasswordService passwordService;

	@GetMapping
	public String getSignup(@ModelAttribute SignupForm form, Model model) {
		//部署はデータベースに役職はEnumにしてるのでそれぞれから取り出す
		model.addAttribute("departments", departmentService.getAllDepartment());
		//変換コードをJavaConfigに書いているので自動で変換されるはず
		model.addAttribute("roles", UserRole.values());
		return "signup/signup";
	}

	@PostMapping
	public String postSingup(@ModelAttribute @Validated SignupForm form, BindingResult bindingResult, Model model) {
		log.info(form.toString());
		
		if (bindingResult.hasErrors()) {
			return getSignup(form, model);
		}
		
		Department department = departmentService.findById(form.getDepartmentId());
		if (department == null) {
			bindingResult.rejectValue("departmentId", "そのような部署は存在しません");
			return getSignup(form, model);
		}
		
		MUser user = modelMapper.map(form, MUser.class);
		//passwordのハッシュ化処理
		user.setPassword(passwordService.hashPassword(form.getPassword()));
		user.setDepartment(department);

		userService.createUser(user);

		return "redirect:/login/login";
	}
}
