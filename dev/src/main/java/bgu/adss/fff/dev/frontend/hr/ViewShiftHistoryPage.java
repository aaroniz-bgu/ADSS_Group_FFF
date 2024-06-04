package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ShiftDto;
import bgu.adss.fff.dev.frontend.cli.ToStringWrapper;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.ListComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;

import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ViewShiftHistoryPage extends AbstractUserComponent {
    protected ViewShiftHistoryPage(PrintStream out, ShiftDto[] shifts) {
        super(out);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        for(ShiftDto shift : shifts) {
            page.add(new LabelComponent("Shift of: " + shift.date().format(format) + " <> " +
                    (shift.shift() == 0 ? "Morning" : "Evening") + ", Branch: " + shift.branch()));
            List<ToStringWrapper> assigned = new ArrayList<>();
            for(EmployeeDto emp : shift.assignedEmployees()) {
                assigned.add(new ToStringWrapper(() -> "Name " + emp.name() + ", ID: " + emp.id()));
            }
            page.add(new ListComponent<>(assigned));
        }
    }
}
