/**
 * This class is owned by Aaron Iziyaev (aaroniz-bgu on GitHub).
 * It was created by @author Aaron Iziyaev.
 */

package bgu.adss.fff.dev.frontend.cli.components;

public class CellComponent<T> extends AbstractTerminalComponent {
    private static final String NO_SPACE = "...";

    private T content;
    private int length;

    public CellComponent(T content, int length) {
        if(content == null) {
            throw new NullPointerException();
        }
        if(length <= 0) {
            throw new IllegalArgumentException("length must be positive!");
        }

        this.content = content;
        this.length = Math.max(length, NO_SPACE.length());
    }

    public void setContent(T content) {
        if(content == null) {
            throw new NullPointerException();
        }
        this.content = content;
        notifyListeners(new StateEvent(true));
    }

    public T getContent() {
        return content;
    }

    public void setLength(int length) {
        if(length <= 0) {
            throw new IllegalArgumentException("length must be positive!");
        }
        this.length = Math.max(length, NO_SPACE.length());
        notifyListeners(new StateEvent(true));
    }

    public int getLength() {
        return length;
    }

    @Override
    public String rerender() {
        String contentToPrint = content.toString();
        if(contentToPrint.length() > length) {
            contentToPrint = contentToPrint.toString()
                    .substring(0, length - NO_SPACE.length())
                    .concat(NO_SPACE);
        } else {
            contentToPrint = contentToPrint.concat(" ".repeat(length - contentToPrint.length()));
        }

        StringBuilder builder = new StringBuilder();
        builder.append('\u250c').append("\u2500".repeat(length)).append('\u2510').append(NEWLINE);
        builder.append('\u2502').append(contentToPrint).append('\u2502').append(NEWLINE);
        builder.append('\u2514').append("\u2500".repeat(length)).append('\u2518').append(NEWLINE);
        return builder.toString();
    }

    @Override
    public String toString() {
        return rerender();
    }
}
