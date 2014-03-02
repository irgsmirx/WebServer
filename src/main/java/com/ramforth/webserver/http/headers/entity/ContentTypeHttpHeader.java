/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.headers.entity;

import com.ramforth.webserver.http.IMediaType;
import com.ramforth.webserver.http.MediaType;
import com.ramforth.webserver.http.headers.StringHttpHeader;
import java.nio.charset.Charset;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class ContentTypeHttpHeader extends StringHttpHeader {

    public static final String CONTENT_TYPE = "Content-Type";
    private IMediaType mediaType;

    public ContentTypeHttpHeader(String rawValue) {
        super(CONTENT_TYPE, rawValue);

        parseMediaType();
    }

    private void parseMediaType() {
        this.mediaType = new MediaType();

        String[] splitRawValue = rawValue.split(";");

        if (splitRawValue.length > 0) {
            mediaType.setType(splitRawValue[0]);

            for (int i = 1; i < splitRawValue.length; i++) {
                String[] splitParameter = splitRawValue[i].split("=");

                if (splitParameter.length == 2) {
                    mediaType.addParameter(splitParameter[0].trim(), splitParameter[1].trim());
                }
            }
        }
    }

    public final IMediaType getMediaType() {
        return mediaType;
    }

    public final Charset getCharset() {
        return mediaType.getCharset();
    }

}
