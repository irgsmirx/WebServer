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
package com.ramforth.webserver.http.headers.general;

import com.ramforth.webserver.http.DispositionType;
import com.ramforth.webserver.http.IDispositionType;
import com.ramforth.webserver.http.IMediaType;
import com.ramforth.webserver.http.headers.StringHttpHeader;
import java.nio.charset.Charset;

/**
 *
 * @author tobias
 */
public class ContentDispositionHttpHeader extends StringHttpHeader {

    public static final String CONTENT_DISPOSITION = "Content-Disposition";

    private IDispositionType dispositionType;

    public ContentDispositionHttpHeader(String rawValue) {
        super(CONTENT_DISPOSITION, rawValue);

        parse();
    }

    private void parse() {
        this.dispositionType = new DispositionType();

        String[] splitRawValue = rawValue.split(";");

        if (splitRawValue.length > 0) {
            dispositionType.setType(splitRawValue[0]);

            for (int i = 1; i < splitRawValue.length; i++) {
                String[] splitParameter = splitRawValue[i].split("=");

                if (splitParameter.length == 2) {
                    dispositionType.addParameter(splitParameter[0].trim(), splitParameter[1].trim());
                }
            }
        }
    }

    public final IDispositionType getDispositionType() {
        return dispositionType;
    }

}
