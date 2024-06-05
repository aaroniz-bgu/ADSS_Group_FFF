package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ShiftDto;
import bgu.adss.fff.dev.frontend.cli.components.TableComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;
import static java.time.temporal.TemporalAdjusters.next;

public class EmployeeAssignedShiftsPage extends AbstractUserComponent {
    private static final String ROUTE = URI_PATH + "/shift/from=";
    private static final List<String> HEADER = List.of(
            "Shift   ", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"
    );
    public EmployeeAssignedShiftsPage(PrintStream out, EmployeeDto emp) {
        super(out);

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate nextSunday = LocalDate.now().with(next(DayOfWeek.SUNDAY));
        LocalDate nextSaturday = nextSunday.plusDays(6);

        ShiftDto[] shifts = new RestTemplate().getForEntity(
                ROUTE+nextSunday.format(format)+"&to="+nextSaturday.format(format)+"&"+emp.branchName(),
                ShiftDto[].class).getBody();

        final List<String> morning = new ArrayList<>();
        final List<String> evening = new ArrayList<>();

        for(ShiftDto shift : shifts) {
            String value = "FREE";
            List<String> li = shift.shift() == 0 ? morning : evening;
            for(EmployeeDto employee : shift.assignedEmployees()) {
                if (employee.id() == emp.id()) {
                    value = "ASSIGNED";
                    break;
                }
            }
            li.add(value);
        }

        morning.add("Morning:");
        evening.add("Evening:");

        page.add(new LogoComponent("Assigned shifts for: "+emp.name()));
        page.add(new TableComponent<>(HEADER, List.of(morning, evening), 150, 12));
    }
}
