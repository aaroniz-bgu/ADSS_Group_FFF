package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class InventoryMenuPage extends AbstractUserComponent {

    private final InputComponent chooseMenuOption;
    private final EmployeeDto employee;
    private final RestTemplate restTemplate;

    public InventoryMenuPage(PrintStream out, EmployeeDto employee) {
        super(out);
        restTemplate = new RestTemplate();
        this.employee = employee;

        page.add(new LogoComponent("Inventory Menu"));
        page.add(new LabelComponent("1. Product Menu"));
        page.add(new LabelComponent("2. Category Menu"));
        page.add(new LabelComponent("3. Report Menu"));
        page.add(new LabelComponent("4. Exit"));

        chooseMenuOption = new InputComponent("Choose an option: ");
        chooseMenuOption.subscribe(this::onChooseMenuOption);
        page.add(chooseMenuOption);
    }

    private void onChooseMenuOption(StateEvent event) {
        boolean rerender = true;
        try {
            init();
            int menuOption = Integer.parseInt(event.getData());
            switch (menuOption) {
                case 1:
                    new ProductMenuPage(out, employee).render();
                    break;
                case 2:
                    new CategoryMenuPage(out).render();
                    break;
                case 3:
                    new ReportMenuPage(out, employee).render();
                    break;
                case 4:
                    rerender = false;
                    break;
                default:
                    throw new NumberFormatException("Invalid option");
            }
            if(rerender) this.render();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            chooseMenuOption.render(out);
        }
    }

    private void init(){
        // Order all items initially
        restTemplate.postForLocation(URI_PATH + "/product/order", null);

        // If it is a monday or a thursday, generate and show the report
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.THURSDAY) {
            new CreateStockReportPage(System.out, employee).render();
        }

        // If it is a sunday morning, order new items.
        if (LocalDate.now().getDayOfWeek() == DayOfWeek.SUNDAY && LocalDateTime.now().getHour() == 10) {
            String result = restTemplate.postForObject(URI_PATH + "/product/order", null, String.class);
            System.out.println(result);
        }
    }

}
