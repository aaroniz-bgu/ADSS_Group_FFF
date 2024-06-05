/**
 * This class is owned by Aaron Iziyaev (aaroniz-bgu on GitHub).
 * It was created by @author Aaron Iziyaev.
 */

package bgu.adss.fff.dev.frontend.cli.components;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.cli.uikit.TerminalApp.SCANNER;

public class InputComponent extends AbstractTerminalComponent {

    private String label;

    public InputComponent(String label) {
        if(label == null) {
            throw new NullPointerException();
        }

        this.label = label;
    }

    @Override
    public void render(PrintStream out) {
        out.print(label + NEWLINE + "> ");
        String input = SCANNER.nextLine();
        notifyListeners(new StateEvent(input));
    }

    public void setLabel(String label) {
        if(label == null) {
            throw new NullPointerException();
        }
        this.label = label;
    }

    @Override
    public String rerender() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
