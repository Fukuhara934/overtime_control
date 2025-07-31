package overtime_control.domain.util;

import java.io.PrintWriter;
import java.util.List;

import overtime_control.dto.OvertimeDTO;

public class CsvExport {
	public static void writeCsv(PrintWriter writer, List<OvertimeDTO> reports) {
        writer.println("残業開始,残業終了,社員名");

        for (OvertimeDTO report : reports) {
            writer.printf("%s,%s,%s %s%n",
                report.getRequest().getStartTime(),
                report.getRequest().getFinishTime(),
                report.getUser().getFamilyName(),
                report.getUser().getFirstName());
        }
    }
}
