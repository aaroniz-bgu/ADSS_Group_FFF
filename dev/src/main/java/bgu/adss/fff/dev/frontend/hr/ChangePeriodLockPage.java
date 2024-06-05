package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class ChangePeriodLockPage extends AbstractUserComponent {
    public ChangePeriodLockPage(PrintStream out) {
        super(out);
        InputComponent input = new InputComponent("In days, please insert how long before should shifts be locked?");
        input.subscribe((e) -> {
            try {
                int cutoff = Integer.parseInt(e.getData());
                new RestTemplate().put(URI_PATH+"/shift/cutoff="+cutoff, null);
            } catch (NumberFormatException ex) {
                out.println("Please write numbers only!");
                input.render(out);
            }
        });
        page.add(new LogoComponent("Change Automatic Lock Time Period"));
        page.add(input);
    }
}
