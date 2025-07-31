package overtime_control.controller;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.model.MOvertime;
import overtime_control.domain.model.MUser;
import overtime_control.domain.model.OvertimeStatus;
import overtime_control.domain.service.OvertimeService;
import overtime_control.domain.service.UserService;
import overtime_control.domain.userInform.UserInform;
import overtime_control.domain.util.CsvExport;
import overtime_control.dto.OvertimeApprovalDTO;
import overtime_control.dto.OvertimeDTO;
import overtime_control.form.ApprovalDecisionForm;
import overtime_control.form.OvertimeSearchForm;

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
	public String getApproval(@AuthenticationPrincipal UserInform principal,
			@ModelAttribute OvertimeSearchForm form, Model model) {
		//申請一覧取得
		//初期値
		if (form.getStartDate() == null || form.getFinishDate() == null) {
			form.setStartDate(LocalDate.now().withDayOfMonth(1));
			form.setFinishDate(LocalDate.now());
		}

		// ページネーション情報
		//最初のページの定義
		int page = form.getPage() != null ? form.getPage() : 1;
		//ページに件数を定義
		int size = form.getSize() != null ? form.getSize() : 10;
		//データベースから取得する際にsizeで上限を定義しているため
		//LIMITで取得件数を制限してOFFSETで何件目から取得するを決める
		int offset = (page - 1) * size;

		// 検索処理
		//フォームに入力した日付の00:00を.atStartOfDay();で取得
		LocalDateTime startDate = form.getStartDate().atStartOfDay();
		//それに一日追加した日付を.plusDays(1)で取得してSQLで比較　注意として比較なので00:00分は含まれない
		LocalDateTime finishDate = form.getFinishDate().plusDays(1).atStartOfDay();
		OvertimeStatus status = OvertimeStatus.REQUESTED;

		List<OvertimeDTO> results = overtimeService.getApproverOvertimeInPeriod(
				startDate, finishDate, size, offset);

		int total = overtimeService.countApproverOvertimeInPeriodandStatus(
				startDate, finishDate, status);
		
		int totalPages = (int) Math.ceil((double) total / size);
		
		System.out.println("sousuu "+startDate+finishDate+" tatal "+total);
		model.addAttribute("overtimes", results);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageSize", size);

		model.addAttribute("startDate", form.getStartDate());
		model.addAttribute("finishDate", form.getFinishDate());

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
							@ModelAttribute ApprovalDecisionForm form, Model model) {
		log.info(form.toString());
		OvertimeApprovalDTO overtime = overtimeService.getApprovalById(id);
		
		
		model.addAttribute("overtime", overtime);
		model.addAttribute("today", LocalDate.now());
		return "approval/reject";
	}
	
	@PostMapping("/reject/{id}")
	public String postReject(@PathVariable Integer id, 
	                         @AuthenticationPrincipal UserInform principal, 
	                         @ModelAttribute @Validated ApprovalDecisionForm form,
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
	
	// ======================================
	// ③ 残業申請却下画面の表示・却下処理
	// ======================================
	
	@GetMapping("/export")
	public void exportOvertimeCsv(@RequestParam("startDate") LocalDate start,
								  @RequestParam("finishDate") LocalDate finish, 
								  HttpServletResponse response) throws IOException {
		
		LocalDateTime startDate = start.atStartOfDay();
	    LocalDateTime finishDate = finish.plusDays(1).atStartOfDay();
		
	    List<OvertimeDTO> reports = overtimeService.getCSVOvertimeInPeriod(startDate, finishDate);

	    response.setContentType("text/csv; charset=Shift_JIS");
	    response.setHeader("Content-Disposition", "attachment; filename=overtime.csv");
	    try (OutputStreamWriter osw = new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8);
	   		 PrintWriter writer = new PrintWriter(osw)) {

	   		//これが意味わからん
	   		writer.write('\uFEFF');

	   		CsvExport.writeCsv(writer, reports);
	   		writer.flush();
	   		log.info("CSV出力完了: {}", reports);
	   	}
	}
}
