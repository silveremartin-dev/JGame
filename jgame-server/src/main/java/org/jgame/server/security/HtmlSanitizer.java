/*
 * MIT License
 * Copyright (c) 2025 Google Gemini (Antigravity)
 * Copyright (c) 2022-2025 Silvere Martin-Michiellot
 */

package org.jgame.server.security;

import java.util.regex.Pattern;

/**
 * HTML/XSS sanitization utilities.
 * 
 * <p>Provides comprehensive protection against XSS attacks.</p>
 *
 * @author Google Gemini (Antigravity)
 * @version 1.0
 */
public final class HtmlSanitizer {

    // Dangerous patterns
    private static final Pattern SCRIPT_PATTERN = Pattern.compile(
            "<script[^>]*>.*?</script>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    private static final Pattern EVENT_HANDLER_PATTERN = Pattern.compile(
            "\\s+on\\w+\\s*=", Pattern.CASE_INSENSITIVE);
    private static final Pattern JAVASCRIPT_URL_PATTERN = Pattern.compile(
            "javascript\\s*:", Pattern.CASE_INSENSITIVE);
    private static final Pattern DATA_URL_PATTERN = Pattern.compile(
            "data\\s*:", Pattern.CASE_INSENSITIVE);
    private static final Pattern VBSCRIPT_URL_PATTERN = Pattern.compile(
            "vbscript\\s*:", Pattern.CASE_INSENSITIVE);
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile(
            "expression\\s*\\(", Pattern.CASE_INSENSITIVE);

    private HtmlSanitizer() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Sanitizes text for safe HTML display.
     * 
     * @param input the input text
     * @return sanitized text safe for HTML
     */
    public static String sanitize(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String result = input;

        // Remove script tags
        result = SCRIPT_PATTERN.matcher(result).replaceAll("");

        // Remove event handlers
        result = EVENT_HANDLER_PATTERN.matcher(result).replaceAll(" ");

        // Remove dangerous URLs
        result = JAVASCRIPT_URL_PATTERN.matcher(result).replaceAll("");
        result = DATA_URL_PATTERN.matcher(result).replaceAll("");
        result = VBSCRIPT_URL_PATTERN.matcher(result).replaceAll("");

        // Remove CSS expressions
        result = EXPRESSION_PATTERN.matcher(result).replaceAll("");

        // Escape HTML entities
        result = escapeHtml(result);

        return result;
    }

    /**
     * Escapes HTML special characters.
     * 
     * @param input the input text
     * @return escaped text
     */
    public static String escapeHtml(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder(input.length() + 16);
        for (char c : input.toCharArray()) {
            switch (c) {
                case '<' -> sb.append("&lt;");
                case '>' -> sb.append("&gt;");
                case '&' -> sb.append("&amp;");
                case '"' -> sb.append("&quot;");
                case '\'' -> sb.append("&#x27;");
                case '/' -> sb.append("&#x2F;");
                default -> sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Sanitizes text for safe use in JSON strings.
     * 
     * @param input the input text
     * @return sanitized text safe for JSON
     */
    public static String sanitizeForJson(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder(input.length() + 16);
        for (char c : input.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 32) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.toString();
    }

    /**
     * Truncates and sanitizes user input.
     * 
     * @param input the input text
     * @param maxLength maximum allowed length
     * @return truncated and sanitized text
     */
    public static String sanitizeAndTruncate(String input, int maxLength) {
        if (input == null || input.isEmpty()) {
            return "";
        }

        String truncated = input.length() > maxLength 
                ? input.substring(0, maxLength)
                : input;

        return sanitize(truncated.trim());
    }
}
