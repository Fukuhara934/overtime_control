package overtime_control.controller;

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

import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.model.MOvertime;
import overtime_control.domain.model.MUser;
import overtime_control.domain.model.OvertimeStatus;
import overtime_control.domain.model.WorkPattern;
import overtime_control.domain.service.OvertimeService;
import overtime_control.domain.service.UserService;
import overtime_control.domain.userInform.UserInform;
import overtime_control.dto.OvertimeDTO;
import overtime_control.dto.OvertimeRequestDTO;
import overtime_control.form.OvertimeReportForm;
import overtime_control.form.OvertimeRequestForm;
import overtime_control.form.OvertimeSearchForm;
import overtime_control.infrastructure.repository.OvertimeRepository;

@Controller
@Slf4j
@RequestMapping("/overtime")
public class OvertimeController {
	
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	UserService userService;

	@Autowired
	OvertimeService overtimeService;
	
	@Autowired
	OvertimeRepository overtimeRepository;

	
	
	// ======================================
	// ① 残業申請：新規申請画面の表示・登録
	// ======================================
	
	@GetMapping("/request")
	public String getRequestOvertime(@AuthenticationPrincipal UserInform principal,
									 @ModelAttribute OvertimeRequestForm form, Model model) {
		
		model.addAttribute("workPatternList", WorkPattern.values());
		model.addAttribute("today", LocalDate.now());
		
		return "overtime/request";
	}

	@PostMapping("/request")
	public String createRequestOvertime(@AuthenticationPrincipal UserInform principal,
										@ModelAttribute @Validated OvertimeRequestForm form, 
										BindingResult bindingResult, Model model) {
		
		//ユーザーの確認
		MUser user = userService.getUserByEmail(principal.getUsername());
		if (user == null) {
			return "redirect:/login";
		}

		// 勤務パターンの確認
		if (form.getWorkPattern() == null || form.getWorkPattern() == WorkPattern.NONE) {
		    bindingResult.rejectValue("workPattern", "invalid.workPattern", "勤務時間を選択してください");
		}

		// 開始時間の確認
		LocalDateTime requestStart = form.getRequestStartTime();
		WorkPattern pattern = form.getWorkPattern();
		if (requestStart != null && pattern != null) {
		    LocalDateTime patternStartDateTime = requestStart.toLocalDate().atTime(pattern.getFinishTime());
		    if (requestStart.isBefore(patternStartDateTime)) {
		        bindingResult.rejectValue("requestStartTime", "invalid.start", "予定開始時間は勤務開始時間以降にしてください");
		    }
		}

		// 終了時間の確認
		LocalDateTime requestFinish = form.getRequestFinishTime();
		if (requestStart != null && requestFinish != null && requestStart.isAfter(requestFinish)) {
		    bindingResult.rejectValue("requestFinishTime", "invalid.finish", "予定終了時間は予定開始時間より後にしてください");
		}
		
		if (bindingResult.hasErrors()) {
			model.addAttribute("workPatternList", WorkPattern.values());
			model.addAttribute("today", LocalDate.now());
			return "overtime/request";
		}
		
		log.info(form.toString());

		MOvertime overtime = modelMapper.map(form, MOvertime.class);
		overtime.setUser(user);
		overtime.setDepartment(user.getDepartment());
		overtimeService.requestOvertime(overtime);

		return "redirect:/home";
	}

	
	
	// ======================================
	// ② 残業申請：更新（修正）画面の表示・処理
	// ======================================
	
