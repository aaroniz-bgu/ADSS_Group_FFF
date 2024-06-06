/**
 * This class is owned by Aaron Iziyaev (aaroniz-bgu on GitHub).
 * It was created by @author Aaron Iziyaev.
 */

package bgu.adss.fff.dev.frontend.cli.components;

public class LabelComponent extends AbstractTerminalComponent {
    private String label;

    public LabelComponent(String label) {
        this.label = label.concat(NEWLINE);
    }

    public void setLabel(String label) {
        this.label = label.concat(NEWLINE);
        notifyListeners(new StateEvent(true));
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