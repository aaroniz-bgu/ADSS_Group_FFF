package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.contracts.RequestAmountDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class SellItemsPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/product";
    private final RestTemplate restTemplate;

    private final InputComponent idInput;
    private final InputComponent amountInput;

    private long id;
    private int amount;

    public SellItemsPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        idInput = new InputComponent("Enter product ID (מק''ט): ");
        amountInput = new InputComponent("Enter amount to sell (כמות): ");

        idInput.subscribe(this::onIdInput);
        amountInput.subscribe(this::onAmountInput);

        page.add(new LogoComponent("Get Product"));
        page.add(idInput);
        page.add(amountInput);

    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            idInput.render(out);
        }
    }

    private void onAmountInput(StateEvent event) {
        try {
            this.amount = Integer.parseUnsignedInt(event.getData());
            sellItems();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            amountInput.render(out);
        }
    }

    private void sellItems() {
        ProductDto product = restTemplate.getForObject(ROUTE + "/" + id, ProductDto.class);
        if (product == null) {
            out.println("Product not found");
            return;
        }

        RequestAmountDto amountDto = new RequestAmountDto(this.amount);
        try {
            String response = restTemplate.postForObject(ROUTE + "/sell/" + id, amountDto, String.class);
            out.println(response);
        } catch (Exception e) {
            out.println(e.getMessage());
        }

    }

}
