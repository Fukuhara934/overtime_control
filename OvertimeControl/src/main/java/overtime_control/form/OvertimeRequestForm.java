package overtime_control.form;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import overtime_control.domain.model.WorkPattern;

@Data
public class OvertimeRequestForm {
	
	@NotNull(message = "勤務時間を選択してください")
	private WorkPattern workPattern;
	
	@NotNull(message = "開始予定時間を入力してください")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime requestStartTime;
	
	@NotNull(message = "終了予定時間を入力してください")
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime requestFinishTime;
	
	@NotBlank(message = "残業理由を入力してください")
	private String reason;
}
