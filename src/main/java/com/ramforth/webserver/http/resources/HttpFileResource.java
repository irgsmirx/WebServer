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
package com.ramforth.webserver.http.resources;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpFileResource extends AbstractHttpResource {

    protected String serverPath;

    public void setServerPath(String value) {
        this.serverPath = value;
    }

    public String getServerPath() {
        return serverPath;
    }

    @Override
    public InputStream tryOpenStream() {
        try {
            return new FileInputStream(serverPath);
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(HttpFileResource.class.getName()).log(Level.SEVERE, null, ex);
            throw new com.ramforth.utilities.exceptions.IOException(ex);
        }
    }

}
