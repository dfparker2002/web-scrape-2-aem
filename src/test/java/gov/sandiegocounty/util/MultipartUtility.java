package gov.sandiegocounty.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * src: https://stackoverflow.com/questions/34276466/simple-httpurlconnection-post-file-multipart-form-data-from-android-to-google-bl
 */
public class MultipartUtility {


	private static final Logger log = LoggerFactory.getLogger(MultipartUtility.class);

	private final String boundary;
	private static final String LINE_FEED = "\r\n";
	private HttpURLConnection httpConn;
	private String charset;
	private OutputStream outputStream;
	private PrintWriter writer;

	/**
	 * This constructor initializes a new HTTP POST request with content type is
	 * set to multipart/form-data
	 *
	 * @param requestURL
	 * @param charset
	 * @throws IOException
	 */
	public MultipartUtility(String requestURL, String charset) throws IOException {
		this.charset = charset;

// creates a unique boundary based on time stamp
		boundary = "===" + System.currentTimeMillis() + "===";
		URL url = new URL(requestURL);
		httpConn = (HttpURLConnection) url.openConnection();

////////////////
// add request header
        httpConn.setReadTimeout(10000);
        httpConn.setConnectTimeout(15000);

		httpConn.setRequestProperty( "Accept-Language", "en-US,en;q=0.5");
        httpConn.setRequestProperty( "charset", "utf-8");
        httpConn.setRequestProperty( "Host", "192.168.200.87:4502");
        httpConn.setRequestProperty( "Connection", "keep-alive");
        httpConn.setRequestProperty( "Pragma", "no-cache");
        httpConn.setRequestProperty( "Cache-Control", "no-cache");
        httpConn.setRequestProperty( "Upgrade-Insecure-Requests", "1");
        httpConn.setRequestProperty( "DNT", "1");
        httpConn.setRequestProperty( "Referer", "http://192.168.200.87:4502/crx/de/index.jsp");
        httpConn.setRequestProperty( "Accept-Encoding", "gzip, deflate");
        httpConn.setRequestProperty( "Accept-Language", "en-US,en;q=0.9" );

        httpConn.setRequestProperty( "Authorization", "Basic YWRtaW46YWRtaW4=");
////////////////

		httpConn.setUseCaches(false);
		httpConn.setDoOutput(true); // indicates POST method
		httpConn.setDoInput(true);
		httpConn.setRequestProperty( "Content-Type"
			, "multipart/form-data; boundary=" + boundary);
		outputStream = httpConn.getOutputStream();
		writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
	}

	/**
	 * Adds a form field to the request
	 *
	 * @param name
	 *            field name
	 * @param value
	 *            field value
	 */
	public void addFormField(String name, String value) {
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
		writer.append("Content-Type: text/plain; charset=" + charset).append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.append(value).append(LINE_FEED);
log.debug( " MultipartUtility.addFormField :: name {},  value {}\n\n", name, value );
		writer.flush();
	}

	/**
	 * Adds a upload file section to the request
	 *
	 * @param fieldName
	 *            name attribute in <input type="file" name="..." />
	 * @param uploadFile
	 *            a File to be uploaded
	 * @throws IOException
	 */
	public void addFilePart(String fieldName, File uploadFile) throws IOException {
		String fileName = uploadFile.getName();
		writer.append("--" + boundary).append(LINE_FEED);
		writer.append("Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + fileName + "\"")
				.append(LINE_FEED);
		writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
		writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
		writer.append(LINE_FEED);
		writer.flush();

		FileInputStream inputStream = new FileInputStream(uploadFile);
		byte[] buffer = new byte[4096];
		int bytesRead = -1;
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
		}
		outputStream.flush();
		inputStream.close();
		writer.append(LINE_FEED);
		writer.flush();
	}

	/**
	 * Adds a header field to the request.
	 *
	 * @param name
	 *            - name of the header field
	 * @param value
	 *            - value of the header field
	 */
	public void addHeaderField(String name, String value) {
		writer.append(name + ": " + value).append(LINE_FEED);
		writer.flush();
	}

	/**
	 * Completes the request and receives response from the server.
	 *
	 * @return a list of Strings as response in case the server returned status
	 *         OK, otherwise an exception is thrown.
	 * @throws IOException
	 */
	public List<String> finish() throws IOException {
		List<String> response = new ArrayList<String>();
		writer.append(LINE_FEED).flush();
		writer.append("--" + boundary + "--").append(LINE_FEED);
		writer.close();

// checks server's status code first
		int status = httpConn.getResponseCode();
System.out.printf( " MultipartUtility :: finish : conn data status: %d, mesg %s\n\n", status, httpConn.getResponseMessage());
		if (status == HttpURLConnection.HTTP_OK) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				response.add(line);
			}
			reader.close();
			httpConn.disconnect();
		} else {
			throw new IOException("Server returned non-OK status: " + status);
		}
		return response;
	}
}
