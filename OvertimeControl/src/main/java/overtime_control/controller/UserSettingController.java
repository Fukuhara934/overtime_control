package overtime_control.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import overtime_control.domain.service.DepartmentService;
import overtime_control.domain.service.UserService;
import overtime_control.domain.userInform.UserInform;
import overtime_control.form.EmailChangeForm;
import overtime_control.form.PasswordForm;
import overtime_control.form.UserProfileForm;

@Controller
@Slf4j
@RequestMapping("/setting")
public class UserSettingController {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	UserService userService;
	
	@Autowired
	DepartmentService departmentService;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	

	// ======================================
	//　個人情報の変更
	// ======================================
	@GetMapping
	public String getSetting(@AuthenticationPrincipal UserInform principal,
							 @ModelAttribute UserProfileForm form, Model model) {
		MUser user = userService.getUserById(principal.getId());
		form = modelMapper.map(user, UserProfileForm.class);
		form.setDepartmentId(user.getDepartment().getId());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("departments", departmentService.getAllDepartment());
		model.addAttribute("userProfileForm", form);
		log.info(form.toString());
		return "settings/profile";
	}
	
	@PostMapping
	public String postSetting(@AuthenticationPrincipal UserInform principal,
			@ModelAttribute @Validated UserProfileForm form,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("departments", departmentService.getAllDepartment());
			model.addAttribute("email", principal.getUsername());
			return "settings/profile";
		}

		Department department = departmentService.findById(form.getDepartmentId());

		if (department == null) {
			bindingResult.rejectValue("departmentId", "そのような部署は存在しません");
			model.addAttribute("departments", departmentService.getAllDepartment());
			return "settings/profile";
		}

		MUser user = modelMapper.map(form, MUser.class);
		user.setId(principal.getId());;
		user.setFamilyName(form.getFamilyName());
		user.setFirstName(form.getFirstName());
		user.setDepartment(department);

		log.info(user.toString());

		userService.updateUser(user);
		
		model.addAttribute("email", principal.getUsername());

		return "redirect:/setting";
	}
	

	// ======================================
	// passwordの変更手続き
	// ======================================
	
	@GetMapping("/password")
	public String getChangePassword(@ModelAttribute PasswordForm form, Model model) {
		
		return "settings/password";
	}
	
	@PostMapping("/password")
	public String postPassword(@ModelAttribute @Validated PasswordForm form, BindingResult bindingResult,
							   @AuthenticationPrincipal UserInform principal, Model model) {
		
		MUser user = userService.getUserById(principal.getId());
		if(user == null) {
			bindingResult.reject("user.notFound", "ユーザーが存在しません");
		}
		
		if (!bindingResult.hasFieldErrors("currentPassword") &&
			!passwordEncoder.matches(form.getCurrentPassword(), user.getPassword())) {
	        bindingResult.rejectValue("currentPassword", "invalid", "現在のパスワードが正しくありません");
	    }
		
		if (!bindingResult.hasFieldErrors("newPassword") &&
			form.getCurrentPassword().equals(form.getNewPassword())) {
			bindingResult.rejectValue("newPassword", "invalid", "新しいパスワードが現在と同じです");
		 }
		
	    if (!bindingResult.hasFieldErrors("confirmPassword") &&
	    	!form.getNewPassword().equals(form.getConfirmPassword())) {
	        bindingResult.rejectValue("confirmPassword", "mismatch", "確認用パスワードが一致しません");
	    }
		
		if(bindingResult.hasErrors()) {
			return "settings/password";
		}
		
		user.setPassword(passwordEncoder.encode(form.getNewPassword()));
	    userService.updatePassword(user);
	    SecurityContextHolder.clearContext();

	    return "redirect:/login?passwordReset";
	}
	
	// ======================================
	// Emailの変更手続き
	// ======================================
	
	@GetMapping("/email")
	public String getChangeEmail(@AuthenticationPrincipal UserInform principal,
								 @ModelAttribute EmailChangeForm form, Model model) {
		MUser user = userService.getUserById(principal.getId());
		model.addAttribute("email",user.getEmail());
		return "settings/email";
	}
	
	@PostMapping("/email")
	public String postChangeEmail(@AuthenticationPrincipal UserInform principal,
								  @ModelAttribute @Validated EmailChangeForm form, BindingResult bindingResult,
								  Model model) {
		MUser user = userService.getUserById(principal.getId());
		if(user == null) {
			bindingResult.reject("user.notFound", "ユーザーが存在しません");
		}
		
		if(!bindingResult.hasFieldErrors("newEmail") &&
				user.getEmail().equals(form.getNewEmail())) {
			bindingResult.rejectValue("newEmail", "invalid", "現在のEメールアドレスと同じです");
		}
		
		if(!bindingResult.hasFieldErrors("password") &&
			!passwordEncoder.matches(form.getPassword(), user.getPassword())){
			bindingResult.rejectValue("password", "invalid", "パスワードが間違っています");
			log.info(form.toString());
			log.info(user.toString());
		}
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("email", user.getEmail());
			return "settings/email";
		}
		
		userService.updateEmail(form.getNewEmail(), principal.getId());
		return "redirect:/setting";
		
	}
}
