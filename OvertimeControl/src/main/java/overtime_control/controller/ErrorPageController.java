package overtime_control.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ErrorPageController {
	
	@GetMapping("/error/error")
	public String accessDnied(Model model) {
		model.addAttribute("message","ページが見つかりません");
		return "error/error";
	}
}
