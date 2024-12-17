package co.istad.reporting.utils;

import java.security.SecureRandom;

public class TokenGenerationUtil {

    // For cryptographically secure numbers (OTP, security tokens, etc.)
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * Generates a secure 6-digit number using SecureRandom
     * Best for security-sensitive operations like OTP
     * @return String containing exactly 6 digits
     */
    public static String generateSecureDigits() {
        return String.format("%06d", secureRandom.nextInt(1000000));
    }

    /**
     * Validates if a string contains exactly 6 digits
     * @param digits string to validate
     * @return true if string contains exactly 6 digits
     */
    public static boolean validateSixDigits(String digits) {
        return digits != null && digits.matches("^\\d{6}$");
    }


}
