package com.ixigo.service;

import java.security.SecureRandom;

public class PNRGenerator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int PNR_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePNR() {
        StringBuilder pnr = new StringBuilder(PNR_LENGTH);

        for (int i = 0; i < PNR_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            pnr.append(CHARACTERS.charAt(index));
        }

        return pnr.toString();
    }
    
}
