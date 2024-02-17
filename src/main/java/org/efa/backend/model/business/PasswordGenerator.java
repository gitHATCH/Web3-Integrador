package org.efa.backend.model.business;

import java.security.SecureRandom;

public class PasswordGenerator {

    public static long generateFiveDigitPassword(){
        SecureRandom random = new SecureRandom();
        final int max = 100000;
        final int min = 9999;
        return random.nextInt(max - min) + min;
    }
}
