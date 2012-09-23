/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ramforth.webserver.http.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpModules implements IHttpModules {

    private List<IHttpModule> modules = new ArrayList<>();

    @Override
    public void add(IHttpModule module) {
        modules.add(module);
    }

    @Override
    public void remove(IHttpModule module) {
        modules.remove(module);
    }

    @Override
    public IHttpModule getAt(int index) {
        return modules.get(index);
    }

    @Override
    public int numberOfModules() {
        return modules.size();
    }

    @Override
    public Iterator<IHttpModule> iterator() {
        return modules.iterator();
    }
}
