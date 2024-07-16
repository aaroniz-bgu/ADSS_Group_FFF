package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;

public class LogoComponent extends LabelComponent {
    private static final String BAR =
"////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////";
    private static final String LOGO = "SUPER LEE : ";
    public LogoComponent(String pageTitle) {
        super(BAR +
              "\n" + " ".repeat((BAR.length() - LOGO.length() - pageTitle.length()) / 2) + LOGO + pageTitle +
              "\n" + BAR);
    }
}
