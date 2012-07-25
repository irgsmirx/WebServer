package http;

import java.io.*;
import java.util.GregorianCalendar;

public class FileContainer implements WebContainer {

	final static int BUF_SIZE = 2048;

	private byte[] buf;
	private String filename;
	private File file;
	private String contentType;

	public FileContainer() {
		buf = new byte[BUF_SIZE];
	}

  @Override
	public void print(PrintStream p) throws IOException {
		if (file.isFile()) {
			try (InputStream is = new FileInputStream(file.getAbsolutePath())) {
				int n;
				while ((n = is.read(buf)) > 0) {
					p.write(buf, 0, n);
				}
			}
		} else if (file.isDirectory()) {
			XHTMLDocument xd = new XHTMLDocument("Directory listing");

			XHTMLBody xb = new XHTMLBody();
			XHTMLHeading1 xh = new XHTMLHeading1();
			XHTMLPCData xp = new XHTMLPCData(file.getAbsolutePath());
			xh.add(xp);
			xb.add(xh);
			XHTMLHorizontalLine xl = new XHTMLHorizontalLine();
			xb.add(xl);
			XHTMLParagraph xpa = new XHTMLParagraph();

			String[] list = file.list();
			for (int i = 0; list != null && i < list.length; i++) {
				File f = new File(file, list[i]);
				if (f.isDirectory()) {
					XHTMLAnchor xa = new XHTMLAnchor();
					xa.setHref(list[i] + File.separator);
					XHTMLPCData xp2 = new XHTMLPCData(list[i] + File.separator);
					xa.add(xp2);
					XHTMLLineBreak xlb = new XHTMLLineBreak();
					xa.add(xlb);
				} else {
					XHTMLAnchor xa = new XHTMLAnchor();
					xa.setHref(list[i]);
					XHTMLPCData xp2 = new XHTMLPCData(list[i]);
					xa.add(xp2);
					XHTMLLineBreak xlb = new XHTMLLineBreak();
					xa.add(xlb);
				}
			}

			xb.add(xpa);
			xb.add(xl);
			XHTMLParagraph xpa2 = new XHTMLParagraph();
			XHTMLPCData xp3 = new XHTMLPCData((new GregorianCalendar()).toString());
			xpa.add(xp3);
			xb.add(xpa2);

			StringBuffer sb = new StringBuffer();
			xd.appendXML(sb);

		}
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
	 * @param buf
	 *          The buf to set.
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
	 * @param file
	 *          The file to set.
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
