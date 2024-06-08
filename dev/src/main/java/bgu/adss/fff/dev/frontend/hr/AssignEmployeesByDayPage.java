package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.contracts.ShiftDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.components.TableComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class AssignEmployeesByDayPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/shift/assign";

    private List<String> HEADER = List.of("Employee Name", "Employee ID", "Morning/Evening");

    private final RestTemplate template;

    private final InputComponent daypartInput;
    private final InputComponent employeeInput;

    private ShiftDto shiftA;
    private ShiftDto shiftB;

    private ShiftDto request;

    public AssignEmployeesByDayPage(PrintStream out, ShiftDto shiftA, ShiftDto shiftB) {
        super(out);

        this.shiftA = shiftA;
        this.shiftB = shiftB;

        List<List<String>> rows = new LinkedList<>();
        listHelper(rows, shiftA);
        listHelper(rows, shiftB);

        template = new RestTemplate();

        daypartInput = new InputComponent("Morning/Evening shift? [0/1]:");
        employeeInput = new InputComponent("Insert the employee ids separated by comma(,):");

        daypartInput.subscribe(this::onDayPartInput);
        employeeInput.subscribe(this::onEmployeeInput);

        page.add(new LogoComponent("Assign Employees To Shift"));
        page.add(new TableComponent<>(HEADER, rows, 150, 20));
        page.add(new LabelComponent("[NOTE]: Employees with [M] symbol next to their names are shift managers."));
        page.add(daypartInput);
        page.add(employeeInput);
    }

    private void onDayPartInput(StateEvent event) {
        try {
            int part = Integer.parseInt(event.getData());
            request = part == 0 ? shiftA : part == 1 ? shiftB : null;
            if(request == null) throw new RuntimeException();
        } catch (RuntimeException e) {
            out.println("Please respond only with the available options!");
            daypartInput.render(out);
        }
    }

    private void onEmployeeInput(StateEvent event) {
        if(event.getData().isBlank()) return;
        String[] ids = event.getData().replace(" ","").split(",");
        try {
            if (ids.length == 0) return;
            EmployeeDto[] assigned = new EmployeeDto[ids.length];
            for(int i = 0; i < assigned.length; i++) {
                assigned[i] = inefficientGetEmployeeById(
                        request.availableEmployees(), Long.parseUnsignedLong(ids[i])
                );
                if(assigned[i] == null) throw new IllegalArgumentException();
            }
            request = new ShiftDto(request.date(), request.shift(), request.branch(), request.isLocked(),
                    request.availableEmployees(), assigned, request.requiredRoles());
            sendRequest();
        } catch (IllegalArgumentException e) {
            out.println("Please make sure your input is correctly separated by comma and contains only ids that are available to work that shift!");
            employeeInput.render(out);
        }
    }

    private void sendRequest() {
        try {
            template.put(ROUTE, request);
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }

    private EmployeeDto inefficientGetEmployeeById(EmployeeDto[] arr, long id) {
        for(EmployeeDto emp : arr) {
            if(emp.id() == id) return emp;
        }
        return null;
    }

    private void listHelper(List<List<String>> rows, ShiftDto shift) {
        for(EmployeeDto emp : shift.availableEmployees()) {
            String shiftMgr = isShiftMgr(emp) ? " [M]" : "";
            rows.add(List.of(emp.name()+shiftMgr, emp.id()+"", shift.shift() == 0 ? "M" : "E"));
        }
    }

    private boolean isShiftMgr(EmployeeDto emp) {
        for(RoleDto role : emp.roles()) {
            if(role.isShiftManager()) return true;
        }
        return false;
    }
}
