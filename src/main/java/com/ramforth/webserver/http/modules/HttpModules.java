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
package com.ramforth.webserver.http.modules;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Tobias Ramforth <tobias.ramforth at tu-dortmund.de>
 */
public class HttpModules implements IHttpModules {

    private final List<IHttpModule> modules = new ArrayList<>();

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
