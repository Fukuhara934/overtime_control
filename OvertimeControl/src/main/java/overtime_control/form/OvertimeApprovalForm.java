package overtime_control.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import overtime_control.domain.model.OvertimeStatus;

@Data
public class OvertimeApprovalForm {

    private OvertimeStatus status;
    @NotBlank(message = "却下理由は必ず入力してください")
    private String rejectReason;

}

