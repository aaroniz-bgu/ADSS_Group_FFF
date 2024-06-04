package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.EmployeeTermsDto;
import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class EditEmployeeTermsPage extends AbstractUserComponent {
    private static final String EMPLOYEE_ROUTE = URI_PATH + "/employee/";
    private static final String TERMS_ROUTE = EMPLOYEE_ROUTE + "terms/";

    private final RestTemplate template;

    private final InputComponent startInput;
    private final InputComponent jobTypeInput;
    private final InputComponent monthlySalaryInput;
    private final InputComponent hourlyRateInput;
    private final InputComponent daysOffInput;
    private final InputComponent managerInput;
    private final InputComponent endDateInput;

    private long id;
    private LocalDate startDate;
    private int jobType;
    private float monthlySalary;
    private float hourlyRate;
    private int daysOff;
    private EmployeeDto directManager;
    private LocalDate endDate;

    public EditEmployeeTermsPage(PrintStream out, long id) {
        super(out);

        this.template = new RestTemplate();
        try {
            EmployeeTermsDto terms = template.getForEntity(TERMS_ROUTE+id, EmployeeTermsDto.class).getBody();
            this.startDate = terms.startDate();
            this.jobType = terms.jobType();
            this.monthlySalary = terms.monthlySalary();
            this.hourlyRate = terms.hourlyRate();
            this.daysOff = terms.daysOff();
            this.directManager = terms.directManager();
            this.endDate = terms.endDate();
        } catch (Exception e) {
            //log?
        }

        this.id = id;

        this.startInput = new InputComponent("Insert start date or leave empty:");
        this.jobTypeInput = new InputComponent("Insert job type:\n1. Full time\n2. Part time\n3. Contract\n" +
                "Do not insert any additional characters, only numbers from 1 to 3!");
        this.monthlySalaryInput = new InputComponent("Insert the monthly salary, you can leave this empty:");
        this.hourlyRateInput = new InputComponent("Insert the hourly rate of the employee\n" +
                "This field cannot be left empty if monthly salary was left empty");
        this.daysOffInput = new InputComponent("Insert the number of yearly days off for the employee " +
                "(must be greater than 0!)");
        this.managerInput = new InputComponent("Insert the id of the manager or leave blank.");
        this.endDateInput = new InputComponent("Insert the end of employment date if you wish to fire the employee." +
                "\nYou could always change it back to [0] and re-employ the employee");

        startInput.subscribe(this::onStartInput);
        jobTypeInput.subscribe(this::onJobTypeInput);
        monthlySalaryInput.subscribe(this::onMonthlySalInput);
        hourlyRateInput.subscribe(this::onHourlyRateInput);
        daysOffInput.subscribe(this::onDaysOffInput);
        managerInput.subscribe(this::onMangerInput);
        endDateInput.subscribe(this::onEndDateInput);

        page.add(new LogoComponent("Edit Employee Terms"));
        page.add(new LabelComponent("Leave empty [ENTER] if you don't want to change the field!"));
        page.add(startInput);
        page.add(jobTypeInput);
        page.add(monthlySalaryInput);
        page.add(hourlyRateInput);
        page.add(daysOffInput);
        page.add(managerInput);
        page.add(endDateInput);
    }

    private void onStartInput(StateEvent event) {
        String input = event.getData();
        if (input.isBlank()) {
            return;
        }
        String regex = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
        if (!input.matches(regex)) {
            out.println("Please make sure your date is formatted properly [dd-MM-yyyy]");
            startInput.render(out);
        } else {
            startDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
    }

    private void onJobTypeInput(StateEvent event) {
        try {
            if(!event.getData().isBlank()) jobType = Integer.parseInt(event.getData()) - 1;
        } catch (NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            jobTypeInput.render(out);
        }
    }

    private void onMonthlySalInput(StateEvent event) {
        try {
            if(!event.getData().isBlank()) monthlySalary = Float.parseFloat(event.getData());
        } catch (NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            monthlySalaryInput.render(out);
        }
    }

    private void onHourlyRateInput(StateEvent event) {
        try {
            if(!event.getData().isBlank()) hourlyRate = Float.parseFloat(event.getData());
        } catch (NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            hourlyRateInput.render(out);
        }
    }

    private void onDaysOffInput(StateEvent event) {
        try {
            if(!event.getData().isBlank()) daysOff = Integer.parseInt(event.getData());
        } catch (NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            daysOffInput.render(out);
        }
    }

    private void onMangerInput(StateEvent event) {
        try {
            if(!event.getData().isBlank()) {
                directManager = new EmployeeDto(Long.parseUnsignedLong(event.getData()), "", null, "", "");
            }
        } catch (NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            managerInput.render(out);
        }
    }

    private void onEndDateInput(StateEvent event) {
        try {
            if(!event.getData().isBlank()) return;
            String input = event.getData();
            if(input.equals("0")) {
                endDate = null;
            } else {
                String regex = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
                if (!input.matches(regex)) {
                    out.println("Please make sure your date is formatted properly [dd-MM-yyyy]");
                    startInput.render(out);
                } else {
                    endDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
                }
            }
            sendRequest();
        } catch (NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            jobTypeInput.render(out);
        }
    }

    private void sendRequest() {
        try {
            template.put(TERMS_ROUTE+id, new EmployeeTermsDto(
                    id, startDate, jobType, monthlySalary, hourlyRate, daysOff, directManager, endDate
            ));
            EmployeeTermsDto terms = template.getForEntity(TERMS_ROUTE+id, EmployeeTermsDto.class).getBody();
            out.println("Updated terms for: "+id);
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }
}
