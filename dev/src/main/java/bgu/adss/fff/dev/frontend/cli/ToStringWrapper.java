package bgu.adss.fff.dev.frontend.cli;

/**
 * Easier to create to string for different objects for different purposes.
 */
public class ToStringWrapper {

    private final Wrap w;

    public ToStringWrapper(Wrap w) {
        this.w = w;
    }

    public String toString() {
        return w.generate();
    }

    @FunctionalInterface
    public static interface Wrap {
        String generate();
    }
}