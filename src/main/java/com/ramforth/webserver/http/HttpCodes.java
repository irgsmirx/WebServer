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

public abstract class HttpCodes {

    protected static Map<Integer, String> statusMap = new TreeMap<>();

    static {
        fillStatusMap();
    }

    private static String put(int err, String message) {
        return statusMap.put(new Integer(err), message);
    }

    protected static String get(int err) {
        return statusMap.get(new Integer(err));
    }

    protected static void fillStatusMap() {
        /* Informational 1xx */
        put(100, "Continue");
        put(101, "Switching Protocols");
        /* Successful 2xx */
        put(200, "OK");
        put(201, "Created");
        put(202, "Accepted");
        put(203, "Non-Authoritative Information");
        put(204, "No Content");
        put(205, "Reset Content");
        put(206, "Partial Content");
        /* Redirection 3xx */
        put(300, "Multiple Choices");
        put(301, "Moved Permanently");
        put(302, "Found");
        put(303, "See Other");
        put(304, "Not Modified");
        put(305, "Use Proxy");
        put(306, "(Unused)");
        put(307, "Temporary Redirect");
        /* Client Error 4xx */
        put(400, "Bad Request");
        put(401, "Unauthorized");
        put(402, "Payment Required");
        put(403, "Forbidden");
        put(404, "Not Found");
        put(405, "Method Not Allowed");
        put(406, "Not Acceptable");
        put(407, "Proxy Authentication Required");
        put(408, "Request Timeout");
        put(409, "Conflict");
        put(410, "Gone");
        put(411, "Length Required");
        put(412, "Precondition Failed");
        put(413, "Request Entity Too Large");
        put(414, "Request-URI Too Large");
        put(415, "Unsupported Media Type");
        put(416, "Requested Range Not Satisfiable");
        put(417, "Expectation Failed");
        /* Server Error 5xx */
        put(500, "Internal Server Error");
        put(501, "Not Implemented");
        put(502, "Bad Gateway");
        put(503, "Service Unavailable");
        put(504, "Gateway Timeout");
        put(505, "HTTP Version Not Supported");
    }
}
