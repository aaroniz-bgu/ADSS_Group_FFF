package bgu.adss.fff.dev.util;

import static bgu.adss.fff.dev.domain.models.Constants.BANK_DETAIL_FIELDS;

public class EmployeeUtilHelper {
    public static int[] getBankDetailsHelper(String bankDetail) {
        String[] details = bankDetail.split(":");
        int i = 0;
        int[] out = new int[BANK_DETAIL_FIELDS];
        for(String s : details) {
            out[i++] = Integer.parseInt(s);
        }
        return out;
    }
}
