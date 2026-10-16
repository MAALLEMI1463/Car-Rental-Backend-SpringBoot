package com.example.demo.util;

/**
 * Small input-sanitization helper (OWASP: "sanitize all user inputs").
 *
 * Note: JPQL queries in this project already use bind parameters
 * (see CarRepository), which is the real defense against SQL injection —
 * parameterized queries never concatenate raw input into SQL. This class
 * additionally trims/caps/strips search input so that stray HTML/script
 * characters can't be reflected back in API responses or logs.
 */
public final class InputSanitizer {

    private static final int MAX_KEYWORD_LENGTH = 100;

    private InputSanitizer() {
    }

    /**
     * Cleans a free-text search keyword (e.g. car brand/model search):
     * trims whitespace, caps length, and strips characters that have no
     * legitimate place in a brand/model search term.
     * Returns null for blank input so callers can treat it as "no filter".
     */
    public static String sanitizeSearchKeyword(String input) {
        if (input == null) {
            return null;
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        if (trimmed.length() > MAX_KEYWORD_LENGTH) {
            trimmed = trimmed.substring(0, MAX_KEYWORD_LENGTH);
        }
        // Strip characters commonly used in XSS/SQLi/log-injection payloads.
        return trimmed.replaceAll("[<>\"';%\\\\]", "");
    }
}