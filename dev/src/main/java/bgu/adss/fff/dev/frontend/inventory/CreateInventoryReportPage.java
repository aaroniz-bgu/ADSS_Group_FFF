package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.ReportDto;
import bgu.adss.fff.dev.contracts.RequestReportDto;
import bgu.adss.fff.dev.domain.models.ReportType;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class CreateInventoryReportPage extends AbstractUserComponent {

    private static final String REPORT_ROUTE = URI_PATH + "/report";
    private final RestTemplate restTemplate;

    private final InputComponent categoriesInput;
    private String[] categories;

    protected CreateInventoryReportPage(PrintStream out) {
        super(out);
        restTemplate = new RestTemplate();

        categoriesInput = new InputComponent("Enter categories to include in the report (comma separated): ");
        categoriesInput.subscribe(this::onCategoriesInput);

        page.add(new LogoComponent("Create Inventory Report"));
        page.add(categoriesInput);
    }

    private void onCategoriesInput(StateEvent event) {
        try {
            String categories = event.getData();
            if (categories.isBlank())
                throw new IllegalArgumentException("Categories cannot be empty.");
            this.categories = categories.split(",");
        } catch (Exception e) {
            out.println(e.getMessage());
            categoriesInput.render(out);
        } finally {
            createReport();
        }
    }

    private void createReport() {
        RequestReportDto requestReportDto = new RequestReportDto(ReportType.INVENTORY, categories);
        ReportDto report = restTemplate.postForObject(REPORT_ROUTE, requestReportDto, ReportDto.class);
        if (report != null) {
            out.println("Inventory Report created successfully: ");
            out.println(report.title());
            out.println(report.content());
        }
    }
}
