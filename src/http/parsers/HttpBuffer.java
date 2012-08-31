package http.parsers;

public class HttpBuffer {

	private static final int INITIAL_CAPACITY = 2048;

	protected byte[] buf = null;
	protected int length = 0;

	public HttpBuffer() {
		buf = new byte[INITIAL_CAPACITY];
	}

	public HttpBuffer(int capacity) {
		buf = new byte[capacity];
	}

	public HttpBuffer append(byte b) {
		int newLength = length + 1;
		if (newLength > buf.length) {
			expandCapacity(newLength);
		}
		buf[length++] = b;
		return this;
	}

	public HttpBuffer append(int i) {
		return append((byte) i);
	}

	public void ensureCapacity(int minimumCapacity) {
		if (minimumCapacity > buf.length) {
			expandCapacity(minimumCapacity);
		}
	}

	protected void expandCapacity(int minimumCapacity) {
		int newCapacity = (buf.length + 1) * 2;
		if (newCapacity < 0) {
			newCapacity = Integer.MAX_VALUE;
		} else if (minimumCapacity > newCapacity) {
			newCapacity = minimumCapacity;
		}
		byte newBuf[] = new byte[newCapacity];
		System.arraycopy(buf, 0, newBuf, 0, length);
		buf = newBuf;
	}

	public void trim() {
		if (length < buf.length) {
			byte[] newBuf = new byte[length];
			System.arraycopy(buf, 0, newBuf, 0, length);
			this.buf = newBuf;
		}
	}

	public byte[] getCopy() {
		byte bufCopy[] = new byte[length];
		System.arraycopy(buf, 0, bufCopy, 0, length);
		return bufCopy;
	}

	public void reset() {
		length = 0;
	}

  @Override
	public String toString() {
		return new String(getCopy());
	}

}
