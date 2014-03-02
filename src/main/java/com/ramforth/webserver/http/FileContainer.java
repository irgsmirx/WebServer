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

import com.ramforth.webserver.exceptions.IOException;

import java.io.File;
import java.io.PrintStream;

public class FileContainer implements WebContainer {

    static final int BUF_SIZE = 2048;
    private byte[] buf;
    private String filename;
    private File file;
    private String contentType;

    public FileContainer() {
        buf = new byte[BUF_SIZE];
    }

    @Override
    public void print(PrintStream p) throws IOException {
//		if (file.isFile()) {
//			try (InputStream is = new FileInputStream(file.getAbsolutePath())) {
//				int n;
//				while ((n = is.read(buf)) > 0) {
//					p.write(buf, 0, n);
//				}
//			}
//		} else if (file.isDirectory()) {
//			XHTMLDocument xd = new XHTMLDocument("Directory listing");
//
//			XHTMLBody xb = new XHTMLBody();
//			XHTMLHeading1 xh = new XHTMLHeading1();
//			XHTMLPCData xp = new XHTMLPCData(file.getAbsolutePath());
//			xh.add(xp);
//			xb.add(xh);
//			XHTMLHorizontalLine xl = new XHTMLHorizontalLine();
//			xb.add(xl);
//			XHTMLParagraph xpa = new XHTMLParagraph();
//
//			String[] list = file.list();
//			for (int i = 0; list != null && i < list.length; i++) {
//				File f = new File(file, list[i]);
//				if (f.isDirectory()) {
//					XHTMLAnchor xa = new XHTMLAnchor();
//					xa.setHref(list[i] + File.separator);
//					XHTMLPCData xp2 = new XHTMLPCData(list[i] + File.separator);
//					xa.add(xp2);
//					XHTMLLineBreak xlb = new XHTMLLineBreak();
//					xa.add(xlb);
//				} else {
//					XHTMLAnchor xa = new XHTMLAnchor();
//					xa.setHref(list[i]);
//					XHTMLPCData xp2 = new XHTMLPCData(list[i]);
//					xa.add(xp2);
//					XHTMLLineBreak xlb = new XHTMLLineBreak();
//					xa.add(xlb);
//				}
//			}
//
//			xb.add(xpa);
//			xb.add(xl);
//			XHTMLParagraph xpa2 = new XHTMLParagraph();
//			XHTMLPCData xp3 = new XHTMLPCData((new GregorianCalendar()).toString());
//			xpa.add(xp3);
//			xb.add(xpa2);
//
//			StringBuffer sb = new StringBuffer();
//			xd.appendXML(sb);
//
//		}
    }

    @Override
    public boolean isConsistent() {
        return (file != null && file.exists() && file.isFile());
    }

    /**
     * @return Returns the buf.
     */
    public byte[] getBuf() {
        return buf;
    }

    /**
     * @param buf The buf to set.
     */
    public void setBuf(byte[] buf) {
        this.buf = buf;
    }

    /**
     * @return Returns the file.
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file The file to set.
     */
    public void setFile(File file) {
        this.file = file;

        if (file.isFile()) {
        } else if (file.isDirectory()) {
        }
    }

    /**
     * @return Returns the suffix.
     */
    public String getSuffix() {
        return file.getName().substring(file.getName().lastIndexOf('.'));
    }

    @Override
    public String getContentType() {
        if (contentType != null) {
            return contentType;
        } else {
            return HttpMimeTypes.getMimeType(getSuffix());
        }
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public long getContentLength() {
        if (file != null) {
            return file.length();
        } else {
            return 0;
        }
    }
}
