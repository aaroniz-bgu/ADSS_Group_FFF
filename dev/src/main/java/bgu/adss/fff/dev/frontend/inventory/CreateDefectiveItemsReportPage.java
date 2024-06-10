package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.ReportDto;
import bgu.adss.fff.dev.contracts.RequestReportDto;
import bgu.adss.fff.dev.domain.models.ReportType;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class CreateDefectiveItemsReportPage extends AbstractUserComponent {

    private static final String REPORT_ROUTE = URI_PATH + "/report";
    private final RestTemplate restTemplate;

    protected CreateDefectiveItemsReportPage(PrintStream out) {
        super(out);
        restTemplate = new RestTemplate();

        page.add(new LogoComponent("Create Out of Stock Report"));
        createReport();
    }

    private void createReport() {
        RequestReportDto requestReportDto = new RequestReportDto(ReportType.DEFECTIVE_ITEMS, new String[0]);
        ReportDto report = restTemplate.postForObject(REPORT_ROUTE, requestReportDto, ReportDto.class);
        if (report != null) {
            out.println("Out of Stock Report created successfully: ");
            out.println(report.title());
            out.println(report.content());
        }
    }
}
