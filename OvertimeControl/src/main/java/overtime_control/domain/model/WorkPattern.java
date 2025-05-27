package overtime_control.domain.model;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public enum WorkPattern {
	NONE(0, "-- 選択してください --", null, null),
	EARLY_A(1, "早出A:", LocalTime.of(5, 30), LocalTime.of(14, 15)),
	EARLY_B(2, "早出B:", LocalTime.of(6, 00), LocalTime.of(14, 45)),
	EARLY_C(3, "早出C:", LocalTime.of(6, 30), LocalTime.of(15, 15)),
	EARLY_D(4, "早出D:", LocalTime.of(7, 00), LocalTime.of(15, 45)),
	EARLY_E(5, "早出E:", LocalTime.of(7, 30), LocalTime.of(16, 15)),
	EARLY_F(6, "早出F:", LocalTime.of(8, 00), LocalTime.of(16, 45)),
	NORMAL_A(7, "通常A:", LocalTime.of(8, 30), LocalTime.of(17, 15)),
	NORMAL_B(8, "通常B:", LocalTime.of(9, 00), LocalTime.of(17, 45)),
	NORMAL_C(9, "通常C:", LocalTime.of(9, 30), LocalTime.of(18, 15)),
	LATE_A(10, "遅出A:", LocalTime.of(10, 00), LocalTime.of(18, 45)),
	LATE_B(11, "遅出B:", LocalTime.of(10, 30), LocalTime.of(19, 15)),
	LATE_C(12, "遅出C:", LocalTime.of(11, 00), LocalTime.of(19, 45)),
	LATE_D(13, "遅出D:", LocalTime.of(11, 30), LocalTime.of(20, 15)),
	LATE_E(14, "遅出E:", LocalTime.of(12, 00), LocalTime.of(20, 45)),
	LATE_F(15, "遅出F:", LocalTime.of(12, 30), LocalTime.of(21, 15)),
	LATE_G(16, "遅出G:", LocalTime.of(13, 00), LocalTime.of(21, 45));

	private final int id;
	private final String patternName;
	private final LocalTime startTime;
	private final LocalTime finishTime;
	

	WorkPattern(int id, String patternName, LocalTime startTime, LocalTime finishTime) {
		this.id = id;
		this.patternName = patternName;
		this.startTime = startTime;
		this.finishTime = finishTime;
	}

	public int getId() {
		return id;
	}

	public String getPatternName() {
		return patternName;
	}
	
	public LocalTime getStartTime() {
		return startTime;
	}
	
	public LocalTime getFinishTime() {
		return finishTime;
	}
	
	public static WorkPattern fromId(int id) {
		return Arrays.stream(values())
				.filter(wp -> wp.id == id)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Invalid WorkPattern ID: " + id));
	}

	public static List<WorkPattern> getAll() {
		return Arrays.asList(values());
	}
	
	public String getDisplayName() {
	    if (startTime == null || finishTime == null) {
	        return patternName; 
	    }
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
	    return String.format("%s %s～%s", patternName, startTime.format(formatter), finishTime.format(formatter));
	}
}
