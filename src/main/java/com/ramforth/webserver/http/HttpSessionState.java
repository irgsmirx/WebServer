/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
