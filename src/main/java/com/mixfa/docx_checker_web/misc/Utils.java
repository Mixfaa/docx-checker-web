package com.mixfa.docx_checker_web.misc;

import java.math.BigInteger;

public class Utils {
    private Utils() {
    }

    public static BigInteger smToTwips(double sm) {
        return BigInteger.valueOf(Math.round(sm * 567.0));
    }
}
