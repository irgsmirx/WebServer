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

import com.ramforth.webserver.http.sessionState.SessionStateMode;
import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpSessionState implements Iterable<Object> {

    private Map<String, Object> items = new TreeMap<>();

    public int getCodePage() {
        throw new RuntimeException();
    }

    public void setCodePage(int value) {
        throw new RuntimeException();
    }

    public HttpSessionState getContents() {
        throw new RuntimeException();
    }

    public HttpCookieMode getCookieMode() {
        throw new RuntimeException();
    }

    public boolean isCookieless() {
        throw new RuntimeException();
    }

    public boolean isNewSession() {
        throw new RuntimeException();
    }

    public boolean isReadOnly() {
        throw new RuntimeException();
    }

    public Set<String> getKeys() {
        return items.keySet();
    }

    public int getLCID() {
        throw new RuntimeException();
    }

    public void setLCID(int value) {
        throw new RuntimeException();
    }

    public SessionStateMode getMode() {
        throw new RuntimeException();
    }

    public String getSessionID() {
        throw new RuntimeException();
    }

//  public HttpStaticObjectsCollectionBase getStaticObjects() {
//    throw new RuntimeException();
//  }
    public int getTimeout() {
        throw new RuntimeException();
    }

    public void setTimeout(int value) {
        throw new RuntimeException();
    }

    public Object getAt(int index) {
        throw new RuntimeException();
    }

    public void setAt(int index, Object value) {
        throw new RuntimeException();
    }

    public Object getAt(String name) {
        throw new RuntimeException();
    }

    public void setAt(String name, Object value) {
        throw new RuntimeException();
    }

    public int getCount() {
        throw new RuntimeException();
    }

    public boolean isSynchronized() {
        throw new RuntimeException();
    }

    public Object getSyncRoot() {
        throw new RuntimeException();
    }

    public void abandon() {
        throw new RuntimeException();
    }

    public void add(String name, Object value) {
        throw new RuntimeException();
    }

    public void clear() {
        throw new RuntimeException();
    }

    public void remove(String name) {
        throw new RuntimeException();
    }

    public void removeAll() {
        throw new RuntimeException();
    }

    public void removeAt(int index) {
        throw new RuntimeException();
    }

    public void copyTo(Array array, int index) {
        throw new RuntimeException();
    }

    @Override
    public Iterator<Object> iterator() {
        return items.values().iterator();
    }
}
