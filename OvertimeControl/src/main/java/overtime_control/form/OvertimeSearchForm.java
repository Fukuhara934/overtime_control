package overtime_control.form;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import overtime_control.domain.model.OvertimeStatus;

@Data
public class OvertimeSearchForm {
	@NotNull
	private LocalDate startDate;
	
	@NotNull
	private LocalDate finishDate;
	
	private OvertimeStatus status;
	
	private Integer page = 1;
	private Integer size = 10;


}
