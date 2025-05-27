package overtime_control.aspect;

import org.springframework.web.bind.annotation.ControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GrobalControllerAdvice {
	
	/*@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public String handleNoResourceFound(MethodArgumentTypeMismatchException e, Model model) {
		log.error("MethodArgumentTypeMismatchExceptionが発生しました。", e);
		model.addAttribute("message", "不正なURLが入力されました");
		return "error/error";
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public String handleNoResourceFound(NoResourceFoundException e, Model model) {
		log.error("NoResourceFoundExceptionが発生しました。", e);
		model.addAttribute("message", "ページが見つかりません");
		return "error/error";
	}

	@ExceptionHandler(DataAccessException.class)
	public String dataAccessExceptionHandler(DataAccessException e, Model model) {
		log.error("DataAccessExceptionが発生しました。", e);
		model.addAttribute("message", "データベースエラーが発生しました。");

		return "error/error";
	}

	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {
		log.error("Exceptionが発生しました。", e);
		model.addAttribute("message", "予期しないエラーが発生しました。");

		return "error/error";
	}*/
}
