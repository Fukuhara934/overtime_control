package overtime_control.domain.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class BreakTimeService {
	public boolean isBreakTime(LocalDateTime startTime, LocalDateTime finishTime, Integer breaktimeMinutes) {
		Duration duration = Duration.between(startTime, finishTime);
        long totalMinutes = duration.toMinutes();

        return totalMinutes >= 480 && (breaktimeMinutes == null || breaktimeMinutes == 0);
    }
}