	@GetMapping("request/update/{id}")
	public String showUpdateRequestForm(@AuthenticationPrincipal UserInform principal,
										@PathVariable Integer id,
										Model model) {

		OvertimeRequestDTO overtime = overtimeService.getRequestById(id);
		if (overtime == null) {
			return "redirect:/home?error=notfound";
		}

		MUser user = userService.getUserByEmail(principal.getUsername());
		if (user == null) {
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

	@PostMapping("request/update/{id}")
	public String updateRequestOvertime(@AuthenticationPrincipal UserInform principal,
			@PathVariable Integer id,
			@ModelAttribute @Validated OvertimeRequestForm form,
			BindingResult bindingResult, Model model) {

		MUser user = userService.getUserByEmail(principal.getUsername());
		if (user == null)
			return "redirect:/login";

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
	
	
	
	// ======================================
	// ③ 報告：報告画面の表示・登録処理
	// ======================================
	
	@GetMapping("report/{id}")
	public String getReport(@AuthenticationPrincipal UserInform principal,
			@PathVariable Integer id, Model model) {

		OvertimeRequestDTO overtime = overtimeService.getRequestById(id);
		if (overtime == null) {
			return "redirect:/home";
		}

		MUser user = userService.getUserByEmail(principal.getUsername());
		if (user == null) {
			return "redirect:/home";
		}
		if (!model.containsAttribute("overtimeReportForm")) {
			OvertimeReportForm form = new OvertimeReportForm();
			form.setReportStartTime(overtime.getRequest().getStartTime());
			form.setReportFinishTime(overtime.getRequest().getFinishTime());
			model.addAttribute("overtimeReportForm", form);
		}

		model.addAttribute("today", LocalDate.now());
		model.addAttribute("overtimeId", id);
		model.addAttribute("overtime", overtime);
		return "overtime/report";
	}

	@PostMapping("report/{id}")
	public String createReportOvertime(@AuthenticationPrincipal UserInform principal,
			@PathVariable Integer id,
			@ModelAttribute @Validated OvertimeReportForm form,
			BindingResult bindingResult, Model model) {

		// 自分の申請かチェック
		if (!overtimeService.isRequestOwner(id, principal.getId(), 2)) {
			bindingResult.reject("request.notfound", "この申請はあなたのものではありません");
		}

		OvertimeRequestDTO request = overtimeService.getRequestById(id);

		WorkPattern pattern = request.getRequest().getPattern();
		LocalDateTime reportStart = form.getReportStartTime();
		LocalDateTime reportFinish = form.getReportFinishTime();

		if (reportStart != null && reportFinish != null) {
			LocalDate requestDate = request.getRequest().getStartTime().toLocalDate();
			LocalDateTime patternStartDateTime = requestDate.atTime(pattern.getFinishTime());

			// 勤務開始時間のチェック
			if (reportStart.isBefore(patternStartDateTime)) {
				bindingResult.rejectValue("reportStartTime", "invalid.start", "実開始時間は勤務終了時間以降にしてください");
			}

			// 終了時間のチェック
			if (reportStart.isAfter(reportFinish)) {
				bindingResult.rejectValue("reportFinishTime", "invalid.finish", "実終了時間は実績開始時間より後にしてください");
			}

			// 休憩時間の妥当性チェック（8時間以上は一時間以上必要
			long minutes = java.time.Duration.between(reportStart, reportFinish).toMinutes();
			Integer breaktime = form.getBreaktime();

			if (minutes >= 480) {
				if (breaktime == null || breaktime < 60) {
					bindingResult.rejectValue("breaktime", "invalid.breaktime.required", "8時間以上の残業には休憩時間が必要です");
				}
			}
		}

		if (bindingResult.hasErrors()) {
			model.addAttribute("overtimeId", id);
			model.addAttribute("today", LocalDate.now());
			model.addAttribute("overtime", request);
			return "overtime/report";
		}

		if (form.getBreaktime() == null) {
			form.setBreaktime(0);
		}

		MOvertime report = modelMapper.map(form, MOvertime.class);
		report.setId(id);
		overtimeService.reportUpdate(report);

		return "redirect:/home";
	}

	
	
	// ======================================
	// ④ 報告一覧及び検索機能
	// ======================================
	
	@GetMapping("/reported")
	public String showReport(@AuthenticationPrincipal UserInform principal,
			@ModelAttribute @Validated OvertimeSearchForm form,
			BindingResult result, Model model) {
		//初期値
		if (form.getStartDate() == null || form.getFinishDate() == null) {
			form.setStartDate(LocalDate.now().withDayOfMonth(1));
			form.setFinishDate(LocalDate.now());
		}

		// ページネーション情報
		//最初のページの定義
		int page = form.getPage() != null ? form.getPage() : 1;
		//サイズの定義
		int size = form.getSize() != null ? form.getSize() : 10;
		int offset = (page - 1) * size;

		// 検索処理
		LocalDateTime startDate = form.getStartDate().atStartOfDay();
		LocalDateTime finishDate = form.getFinishDate().plusDays(1).atStartOfDay();
		OvertimeStatus status = OvertimeStatus.REPORTED;

		List<OvertimeDTO> results = overtimeService.getUserOvertimeInPeriod(
				principal.getId(), startDate, finishDate, size, offset);

		int total = overtimeService.countByUserIdAndPeriodAndStatus(

				principal.getId(), startDate, finishDate, status);
		int totalPages = (int) Math.ceil((double) total / size);

		model.addAttribute("overtimes", results);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("pageSize", size);

		model.addAttribute("startDate", form.getStartDate());
		model.addAttribute("finishDate", form.getFinishDate());

		return "overtime/reported";
	}
	
	
	
	// ======================================
	// ⑤ 残業申請の削除処理（フラグ処理にするか検討中）
	// ======================================
	
	@PostMapping("/delete/{id}")
	public String postDelete(@AuthenticationPrincipal UserInform principal, @PathVariable Integer id, Model model) {
		if(!overtimeService.isRequestOwner(id, principal.getId(), 1)) {
			return "error/error";
		}
		
		overtimeService.requestDelete(id);
		return "redirect:/home";
	}
}
