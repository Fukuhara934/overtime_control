package overtime_control.form;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OvertimeReportForm {
	@NotNull(message = "実残業開始時間を入力してください")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime reportStartTime;
	
	@NotNull(message = "実残業終了時間を入力してください")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime reportFinishTime;
	
	@Min(value = 0, message = "休憩時間は0分以上で入力してください")
	private Integer breaktime;
	
	@NotBlank(message = "報告を記入してください")
	private String report;
}
