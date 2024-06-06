/**
 * This class is owned by Aaron Iziyaev (aaroniz-bgu on GitHub).
 * It was created by @author Aaron Iziyaev.
 */

package bgu.adss.fff.dev.frontend.cli.components;

public class StateEvent {
    private final boolean contentChanged;
    private final String data;

    public StateEvent() {
        this(false, null);
    }

    public StateEvent(String data) {
        this(false, data);
    }

    public StateEvent(boolean contentChanged) {
        this(contentChanged, null);
    }

    public StateEvent(boolean contentChanged, String data) {
        this.contentChanged = contentChanged;
        this.data = data;
    }

    public boolean isContentChanged() {
        return contentChanged;
    }

    public String getData() {
        return data;
    }

}