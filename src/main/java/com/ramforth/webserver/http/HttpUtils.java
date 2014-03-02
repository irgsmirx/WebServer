package com.ramforth.webserver.http;

public abstract class HttpUtils {

    public static String QUOTE = "&quot;";
    public static String AMPERSAND = "&amp;";
    public static String GREATER_THAN = "&gt;";
    public static String LESS_THAN = "&lt;";
    public static String HASH = "&#";
    public static String SEMICOLON = ";";

    public static String httpVersion(int major, int minor) {
        return "HTTP/" + major + "." + minor;
    }

    public static boolean isCHAR(int ch) {
        return (ch >= 0 && ch <= 127);
    }

    public static boolean isUPALPHA(int ch) {
        return (ch >= 65 && ch <= 90);
    }

    public static boolean isLOALPHA(int ch) {
        return (ch >= 97 && ch <= 122);
    }

    public static boolean isALPHA(int ch) {
        return (isUPALPHA(ch) || isLOALPHA(ch));
    }

    public static boolean isCTL(int ch) {
        return (ch >= 0 && ch <= 31 || ch == 127);
    }

    public static boolean isDIGIT(int ch) {
        return (ch >= 48 && ch <= 57);
    }

    public static boolean isCR(int ch) {
        return (ch == 13);
    }

    public static boolean isLF(int ch) {
        return (ch == 10);
    }

    public static boolean isSP(int ch) {
        return (ch == 32);
    }

    public static boolean isHT(int ch) {
        return (ch == 9);
    }

    public static boolean isDoubleQuoteMark(int ch) {
        return (ch == 34);
    }

    public static boolean isGreaterThan(int ch) {
        return (ch == 62);
    }

    public static boolean isLessThan(int ch) {
        return (ch == 60);
    }

    public static boolean isAmpersand(int ch) {
        return (ch == 38);
    }

    public static boolean isSeparator(int ch) {
        return (ch == '(' || ch == ')' || ch == '<' || ch == '>' || ch == '@' || ch == ',' || ch == ';' || ch == ':'
                || ch == '\\' || ch == '\"' || ch == '/' || ch == '[' || ch == ']' || ch == '?' || ch == '=' || ch == '{'
                || ch == '}' || ch == ' ' || ch == '\t');
    }

    public static String encode(String s) {
        int num = indexOfHtmlEncodingChars(s, 0);

        if (num == -1) {
            return s;
        }

        boolean appendRest = true;

        StringBuilder stringBuilder = new StringBuilder(s.length() + 5);
        int length = s.length();
        int num2 = 0;
        do {
            if (num > num2) {
                stringBuilder.append(s, num2, num - num2);
            }
            int c = s.codePointAt(num);
            if (c <= '>') {
                if (!isDoubleQuoteMark(c)) {
                    if (!isAmpersand(c)) {
                        switch (c) {
                            case '<':
                                stringBuilder.append(LESS_THAN);
                                break;
                            case '>':
                                stringBuilder.append(GREATER_THAN);
                                break;
                        }
                    } else {
                        stringBuilder.append(AMPERSAND);
                    }
                } else {
                    stringBuilder.append(QUOTE);
                }
            } else {
                stringBuilder.append(HASH).append((int) c).append(SEMICOLON);
            }
            num2 = num + 1;
            if (num2 >= length) {
                appendRest = false;
            }
            num = indexOfHtmlEncodingChars(s, num2);
        } while (num != -1);

        if (appendRest) {
            stringBuilder.append(s, num2, length - num2);
        }

        return stringBuilder.toString();
    }

    private static int indexOfHtmlEncodingChars(String s, int startPos) {
        int i = s.length() - startPos;
        int position = startPos;

        while (i > 0) {
            int c = s.codePointAt(position);

            if (c <= 62) {
                if (!isDoubleQuoteMark(c) && !isAmpersand(c)) {
                    switch (c) {
                        case '<':
                        case '>':
                            break;
                        default:
                        case '=':
                            position++;
                            i--;
                            continue;
                    }
                }
                int result = s.length() - i;
                return result;
            }

            if (c >= 160 && c < 256) {
                int result = s.length() - i;
                return result;
            }

            position++;
            i--;
        }

        return -1;
    }
}
