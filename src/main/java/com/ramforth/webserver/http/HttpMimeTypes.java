/* 
 * Copyright (C) 2014 Tobias Ramforth
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ramforth.webserver.http;

import java.util.Map;
import java.util.TreeMap;

public class HttpMimeTypes {

    protected static Map<String, String> typeMap = new TreeMap<>();

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
