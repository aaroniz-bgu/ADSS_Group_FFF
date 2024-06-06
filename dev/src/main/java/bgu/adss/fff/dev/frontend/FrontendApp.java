package bgu.adss.fff.dev.frontend;

import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.TerminalApp;
import bgu.adss.fff.dev.frontend.inventory.InventoryMenuPage;

public class FrontendApp extends TerminalApp {
    public static final String URI_PATH = "http://localhost:8080";

    public FrontendApp() {
        before();
        AbstractUserComponent page = new InventoryMenuPage(System.out);
        while(!Thread.currentThread().isInterrupted()) {
            page.render();
        }
        this.close();
    }

    private void before() {

    }
}