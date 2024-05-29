/**
 * This class is owned by Aaron Iziyaev (aaroniz-bgu on GitHub).
 * It was created by @author Aaron Iziyaev.
 */

package bgu.adss.fff.dev.frontend.cli.components;

import java.util.List;

public class ListComponent<T> extends AbstractTerminalComponent {
    private List<T> list;

    public ListComponent(List<T> list) {
        if(list == null) {
            throw new NullPointerException("List is null");
        }

        this.list = list;
    }

    public void setList(List<T> list) {
        if(list == null) {
            throw new NullPointerException("List is null");
        }
        this.list = list;
        notifyListeners(new StateEvent(true));
    }

    @Override
    public String rerender() {
        StringBuilder builder = new StringBuilder();
        int index = 1;
        for(T item : list) {
            builder.append(index++);
            builder.append(". ");
            builder.append(item.toString());
            builder.append(NEWLINE);
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return rerender();
    }
}
