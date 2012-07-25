package http;

public abstract class HttpUtils {

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

	public static boolean isSeparator(int ch) {
		return (ch == '(' || ch == ')' || ch == '<' || ch == '>' || ch == '@' || ch == ',' || ch == ';' || ch == ':'
				|| ch == '\\' || ch == '\"' || ch == '/' || ch == '[' || ch == ']' || ch == '?' || ch == '=' || ch == '{'
				|| ch == '}' || ch == ' ' || ch == '\t');
	}

}
