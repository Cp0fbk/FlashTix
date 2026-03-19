package com.flashtix.common.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class BookingCodeGenerator {

    private static final String PREFIX = "FT";
    private static final String CHARACTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"; // Removed ambiguous chars: 0,O,1,I
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a unique booking code in format: FT-XXXXXX
     * Example: FT-ABC123
     */
    public String generate() {
        StringBuilder code = new StringBuilder(PREFIX);
        code.append("-");

        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }

    /**
     * Generates a unique ticket code in format: TK-XXXXXXXXXX
     * Example: TK-A1B2C3D4E5
     */
    public String generateTicketCode() {
        StringBuilder code = new StringBuilder("TK");
        code.append("-");

        for (int i = 0; i < 10; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }

        return code.toString();
    }
}
