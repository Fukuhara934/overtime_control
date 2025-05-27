package overtime_control.controller;

import java.time.LocalDate;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.model.MOvertime;
import overtime_control.domain.model.MUser;
import overtime_control.domain.model.OvertimeStatus;
import overtime_control.domain.service.OvertimeService;
import overtime_control.domain.service.UserService;
import overtime_control.domain.userInform.UserInform;
import overtime_control.dto.OvertimeApprovalDTO;

@Controller
@Slf4j
public class OvertimeSuccessControl {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserService userService;

	@Autowired
	OvertimeService overtimeService;

	@GetMapping("/success/{id}")
	public String getSuccess(@AuthenticationPrincipal UserInform principal,
							 @PathVariable Integer id, Model model) {
		OvertimeApprovalDTO overtime = overtimeService.getOvertimeApproval(id);

		model.addAttribute("overtime", overtime);
		model.addAttribute("today", LocalDate.now());
		return "approval/success";
	}

	@PostMapping("/success/{id}")
	public String postDecide(@PathVariable Integer id,
							 @AuthenticationPrincipal UserInform principal, Model model) {
		//申請を承認
		MUser user = userService.getUserByEmail(principal.getUsername());
		if (user == null) {
			return "redirect:/login";
		}
		//申請の存在チェック
		if (!overtimeService.isExistsId(id)) {
			return "redirect:/approval?error=notfound";
		}
		MOvertime overtime = new MOvertime();

		overtime.setId(id);
		overtime.setApprover(user);
		overtime.setStatus(OvertimeStatus.APPROVED);
		
		overtimeService.approvalOvertime(overtime);

		return "redirect:/home/approval";

	}
}
