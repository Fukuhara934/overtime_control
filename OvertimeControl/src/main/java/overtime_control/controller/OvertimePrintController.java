package overtime_control.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.service.OvertimeService;
import overtime_control.dto.OvertimeDTO;

@Controller
@Slf4j
@RequestMapping("print")
public class OvertimePrintController {
	
	@Autowired
	OvertimeService overtimeService;
	
	
	@GetMapping
	public String getPrint(Model model) {
		OvertimeDTO overtimes = new OvertimeDTO();
		model.addAttribute("overtime",overtimes);
		return "overtime/overtime-print";
	}
	
	@GetMapping("/{id}")
	public String getPrint(@PathVariable Integer id, Model model) {
		OvertimeDTO overtime = overtimeService.getOvertimeById(id);
		model.addAttribute("overtime",overtime);
		return "overtime/overtime-print";
	}
}
