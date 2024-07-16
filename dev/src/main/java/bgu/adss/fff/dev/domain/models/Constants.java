package bgu.adss.fff.dev.domain.models;

public class Constants {
    /**
     * Used to check if monthly salary / hourly rate are unset.
     */
    public static final float NOT_SET = -1;
    /**
     * The number of fields a bank account has.
     */
    public static final int BANK_DETAIL_FIELDS = 3;
    /**
     * Bank details parameters index:
     */
    public static final int BANK_ID_IND = 0;
    public static final int BANK_BRANCH_IND = 1;
    public static final int ACCOUNT_ID_IND = 2;

    /**
     * Default days to cut off period.
     */
    public final static String FALLBACK_CUTOFF = "1";
    /**
     * Cut off config name.
     */
    public final static String CUTOFF_CONFIG_KEY = "CUT_OFF";

    /**
     * The time when a morning shift becomes evening shift.
     */
    public final static int SHIFT_CHANGE_HOUR = 15;
}
