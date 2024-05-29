/**
 * This class is owned by Aaron Iziyaev (aaroniz-bgu on GitHub).
 * It was created by @author Aaron Iziyaev.
 */

package bgu.adss.fff.dev.frontend.cli.components;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTerminalComponent {
    protected static final String NEWLINE = "\n";
    private final List<ComponentListener> listeners;

    protected AbstractTerminalComponent() {
        this.listeners = new ArrayList<>();
    }

    protected void notifyListeners(StateEvent event) {
        for(ComponentListener listener: listeners) {
            listener.onComponentEvent(event);
        }
    }

    public void subscribe(ComponentListener listener) {
        if(!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void unsubscribe(ComponentListener listener) {
        listeners.remove(listener);
    }

    public void render(PrintStream out) {
        out.print(toString());
    }
    public abstract String rerender();
    @Override
    public abstract String toString();
}
