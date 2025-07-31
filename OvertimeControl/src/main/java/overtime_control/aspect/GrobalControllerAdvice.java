
package overtime_control.aspect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import lombok.extern.slf4j.Slf4j;
import overtime_control.domain.model.MUser;
import overtime_control.domain.service.UserService;
import overtime_control.domain.userInform.UserInform;
import overtime_control.domain.userInform.UserInformDTO;

@ControllerAdvice
@Slf4j
public class GrobalControllerAdvice {
	
	@Autowired
	UserService userService;
	
	@ModelAttribute("loginUser")
	public UserInformDTO setLoginUser(@AuthenticationPrincipal UserInform principal) {
        if (principal == null) {
            return null;
        }
        MUser user = userService.getUserById(principal.getId());
        return new UserInformDTO(
        		user.getFamilyName(),
        		user.getFirstName(),
        		user.getDepartment().getDepartmentName(),
        		user.getRole()
        );
    }
	
	
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
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
	}
}
