package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.ItemDto;
import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class SetDefectivePage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/product";
    private final RestTemplate restTemplate;

    private final InputComponent idInput;

    private long id;

    public SetDefectivePage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        idInput = new InputComponent("Enter product ID (מק''ט): ");

        idInput.subscribe(this::onIdInput);

        page.add(new LogoComponent("Change price of a Product"));
        page.add(idInput);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
            setItemDefective();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            idInput.render(out);
        }
    }

    private void setItemDefective() {

        ItemDto response = restTemplate.getForObject(ROUTE + "/" + id, ItemDto.class);
        if(response == null) {
            out.println("Product with ID " + id + " not found.");
            return;
        }
        ItemDto defectiveItem = new ItemDto(this.id, response.expirationDate(), true);
        restTemplate.put(ROUTE, defectiveItem);
        out.println("Item with ID " + id + " was set as defective.");
    }

}
