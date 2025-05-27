package overtime_control.domain.model;

import java.util.Arrays;

public enum OvertimeStatus {
	REQUESTED(1, "申請中"),
	APPROVED(2, "承認"),
	REJECTED(3, "却下"),
	REPORTED(4, "報告完了"),
	COMPLETE(5,"完了");
	
	private final int id;
	private final String status;
	
	OvertimeStatus(int id, String status){
		this.id = id;
		this.status = status;
	}
	
	public int getId() {
		return id;
	}
	
	public String getStatus() {
		return status;
	}
	
	public static OvertimeStatus fromId(int id) {
		return Arrays.stream(values())
				.filter(os -> os.id == id)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Invalid OvertimeStatus value: " + id));
	}
}
