package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.ReportDto;
import bgu.adss.fff.dev.contracts.RequestReportDto;
import bgu.adss.fff.dev.domain.models.ReportType;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class CreateStockReportPage extends AbstractUserComponent {

    private static final String REPORT_ROUTE = URI_PATH + "/report";
    private final RestTemplate restTemplate;

    public CreateStockReportPage(PrintStream out) {
        super(out);
        restTemplate = new RestTemplate();

        page.add(new LogoComponent("Create Out of Stock Report"));
        page.add(new LabelComponent(createReport()));
    }

    private String createReport() {
        RequestReportDto requestReportDto = new RequestReportDto(ReportType.OUT_OF_STOCK, new String[0]);
        ReportDto report = restTemplate.postForObject(REPORT_ROUTE, requestReportDto, ReportDto.class);
        if (report != null) {
            return report.title() + "\n" + report.content() + "\n";
        }
        return "Fail";
    }
}
