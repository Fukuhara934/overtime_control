package overtime_control.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
import overtime_control.domain.service.BreakTimeService;
import overtime_control.domain.service.OvertimeService;
import overtime_control.domain.service.UserService;
import overtime_control.domain.userInform.UserInform;
import overtime_control.dto.OvertimeDTO;
import overtime_control.dto.OvertimeRequestDTO;
import overtime_control.form.OvertimeReportForm;

@Controller
@Slf4j
@RequestMapping("/report")
public class OvertimeReportController {

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	MessageSource messageSource;

	@Autowired
	OvertimeService overtimeService;

	@Autowired
	UserService userService;

	@Autowired
	BreakTimeService breaktimeService;
	
	@GetMapping
	public String getAllRepoted(@AuthenticationPrincipal UserInform principal, Model model) {
		List<OvertimeDTO> overtimes = overtimeService.getAllByUserId(principal.getId());
		model.addAttribute("overtimes", overtimes);
		return "overtime/reported";
	}
	

	@GetMapping("/{id}")
	public String getReport(@AuthenticationPrincipal UserInform principal, @PathVariable Integer id,
			@ModelAttribute OvertimeReportForm form, Model model) {
		OvertimeRequestDTO overtime = overtimeService.getRequestById(id);
		if (overtime == null) {
			String message = messageSource.getMessage("request.id", null, Locale.getDefault());
			model.addAttribute("message", message);
			return "error/error";
		}
		log.info(form.toString());
		model.addAttribute("overtime", overtime);
		model.addAttribute("today", LocalDate.now());
		return "overtime/report";
	}

	@PostMapping("/{id}")
	public String createReportOvertime(@AuthenticationPrincipal UserInform principal, @PathVariable Integer id,
			@ModelAttribute @Validated OvertimeReportForm form, BindingResult bindingResult, Model model) {
		//申請が現在ログインしているuserのものかチェック
		if (!overtimeService.isRequestOwner(id, principal.getId())) {
			bindingResult.rejectValue("request", "request.notfound", "この申請はあなたのものではありません");
			return getReport(principal, id, form, model);
		}
		
		if(form.getReportStartTime() != null && form.getReportFinishTime() != null) {
			if (form.getReportStartTime().isAfter(form.getReportFinishTime())) {
		    bindingResult.rejectValue("reportFinishTime", "invalid.range", "予定終了時間は予定開始時間より後にしてください");
			}
		}
		
		if (form.getReportStartTime() != null && form.getReportFinishTime() != null) {
		    if (breaktimeService.isBreakTime(form.getReportStartTime(), form.getReportFinishTime(), form.getBreaktime())) {
		        bindingResult.rejectValue("breaktime", "breaktime", "8時間以上の残業には休憩時間が必要です");
		    }
		}
		
		if (bindingResult.hasErrors()) {
			return getReport(principal, id, form, model);
		}
		
		MOvertime overtime = modelMapper.map(form, MOvertime.class);
		overtime.setId(id);
		log.info(form.toString());
		overtimeService.reportUpdate(overtime);

		return "redirect:/home";

	}
}
