package bgu.adss.fff.dev.frontend.cli.uikit;

import java.io.Closeable;
import java.util.Scanner;

public abstract class TerminalApp implements Closeable {
    public static final Scanner SCANNER = new Scanner(System.in);



    public void close() {
        SCANNER.close();
    }
}
