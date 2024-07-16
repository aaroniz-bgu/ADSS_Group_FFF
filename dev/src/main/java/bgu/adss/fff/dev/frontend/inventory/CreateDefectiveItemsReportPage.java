package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ReportDto;
import bgu.adss.fff.dev.contracts.RequestReportDto;
import bgu.adss.fff.dev.domain.models.ReportType;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class CreateDefectiveItemsReportPage extends AbstractUserComponent {

    private static final String REPORT_ROUTE = URI_PATH + "/report";
    private final RestTemplate restTemplate;
    private final EmployeeDto employee;

    protected CreateDefectiveItemsReportPage(PrintStream out, EmployeeDto employee) {
        super(out);
        restTemplate = new RestTemplate();

        this.employee = employee;

        page.add(new LogoComponent("Create Defective Items Report"));
        page.add(new LabelComponent(createReport()));
    }

    private String createReport() {

        try {
            RequestReportDto requestReportDto = new RequestReportDto(
                    ReportType.DEFECTIVE_ITEMS, new String[0], employee.branchName());
            ReportDto report = restTemplate.postForObject(REPORT_ROUTE, requestReportDto, ReportDto.class);
            if (report != null) {
                return report.title() + "\n" + report.content() + "\n";
            }
            return "Fail";
        } catch (Exception e) { out.println(e.getMessage()); return "Fail"; }
    }
}
