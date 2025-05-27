package overtime_control.controller;

import java.time.LocalDate;

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
import overtime_control.domain.model.WorkPattern;
import overtime_control.domain.service.OvertimeService;
import overtime_control.domain.service.UserService;
import overtime_control.domain.userInform.UserInform;
import overtime_control.dto.OvertimeRequestDTO;
import overtime_control.form.OvertimeRequestForm;
import overtime_control.infrastructure.repository.OvertimeRepository;

@Controller
@RequestMapping("/request")
@Slf4j
public class OvertimeRequestController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserService userService;

	@Autowired
	OvertimeService overtimeService;
	
	@Autowired
	OvertimeRepository overtimeRepository;

	@GetMapping
	public String getRequestOvertime(@AuthenticationPrincipal UserInform principal,
									 @ModelAttribute OvertimeRequestForm form, Model model) {
		model.addAttribute("workPatternList", WorkPattern.values());
		model.addAttribute("today", LocalDate.now());
		
		return "overtime/request";
	}

	@PostMapping
	public String createRequestOvertime(@AuthenticationPrincipal UserInform principal,
										@ModelAttribute @Validated OvertimeRequestForm form, 
										BindingResult bindingResult, Model model) {
		log.info(form.toString());
		//ユーザーの確認
		MUser user = userService.getUserByEmail(principal.getUsername());
		if (user == null) {
			return "redirect:/login";
		}
		//開始時間と終了時間の確認
		if (form.getRequestStartTime() != null && form.getRequestFinishTime() != null) {
			if (form.getRequestStartTime().isAfter(form.getRequestFinishTime())) {
				bindingResult.rejectValue("requestFinishTime", null, "予定終了時間は予定開始時間より後にしてください");
			}
		}
		
		if (form.getWorkPattern() == WorkPattern.NONE) {
			bindingResult.rejectValue("workPattern", null, "勤務時間を選択してください");
		}
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("workPatternList", WorkPattern.values());
			model.addAttribute("today", LocalDate.now());
			return "overtime/request";
		}

		MOvertime overtime = modelMapper.map(form, MOvertime.class);
		overtime.setUser(user);
		overtime.setDepartment(user.getDepartment());
		overtimeService.requestOvertime(overtime);

		return "redirect:/home";
	}
	
	
	@GetMapping("/update/{id}")
	public String showUpdateRequestForm(@AuthenticationPrincipal UserInform principal,
	                                    @PathVariable Integer id,
	                                    Model model) {

	    OvertimeRequestDTO overtime = overtimeService.requestById(id);
	    if (overtime == null) {
	        return "redirect:/approval?error=notfound";
	    }

	    MUser user = userService.getUserByEmail(principal.getUsername());
	    if (user == null ) {
	        return "redirect:/home";
	    }

	    OvertimeRequestForm form = modelMapper.map(overtime, OvertimeRequestForm.class);
	    form.setWorkPattern(overtime.getRequest().getPattern());
	    form.setReason(overtime.getRequest().getReason());

	    
	    model.addAttribute("overtimeRequestForm", form);
	    model.addAttribute("workPatternList", WorkPattern.values());
	    model.addAttribute("today", LocalDate.now());
	    model.addAttribute("overtimeId", id);

	    if (overtime.getApproval() != null) {
	        model.addAttribute("rejectReason", overtime.getApproval().getRejectReason());
	    }

	    return "overtime/update";
	}


	
	
	@PostMapping("/update/{id}")
	public String updateRequestOvertime(@AuthenticationPrincipal UserInform principal,
										@PathVariable Integer id,
										@ModelAttribute @Validated OvertimeRequestForm form,
										BindingResult bindingResult, Model model) {
		

		MUser user = userService.getUserByEmail(principal.getUsername());
		if (user == null) return "redirect:/login";
		
		// バリデーション
		if (form.getRequestStartTime() != null && form.getRequestFinishTime() != null &&
			form.getRequestStartTime().isAfter(form.getRequestFinishTime())) {
			bindingResult.rejectValue("requestFinishTime", null, "予定終了時間は予定開始時間より後にしてください");
		}

		if (form.getWorkPattern() == WorkPattern.NONE) {
			bindingResult.rejectValue("workPattern", null, "勤務時間を選択してください");
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("workPatternList", WorkPattern.values());
			model.addAttribute("today", LocalDate.now());
			model.addAttribute("overtimeId", id);
			return "overtime/update";
		}
		

		log.info("id{}", id);
		log.info("修正申請内容: {}", form);
		
		MOvertime overtime = modelMapper.map(form, MOvertime.class);
		overtime.setId(id);
		overtime.setStatus(OvertimeStatus.REQUESTED);
		

		overtimeService.requestUpdate(overtime);

		return "redirect:/home";

	}
}

