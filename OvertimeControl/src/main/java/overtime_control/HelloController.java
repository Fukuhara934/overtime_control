package overtime_control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
	@GetMapping("/hello")
	public String getHello() {
		return "hello";
	}
}
