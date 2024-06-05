package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.*;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDate;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;
import static java.time.temporal.TemporalAdjusters.next;

public class LoginPage extends AbstractUserComponent {

    private static final String ROUTE = "/employee";
    private static final String DEBUG_MANAGER = "SYSADMIN-000";
    private static final String LOAD_TEST = "LOAD-TEST";

    private final RestTemplate restTemplate;

    public LoginPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        out.println();
        page.add(new LogoComponent("LOGIN"));
        InputComponent input = new InputComponent("Insert your ID: ");
        input.subscribe(this::onLogin);
        page.add(input);
    }

    private void onLogin(StateEvent event) {
        String ans = event.getData();
        if(ans.equals(DEBUG_MANAGER)) {
            new HrManagerMenuPage(out,
                    new EmployeeDto(0L,"ADMIN", null, null, null))
                    .render();
            return;
        } else if(ans.equals(LOAD_TEST)) {
            loadTest();
            return;
        }
        String uri =  URI_PATH + ROUTE + "/" + ans;
        try {
            EmployeeDto response = restTemplate.getForEntity(uri, EmployeeDto.class).getBody();
            AbstractUserComponent nextPage = isHr(response) ?
                    new HrMenuChooseMenu(out, response) :
                    new EmployeeMenuPage(out, response);
            nextPage.render();
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
            this.render();
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }

    private boolean isHr(EmployeeDto emp) {
        for(RoleDto r : emp.roles()) {
            if(r.isHrManager()) return true;
        }
        return false;
    }

    private void loadTest() {
        try {
            RestTemplate template = new RestTemplate();

            template.postForLocation(URI_PATH+"/employee",new FullEmployeeDto(
                    1,
                    "Avi Ron",
                    new RoleDto[]{new RoleDto("shift-manager", false, false)},
                    "10:100:100100",
                    "main",
                    LocalDate.now(),
                    0,
                    6000.0f,
                    38.0f,
                    12,
                    null,
                    null
            ));
            template.postForLocation(URI_PATH+"/employee",new FullEmployeeDto(
                    2,
                    "Eli Copter",
                    new RoleDto[]{new RoleDto("cashier", false, false)},
                    "11:110:1100110",
                    "main",
                    LocalDate.now(),
                    1,
                    3000.0f,
                    34.0f,
                    10,
                    new EmployeeDto(1, "",  new RoleDto[0], null, null),
                    null
            ));
            template.postForLocation(URI_PATH+"/employee",new FullEmployeeDto(
                    3,
                    "Benny Moss",
                    new RoleDto[]{new RoleDto("hr-manager", false, false)},
                    "12:800:420420",
                    "main",
                    LocalDate.now(),
                    0,
                    8000.0f,
                    40.0f,
                    15,
                    null,
                    null
            ));
            template.postForLocation(URI_PATH+"/shift", new ReportShiftRequest(
                    LocalDate.now().with(next(DayOfWeek.SUNDAY)),
                    0,
                    1
            ));
            template.postForLocation(URI_PATH+"/shift", new ReportShiftRequest(
                    LocalDate.now().with(next(DayOfWeek.SUNDAY)),
                    0,
                    2
            ));
            template.postForLocation(URI_PATH+"/shift", new ReportShiftRequest(
                    LocalDate.now().with(next(DayOfWeek.SUNDAY)),
                    0,
                    3
            ));
            template.postForLocation(URI_PATH+"/shift", new ReportShiftRequest(
                    LocalDate.now().with(next(DayOfWeek.MONDAY)),
                    0,
                    1
            ));
            template.postForLocation(URI_PATH+"/shift", new ReportShiftRequest(
                    LocalDate.now().with(next(DayOfWeek.MONDAY)),
                    0,
                    2
            ));
        } catch (RestClientResponseException e) {
            out.println(e.getResponseBodyAs(ErrorDetails.class).message());
        }
    }
}
