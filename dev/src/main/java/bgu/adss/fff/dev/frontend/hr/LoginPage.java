package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.FullEmployeeDto;
import bgu.adss.fff.dev.contracts.ReportShiftRequest;
import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import bgu.adss.fff.dev.frontend.inventory.InventoryMenuPage;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class LoginPage extends AbstractUserComponent {

    private static final String ROUTE = "/employee";
    private static final String DEBUG_MANAGER = "SYSADMIN-000";
    private static final String LOAD_TEST = "LOAD-TEST";
    private static final String DELIVERY_REG = "make-delivery";

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
        } else if(ans.equals(DELIVERY_REG)) {
            new CreateDeliveryPage(out).render();
        }

        String uri =  URI_PATH + ROUTE + "/" + ans;
        try {
            EmployeeDto response = restTemplate.getForEntity(uri, EmployeeDto.class).getBody();

            List<AbstractUserComponent> menus = new LinkedList<>();
            List<String> menuTitles = new LinkedList<>();

            boolean hrFound = false, smFound = false;

            for(RoleDto r : response.roles()) {
                if(r.isHrManager() && !hrFound) {
                    hrFound = true;
                    menus.add(new HrManagerMenuPage(out, response));
                    menuTitles.add("HR Management Menu");
                } else if (r.isShiftManager() && !smFound) {
                    smFound = true;
                    menus.add(new InventoryMenuPage(out, response));
                    menuTitles.add("Inventory Management Options & Menu");
                }
            }

            AbstractUserComponent nextPage = hrFound || smFound ?
                    new MenuChooseMenu(out, response, menuTitles, menus) :
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
                    LocalDate.now().plusDays(7),
                    0,
                    1
            ));
            template.postForLocation(URI_PATH+"/shift", new ReportShiftRequest(
                    LocalDate.now().plusDays(7),
                    0,
                    2
            ));
            template.postForLocation(URI_PATH+"/shift", new ReportShiftRequest(
                    LocalDate.now().plusDays(7),
                    0,
                    3
            ));
            template.postForLocation(URI_PATH+"/shift", new ReportShiftRequest(
                    LocalDate.now().plusDays(8),
                    0,
                    1
            ));
            template.postForLocation(URI_PATH+"/shift", new ReportShiftRequest(
                    LocalDate.now().plusDays(8),
                    0,
                    2
            ));
            template.postForLocation(URI_PATH+"/category/init-starter", null);
        } catch (RestClientResponseException e) {
            out.println(e.getResponseBodyAs(ErrorDetails.class).message());
        }
    }
}
