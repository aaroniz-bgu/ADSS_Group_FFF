/**
 * This class is owned by Aaron Iziyaev (aaroniz-bgu on GitHub).
 * It was created by @author Aaron Iziyaev.
 */

package bgu.adss.fff.dev.frontend.cli.components;

import java.io.PrintStream;
import java.util.Scanner;

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
        String input;
        try (Scanner scn = new Scanner(System.in)) {
            out.print(label + NEWLINE + "> ");
            input = scn.next();
            scn.nextLine();
        }
        if(input != null) notifyListeners(new StateEvent(input));
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
