package com.ramforth.webserver.http;

import java.io.IOException;
import java.io.PrintStream;

public interface WebContainer {

	public void print(PrintStream p) throws IOException;

	public boolean isConsistent();

	public String getContentType();
	public void setContentType(String contentType);

	public long getContentLength();

}
