package http;

import java.util.Hashtable;

public class HttpMimeTypes {

	protected static Hashtable<String, String> typeMap = new Hashtable<String, String>();

	public static void setMimeType(String suffix, String mimeType) {
		typeMap.put(suffix, mimeType);
	}

	public static String getMimeType(String suffix) {
		String contentType = null;

		if (suffix != null) {
			contentType = typeMap.get(suffix);
		}

		if (contentType == null) {
			contentType = "content/unknown";
		}

		return contentType;
	}

	protected static void loadTypeMap() {

	}

	protected static void fillTypeMap() {
		setMimeType("", "content/unknown");
		setMimeType(".uu", "application/octet-stream");
		setMimeType(".exe", "application/octet-stream");
		setMimeType(".ps", "application/postscript");
		setMimeType(".zip", "application/zip");
		setMimeType(".sh", "application/x-shar");
		setMimeType(".tar", "application/x-tar");
		setMimeType(".snd", "audio/basic");
		setMimeType(".au", "audio/basic");
		setMimeType(".wav", "audio/x-wav");
		setMimeType(".gif", "image/gif");
		setMimeType(".jpg", "image/jpeg");
		setMimeType(".jpeg", "image/jpeg");
		setMimeType(".htm", "text/html");
		setMimeType(".html", "text/html");
		setMimeType(".text", "text/plain");
		setMimeType(".c", "text/plain");
		setMimeType(".cc", "text/plain");
		setMimeType(".c++", "text/plain");
		setMimeType(".h", "text/plain");
		setMimeType(".pl", "text/plain");
		setMimeType(".txt", "text/plain");
		setMimeType(".java", "text/plain");
	}

	static {
		loadTypeMap();
	}

}
