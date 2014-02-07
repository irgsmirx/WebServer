/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ramforth.webserver.http.headers.general;

import com.ramforth.webserver.http.DispositionType;
import com.ramforth.webserver.http.IDispositionType;
import com.ramforth.webserver.http.IMediaType;
import com.ramforth.webserver.http.headers.StringHttpHeader;
import java.nio.charset.Charset;

/**
 *
 * @author tobias
 */
public class ContentDispositionHttpHeader extends StringHttpHeader {
    
    public static final String CONTENT_DISPOSITION = "Content-Disposition";
    
    private IDispositionType dispositionType;
    
    public ContentDispositionHttpHeader(String rawValue) {
        super(CONTENT_DISPOSITION, rawValue);
        
        parse();
    }
    
    private void parse() {
        this.dispositionType = new DispositionType();
                
        String[] splitRawValue = rawValue.split(";");
        
        if (splitRawValue.length > 0) {
            dispositionType.setType(splitRawValue[0]);
            
            for (int i = 1; i < splitRawValue.length; i++) {
                String[] splitParameter = splitRawValue[i].split("=");
                
                if (splitParameter.length == 2) {
                    dispositionType.addParameter(splitParameter[0].trim(), splitParameter[1].trim());
                }
            }
        }
    }

    public final IDispositionType getDispositionType() {
        return dispositionType;
    }
        
}
