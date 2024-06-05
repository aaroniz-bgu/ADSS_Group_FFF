package bgu.adss.fff.dev.frontend;

import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.TerminalApp;
import bgu.adss.fff.dev.frontend.hr.LoginPage;
import org.springframework.web.client.RestTemplate;

public class FrontendApp extends TerminalApp {
    public static final String URI_PATH = "http://localhost:8080";

    public FrontendApp() {
        before();
        AbstractUserComponent page = new LoginPage(System.out);
        while(!Thread.currentThread().isInterrupted()) {
            page.render();
        }
        this.close();
    }

    private void before() {
        RestTemplate template = new RestTemplate();
        template.postForLocation(URI_PATH+"/branch/main", null);
        template.postForLocation(URI_PATH+"/role", new RoleDto("shift-manager", true, false));
        template.postForLocation(URI_PATH+"/role", new RoleDto("hr-manager", false, true));
        template.postForLocation(URI_PATH+"/role", new RoleDto("cashier", false, false));
        template.postForLocation(URI_PATH+"/role", new RoleDto("storekeeper", false, false));
    }
}
