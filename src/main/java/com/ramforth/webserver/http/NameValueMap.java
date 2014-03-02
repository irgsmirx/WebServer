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

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class NameValueMap implements Iterable<String> {

    private final Map<String, String> map = new TreeMap<>();

    public void add(String name, String value) {
        map.put(name, value);
    }

    public void remove(String name) {
        map.remove(name);
    }

    public boolean containsName(String name) {
        return map.containsKey(name);
    }

    public boolean containsValue(String value) {
        return map.containsValue(value);
    }

    public String get(String name) {
        return map.get(name);
    }

    public void clear() {
        map.clear();
    }

    public int numberOfEntries() {
        return map.size();
    }

    public void addAll(NameValueMap nameValueMap) {
        for (String name : nameValueMap) {
            add(name, nameValueMap.get(name));
        }
    }

    public NameValueMap union(NameValueMap nameValueMap) {
        NameValueMap union = new NameValueMap();

        for (String name : map.keySet()) {
            union.add(name, map.get(name));
        }

        for (String name : nameValueMap) {
            union.add(name, nameValueMap.get(name));
        }

        return union;
    }

    @Override
    public Iterator<String> iterator() {
        return map.keySet().iterator();
    }
}
