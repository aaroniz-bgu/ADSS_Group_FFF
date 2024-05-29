/**
 * This class is owned by Aaron Iziyaev (aaroniz-bgu on GitHub).
 * It was created by @author Aaron Iziyaev.
 */

package bgu.adss.fff.dev.frontend.cli.uikit;

import bgu.adss.fff.dev.frontend.cli.components.AbstractTerminalComponent;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractUserComponent {
    protected List<AbstractTerminalComponent> page;
    protected PrintStream out;

    protected AbstractUserComponent(PrintStream out) {
        this.page = new LinkedList<>();
        this.out = out;
    }

    public void render() {
        for(AbstractTerminalComponent component : page) {
            component.render(out);
        }
    }
}
