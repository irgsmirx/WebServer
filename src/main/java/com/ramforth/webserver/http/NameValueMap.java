/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    private Map<String, String> map = new TreeMap<>();

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
