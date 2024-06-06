/**
 * This class is owned by Aaron Iziyaev (aaroniz-bgu on GitHub).
 * It was created by @author Aaron Iziyaev.
 */

package bgu.adss.fff.dev.frontend.cli.components;

import java.util.List;

public class TableComponent<T, E> extends AbstractTerminalComponent {
    private static final String NO_SPACE = "...";
    private static final String HORIZONTAL_DIV = "\u2500";
    private static final String VERTICAL_DIV = "\u2502";

    private List<T> headers;
    private List<List<E>> rows;
    private int maxCellLength;
    private int minCellLength;
    private int[] colsSize;
    private String lastRender;

    public TableComponent(List<T> headers,List<List<E>> rows, int maxCellLength, int minCellLength) {
        checkArgs(headers, rows);
        this.headers = headers;
        this.rows = rows;
        this.maxCellLength = Math.max(NO_SPACE.length(), maxCellLength);
        this.minCellLength = Math.max(NO_SPACE.length(), minCellLength);
        this.colsSize = null;
        this.lastRender = null;
    }

    public TableComponent(List<T> headers, List<List<E>> rows) {
        this(headers, rows, Integer.MAX_VALUE, 12);
    }

    private void checkArgs(List<T> headers, List<List<E>> rows) {
        if(headers == null || rows == null) {
            throw new NullPointerException();
        }
        int rowIndex = 0;
        for(List<E> row : rows) {
            if(row == null) {
                throw new NullPointerException();
            }
            if(row.size() != headers.size()) {
                throw new IllegalArgumentException("Row "+rowIndex+" size mismatched to headers size");
            }
            for(E item : row) {
                if(item == null) {
                    throw new NullPointerException();
                }
            }
            rowIndex++;
        }
    }

    private String getContentSized(String content, int columnSize) {
        columnSize = Math.min(columnSize, maxCellLength);
        if(content.length() > columnSize) {
            return content.substring(0, columnSize - NO_SPACE.length()).concat(NO_SPACE);
        } else {
            return content.concat(" ".repeat(columnSize - content.length()));
        }
    }

    private String renderHeaders() {
        this.colsSize = new int[headers.size()];

        StringBuilder hig = new StringBuilder();
        StringBuilder mid = new StringBuilder();
        StringBuilder low = new StringBuilder();

        hig.append('\u250c');
        mid.append(VERTICAL_DIV);
        low.append('\u251c');

        int i = 0;
        for(T header : headers) {
            String content = getContentSized(header.toString(), Math.max(header.toString().length(), minCellLength));
            int size = content.length();
            colsSize[i] = size;
            hig.append(HORIZONTAL_DIV.repeat(size).concat("\u252c"));
            mid.append(content.concat(VERTICAL_DIV));
            low.append(HORIZONTAL_DIV.repeat(size).concat("\u253c"));
            i++;
        }

        if(i > 0) {
            hig.deleteCharAt(hig.length() - 1);
            mid.deleteCharAt(mid.length() - 1);
            low.deleteCharAt(low.length() - 1);
        }

        hig.append('\u2510');
        mid.append(VERTICAL_DIV);
        low.append('\u2524');

        return hig.toString() + NEWLINE + mid.toString() + NEWLINE + low.toString();
    }

    private String renderRows() {
        StringBuilder builder = new StringBuilder();
        for(List<E> row : rows) {
            builder.append(VERTICAL_DIV);
            int i = 0;
            for(E item : row) {
                builder.append(getContentSized(item.toString(), colsSize[i]));
                builder.append(VERTICAL_DIV);
                i++;
            }
            builder.append(NEWLINE);
        }
        builder.append('\u2514');
        for(int  col : colsSize) {
            builder.append(HORIZONTAL_DIV.repeat(col));
            builder.append('\u2534');
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append('\u2518');
        return builder.toString();
    }

    @Override
    public String rerender() {
        lastRender = renderHeaders() + NEWLINE + renderRows() + NEWLINE;
        return lastRender;
    }

    @Override
    public String toString() {
        if(lastRender == null) {
            rerender();
        }
        return lastRender;
    }
}