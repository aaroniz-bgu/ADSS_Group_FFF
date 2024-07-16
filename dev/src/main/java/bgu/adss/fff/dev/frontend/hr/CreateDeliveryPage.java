package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.MakeDeliveryRequest;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class CreateDeliveryPage extends AbstractUserComponent {

    private static final String PATH = URI_PATH + "/delivery";

    private final RestTemplate template;

    private final InputComponent dateInput;
    private final InputComponent truckInput;

    private String source;
    private LocalDateTime start;
    private long truckNumber;
    private String license;
    private List<String> destinations;

    public CreateDeliveryPage(PrintStream out) {
        super(out);

        template = new RestTemplate();

        InputComponent sourceInput = new InputComponent("Insert the name of the source branch:");
        dateInput = new InputComponent("Insert the date and start time using the following pattern: dd-MM-yyyy HH:mm");
        truckInput = new InputComponent("Insert the truck number (must contain only numbers):");
        InputComponent licenseInput = new InputComponent("Insert the license type:");
        InputComponent destinationsInput = new InputComponent("Insert the destinations separated by comma (,):");

        sourceInput.subscribe(e -> source = e.getData());
        dateInput.subscribe(this::onDateInput);
        truckInput.subscribe(this::onTruckInput);
        licenseInput.subscribe(e -> license = e.getData());
        destinationsInput.subscribe(e -> {
            destinations = Arrays.asList(e.getData().trim().split(","));
            sendRequest();
        });

        page.add(new LogoComponent("Register a new Delivery"));
        page.add(sourceInput);
        page.add(dateInput);
        page.add(truckInput);
        page.add(licenseInput);
        page.add(destinationsInput);
    }

    private void onDateInput(StateEvent e) {
        try {
            DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            start = LocalDateTime.parse(e.getData(), format);
        } catch (DateTimeParseException ex) {
            dateInput.render(out);
        }
    }

    private void onTruckInput(StateEvent e) {
        try {
            truckNumber = Long.parseLong(e.getData());
        } catch(NumberFormatException ex) {
            truckInput.render(out);
        }
    }

    private void sendRequest() {
        try {
            template.postForLocation(PATH, new MakeDeliveryRequest(
                    source, start, truckNumber, license, destinations.toArray(new String[0])
            ));
        } catch (RestClientResponseException e) {
            out.println(e.getResponseBodyAs(ErrorDetails.class).message());
        } catch (Exception e) {
            out.println("Fatal error contact IT team.");
        }
    }
}
