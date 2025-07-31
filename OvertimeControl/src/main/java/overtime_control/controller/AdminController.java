package overtime_control.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.model.OvertimeStatus;
import overtime_control.domain.service.OvertimeService;

@Controller
@Slf4j
@RequestMapping("/admin")
public class AdminController {
	
	@Autowired
	OvertimeService overtimeService;
	
	@GetMapping
	public String getAdminHome(Model model) {
		LocalDateTime start = LocalDate.now().withDayOfMonth(1).atStartOfDay();
		LocalDateTime finish = LocalDateTime.now();
		OvertimeStatus status = OvertimeStatus.REQUESTED;
		
		Integer pendingCount = overtimeService.countApproverOvertimeInPeriodandStatus(start, finish, status);
		
		status = OvertimeStatus.REPORTED;
		Integer monthlyCount =overtimeService.countApproverOvertimeInPeriodandStatus(start, finish, status);
		
		//csv情報をここにいれたらいい
		model.addAttribute("pendingCount", pendingCount);
		model.addAttribute("monthlyCount", monthlyCount);
		//名前がhomeというのはわかりにくいのではないか？
		return "admin/home";
	}
}
