/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
     @Test
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
         BufferedInputStream bis = new BufferedInputStream(is);
         
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
