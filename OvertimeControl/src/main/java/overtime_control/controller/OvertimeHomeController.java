package overtime_control.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.service.OvertimeService;
import overtime_control.domain.userInform.UserInform;
import overtime_control.dto.OvertimeRequestDTO;

@Controller
@Slf4j
@RequestMapping("/home")
public class OvertimeHomeController {

	@Autowired
	OvertimeService overtimeService;
	
	//残業用
	@GetMapping
	public String home(@AuthenticationPrincipal UserInform principal, Model model) {
		// 残業申請のリストを取得
		List<OvertimeRequestDTO> overtimeRequests = overtimeService.getAllRequestByUserId(principal.getId());
		log.info("取得した残業申請一覧: {}", overtimeRequests); 
		
		// 承認待ちの申請を取得
		model.addAttribute("overtimeRequests", overtimeRequests);

		return "overtime/home";
	}
}
