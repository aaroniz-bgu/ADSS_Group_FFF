package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.ShiftDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.components.TableComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import bgu.adss.fff.dev.frontend.hr.AssignEmployeesByDayPage;
import bgu.adss.fff.dev.frontend.hr.LoginPage;
import bgu.adss.fff.dev.frontend.hr.LogoComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;
import static java.time.temporal.TemporalAdjusters.next;

public class ShiftAssignmentBranchPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/shift";
    private static final List<String> HEADER = List.of(
            "", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday");

    private final RestTemplate template;
    private final InputComponent input;
    private final String branch;
    private ShiftDto[] shifts;

    public ShiftAssignmentBranchPage(PrintStream out, String branch) throws RestClientResponseException {
        super(out);
        this.template = new RestTemplate();
        this.branch = branch;

        LocalDate nextSunday = LocalDate.now().with(next(DayOfWeek.SUNDAY));
        LocalDate nextSaturday = nextSunday.plusDays(6);
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        String query = ROUTE + "/from=" + nextSunday.format(format) +
                "&to=" + nextSaturday.format(format) + "&" + branch;
        shifts = template.getForEntity(query, ShiftDto[].class).getBody();

        List<String> morning = new ArrayList<>();
        List<String> evening = new ArrayList<>();

        morning.add("Available morning");
        evening.add("Available evening");

        for(ShiftDto shift : shifts) {
            List<String> li = shift.shift() == 0 ? morning : evening;
            li.add(shift.availableEmployees().length + "");
        }

        input = new InputComponent("Pick a day which you want to assign employees to: [1-7]");
        input.subscribe(this::onInput);

        page.add(new LogoComponent("Assign Employees To Shift"));
        page.add(new TableComponent<>(HEADER, List.of(morning, evening), 150, 24));
        page.add(input);

    }

    private void onInput(StateEvent event) {
        try {
            // oof don't get me started the submission is in few hours...
            int day = Integer.parseInt(event.getData()) - 1;
            new AssignEmployeesByDayPage(out, shifts[day * 2], shifts[day * 2 + 1]).render();
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            out.println("Only insert numbers as requested!");
            input.render(out);
        }
    }
}
