package peertrust.filter;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * Wrapper that can access a resource on the web, but stores its content in a
 * buffer after receiving it, so that the user of this class can use the content
 * or manipulate it. The ProtectedResourcePolicyFilter for example uses this class
 * to get the real resource content, but only forwards it to the client if the
 * resource is unprotected or the client fulfilled all policies. If not, the client
 * gets an error page instad.
 * Wraps the http response, and is itself a "dummy" http response.
 * @see ProtectedResourcePolicyFilter
 * @author Sebastian Wittler
 */
public class ProtectedResponseWrapper extends HttpServletResponseWrapper {

	/**
	 * Outputstream for the wrapper in which the resource content will be stored
	 * for access or manipulation for the user of the wrapper.
	 * @author Sebastian Wittler
	 */
	public class ProtectedOutputStream extends ServletOutputStream {
		// The ByteArrayOutputStream used to store the resource content.
		private ByteArrayOutputStream bos;

		/**
		 * @see ServletOutputStream.close
		 */
		public void close() throws IOException {
			bos.close();
		}

		/**
		 * @see ServletOutputStream.flush
		 */
		public void flush() throws IOException {
			bos.flush();
		}

		/**
		 * Constructor
		 */
		public ProtectedOutputStream() {
			super();
			bos = new ByteArrayOutputStream();
		}

		/**
		 * @see ServletOutputStream.write
		 */
		public void write(int b) throws IOException {
			bos.write(b);
		}

		/**
		 * @see ServletOutputStream.write
		 */
		public void write(byte[] b, int off, int len) throws IOException {
			bos.write(b, off, len);
		}

		/**
		 * @see ServletOutputStream.write
		 */
		public void write(byte[] b) throws IOException {
			bos.write(b);
		}

		/**
		 * Gets the resource content as a byte array.
		 * @return Resource content.
		 */
		public byte[] getData() {
			return bos.toByteArray();
		}
	}

	// The output stream for this wrapper (see inner class above), the user of this
	// wrapper can manipulate its content later
	private ProtectedOutputStream pos;

	// Output stream/writer for text (jsp for example), the user of this wrapper can
	// manipulate its content later
	private CharArrayWriter caw;

	/**
	 * Constructor.
	 * @param response The http response that should be wrapped.
	 */
	public ProtectedResponseWrapper(HttpServletResponse response) {
		super(response);
		caw = new CharArrayWriter();
		pos = new ProtectedOutputStream();
	}

	/**
	 * @see HttpServletResponseWrapper.getWriter
	 */
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(caw);
	}

	/**
	 * @see HttpServletResponseWrapper.getOutputStream
	 */
	public ServletOutputStream getOutputStream() throws IOException {
		return pos;
	}

	/**
	 * Returns the wrapped http responses content as an byte array.
	 * @return The resource content of the wrappd http response.
	 */
	public byte[] getBytes() {
		return pos.getData();
	}

	/**
	 * Returns the wrapped http responses content as an a text string.
	 * @return The resource content of the wrappd http response.
	 */
	public String toString() {
		return caw.toString();
	}
}