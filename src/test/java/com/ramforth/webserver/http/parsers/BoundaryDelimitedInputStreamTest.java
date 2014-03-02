/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ramforth.webserver.http.parsers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
public class BoundaryDelimitedInputStreamTest {

    private byte[] boundary = new byte[] { (byte)'-', (byte)'-', (byte)'A', (byte)'B' };
    private byte[] testA = new byte[] { (byte)'-', (byte)'-', (byte)'A', (byte)'4', (byte)'5', (byte)'6' };
    private byte[] testB = new byte[] { (byte)'-', (byte)'-', (byte)'A', (byte)'4', (byte)'5', (byte)'6', (byte)'-', (byte)'-', (byte)'A', (byte)'B' };

    
    public BoundaryDelimitedInputStreamTest() {
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
    public void hello1() {
        ByteArrayInputStream bais = new ByteArrayInputStream(testA);
        BoundaryDelimitedInputStream bdis = new BoundaryDelimitedInputStream(bais, boundary);
        
        for (int i = 0; i < testA.length; i++) {
            try {
                assertEquals(testA[i], (byte)bdis.read());
            }
            catch (IOException ex) {
                Logger.getLogger(BoundaryDelimitedInputStreamTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Test
    public void hello2() {
        ByteArrayInputStream bais = new ByteArrayInputStream(testB);
        BoundaryDelimitedInputStream bdis = new BoundaryDelimitedInputStream(bais, boundary);
        
        for (int i = 0; i < 6; i++) {
            try {
                byte b = (byte)bdis.read();
                assertEquals(testB[i], b);
            }
            catch (IOException ex) {
                Logger.getLogger(BoundaryDelimitedInputStreamTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try {
            byte b = (byte)bdis.read();
            assertEquals('-', b);

            b = (byte)bdis.read();
            assertEquals('-', b);
        }
        catch (IOException ex) {
            Logger.getLogger(BoundaryDelimitedInputStreamTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Test
    public void intCastingToByteTest() {
        for (int i = 0; i < 256; i++) {
            System.out.println((byte)i);
        }
    }

    
    
}
