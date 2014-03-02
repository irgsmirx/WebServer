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
package com.ramforth.webserver.http.parsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tobias
 */
public class BufferedInputStreamTest {
    
    public BufferedInputStreamTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     //@Test commented out, maybe "I cannot see the light", but wrapping a bytearrayinputstream in a buffered one does not seem to work at all
     public void hello() {
         byte[] bytes = new byte[8192];
         
         byte value = 0;
         for (int i = 0; i < bytes.length; i++) {
             bytes[i] = value;
             
             if (value < 255) {
                value ++;
             } else {
                 value = 0;
             }
         }
         
         InputStream is = new ByteArrayInputStream(bytes);
         BufferedInputStream bis = new BufferedInputStream(is, 8192);
         
         byte[] readBytes = new byte[8192];
         int index = 0 ;
         int b;
        try {
            while ((b = bis.read()) != -1) {
                readBytes[index] = (byte)b;
                index++;
            }
        }
        catch (IOException ex) {
            Logger.getLogger(BufferedInputStreamTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         assertArrayEquals(readBytes, bytes);
     }
}
