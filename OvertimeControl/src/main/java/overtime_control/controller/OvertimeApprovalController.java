package overtime_control.controller;

import java.time.LocalDate;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.model.MOvertime;
import overtime_control.domain.model.MUser;
import overtime_control.domain.model.OvertimeStatus;
import overtime_control.domain.service.OvertimeService;
import overtime_control.domain.service.UserService;
import overtime_control.domain.userInform.UserInform;
import overtime_control.dto.OvertimeApprovalDTO;
import overtime_control.form.OvertimeApprovalForm;

@Controller
@RequestMapping("/approval")
@Slf4j
public class OvertimeApprovalController {
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	OvertimeService overtimeService;
	
	@Autowired
	UserService userService;
	
	
	
	// ======================================
	// ① 残業申請一覧の表示
	// ======================================
	@GetMapping
	public String getApproval(@AuthenticationPrincipal UserInform principal, Model model) {
		//申請一覧取得
		List<OvertimeApprovalDTO> overtimes = overtimeService.getAllApproval();
		model.addAttribute("overtimeApproval", overtimes);

		return "approval/home";
	}
	
	
	// ======================================
	// ② 残業申請承認画面の表示・承認処理
	// ======================================
	
	@GetMapping("/success/{id}")
	public String getSuccess(@AuthenticationPrincipal UserInform principal,
							 @PathVariable Integer id, Model model) {
		OvertimeApprovalDTO overtime = overtimeService.getApprovalById(id);

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
		if (!overtimeService.existsById(id)) {
			return "redirect:/approval?error=notfound";
		}
		MOvertime overtime = new MOvertime();

		overtime.setId(id);
		overtime.setApprover(user);
		overtime.setStatus(OvertimeStatus.APPROVED);
		
		overtimeService.approvalUpdate(overtime);

		return "redirect:/approval";

	}
	
	
	
	// ======================================
	// ③ 残業申請却下画面の表示・却下処理
	// ======================================	
	
	@GetMapping("/reject/{id}")
	public String getReject(@AuthenticationPrincipal UserInform principal,
							@PathVariable Integer id,
							@ModelAttribute OvertimeApprovalForm form, Model model) {
		log.info(form.toString());
		OvertimeApprovalDTO overtime = overtimeService.getApprovalById(id);
		
		
		model.addAttribute("overtime", overtime);
		model.addAttribute("today", LocalDate.now());
		return "approval/reject";
	}
	
	@PostMapping("/reject/{id}")
	public String postReject(@PathVariable Integer id, 
	                         @AuthenticationPrincipal UserInform principal, 
	                         @ModelAttribute @Validated OvertimeApprovalForm form,
	                         BindingResult result, Model model) {
		
		MUser user = userService.getUserByEmail(principal.getUsername());
		if (user == null) {
			return "redirect:/login";
		}
		
		if (result.hasErrors()) {
	        return getReject(principal, id, form, model);
		}
		
		
		MOvertime overtime = modelMapper.map(form, MOvertime.class);
		overtime.setApprover(user);
		overtime.setId(id);
		
		log.info(form.toString());
		overtimeService.approvalUpdate(overtime);
		return "redirect:/approval";		
	}	
}
