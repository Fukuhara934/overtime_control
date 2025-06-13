package overtime_control.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import overtime_control.form.UserProfileForm;

@Controller
@Slf4j
@RequestMapping("/setting")
public class UserSettingController {
	
	@GetMapping
	public String getSetting(@ModelAttribute UserProfileForm form, Model model) {
		form.setFirstName("tarou");
		form.setFamilyName("zanngyou");
		form.setEmail("email");
		form.setDepartment("eigyou");
		
		return "settings/profile";
	}
}
