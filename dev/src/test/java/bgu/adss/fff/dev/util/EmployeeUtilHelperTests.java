package bgu.adss.fff.dev.util;

import org.junit.jupiter.api.Test;

import static bgu.adss.fff.dev.util.EmployeeUtilHelper.getBankDetailsHelper;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeUtilHelperTests {
    @Test
    void testGetBankDetailsHelper() {
        assertArrayEquals(new int[]{10, 800, 000000}, getBankDetailsHelper("10:800:000000"));
        assertArrayEquals(new int[]{20, 300, 7654321}, getBankDetailsHelper("20:300:7654321"));
        assertArrayEquals(new int[]{12, 800, 12345678}, getBankDetailsHelper("12:800:12345678"));
        assertArrayEquals(new int[]{999, 999, 999999999}, getBankDetailsHelper("999:999:999999999"));
    }
}
