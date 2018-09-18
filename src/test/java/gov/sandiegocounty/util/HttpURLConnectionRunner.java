package gov.sandiegocounty.util;


import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

//import javax.net.ssl.HttpsURLConnection;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * Refs
 * HTTP Con
 * 	https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
 * 	https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/
 * 	https://stackoverflow.com/questions/40574892/how-to-send-post-request-with-x-www-form-urlencoded-body
 * 	https://stackoverflow.com/questions/11766878/sending-files-using-post-with-httpurlconnection?rq=1
 * 
 * Multipart util
 * https://stackoverflow.com/questions/34276466/simple-httpurlconnection-post-file-multipart-form-data-from-android-to-google-bl
 * 
 * HTTPClient
 * 	https://stackoverflow.com/questions/28559377/sending-multipartentity-using-httpurlconnection
 * 
 * Cookies
 * 	https://duckduckgo.com/?q=java.net.HttpURLConnection+add+cookie&ia=qa&iax=qa
 */
public class HttpURLConnectionRunner {

	
	private static final Logger log = LoggerFactory.getLogger(HttpURLConnectionRunner.class);

//	private static final String CREDS = "admin:admin@";
	private static final String OES_HOME = /*String.format( */"http://192.168.200.87:4502%s"/*, CREDS)*/;
//	private final String USER_AGENT = "Mozilla/5.0";
//	private static final String twoHyphens = "--";
//	private static final String boundary = "*****" + Long.toString(System.currentTimeMillis()) + "*****";
//	private static final String lineEnd = "\r\n";
//	private static final int maxBufferSize = 1 * 1024 * 1024;
	static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();

//	public static void main(String[] args) throws Exception {
//
//		HttpURLConnectionRunner http = new HttpURLConnectionRunner();
///*
//		System.out.println("Testing 1 - Send Http GET request");
//		http.sendGet();
//*/
//		System.out.println("\nTesting 2 - Send Http POST request");
////		http.sendPost();
//
//	}

/*
// HTTP GET request
	private void sendGet() throws Exception {

		String url = OES_HOME;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println(response.toString());

	}
*/

// HTTP POST request :: HTML DATA
	/**
	 * @param e
	 * @param tgt_node node path						; e.g., /content/oes/en-us/test
	 * @param tgt_internal_page_path content/attr path	; e.g., jcr:content/parcontent/text/text
	 * @throws Exception
	 */
	static public void execPost( final Element e, final String tgt_node 
		, final String tgt_internal_page_path 
	) throws Exception {

// OK
log.debug( "INPUT {} {}", tgt_node, tgt_internal_page_path);

System.out.printf( " HttpURLConnectionRunner :: execPost \n"
	+ "INPUT\n- node %s\n- attr %s\n- data %s\n", tgt_node, tgt_internal_page_path, e);


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;

//////////


/*
 * Build seed config
 */
		URL obj = new URL(String.format( OES_HOME, tgt_node) );
System.err.println( " HttpURLConnectionRunner :: execPost TARGET NODE: " + obj.toString() );

/*
 * Open URL connection
 */
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

///////////////
/// * This is to add file content */
		MultipartUtility multipart = new MultipartUtility( 
			obj.toString(),
			StandardCharsets.UTF_8.toString()
		);
		multipart.addFormField( tgt_internal_page_path, e.html() );

// Send post request
		List<String> response = multipart.finish();
///////////////

log.debug( " HttpURLConnectionRunner :: execPost \n{}", String.join("\n** ", response));
	}

/////////////////
// HTTP POST structure data request
	/**
	 * @param e
	 * @param tgt_node node path						; e.g., /content/oes/en-us/test
	 * @param tgt_internal_page_path content/attr path	; e.g., jcr:content/parcontent/text/text
	 * @throws Exception
	 */
	static public void execPostStructure( final String data, final String tgt_node 
		, final String tgt_internal_page_path 
	) throws Exception {

// OK
log.debug( "INPUT {} {}", tgt_node, tgt_internal_page_path);

System.out.printf( " HttpURLConnectionRunner :: execPost \n"
	+ "INPUT\n- node %s\n- attr %s\n- data %s\n", tgt_node, tgt_internal_page_path, data );


	        int bytesRead, bytesAvailable, bufferSize;
	        byte[] buffer;

//////////


// Send post request
/*
 * Build seed config
 */
//        String urlParameters  = tgt_internal_page_path.concat("= ").concat(data);
//        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
//        int    postDataLength = postData.length;
		URL obj = new URL(String.format( OES_HOME, tgt_node) );
System.err.println( " HttpURLConnectionRunner :: execPostStructure TARGET NODE: " + obj.toString() );

/*
 * Open URL connection
 */
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

///////////////
/// * This is to add file content */
		MultipartUtility multipart = new MultipartUtility( 
			obj.toString(),
			StandardCharsets.UTF_8.toString()
		);

		multipart.addFormField( tgt_internal_page_path, data );
		List<String> response = multipart.finish();
///////////////
//System.out.println( " HttpURLConnectionRunner :: execPostStructure " + 
log.debug( " HttpURLConnectionRunner :: execPost \n{}", 
		String.join("\n** ", response));
	}

/////////////////
	/**
	 * 
	 */
	private void addCookie(String key, String val, HttpURLConnection con) {

log.debug( " :: addCookie {} = {}", key , val );
		if (msCookieManager.getCookieStore().getCookies().size() > 0) {
// While joining the Cookies, use ',' or ';' as needed. Most of the
// servers are using ';'
			String cookies = "";
			for (HttpCookie cookie : msCookieManager.getCookieStore().getCookies()) {
				cookies.concat(cookie.getName() ).concat("=").concat(cookie.getValue() );
			}

		}
		
	}
	private void parseCookie(HttpURLConnection con) {

        Map<String, List<String>> headerFields = con.getHeaderFields();
        List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

        if (cookiesHeader != null) {
            for (String cookie : cookiesHeader) {
                msCookieManager.getCookieStore().add(null,HttpCookie.parse(cookie).get(0));
            }               
        }
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		final String tgt_node = "/content/oes/en-us/test/jcr:content/parcontent/text/text";

//System.err.println( Arrays.asList( tgt_node.split( "/jcr:content" ) ) );
///tgt_node.split( "/jcr:content" )

		Element e = new Element( "p" );
		e.tagName("p");
		e.html("&nbsp;<p>stuff " + System.currentTimeMillis() + "</p>");
		HttpURLConnectionRunner.execPost(e
			, Arrays.asList( tgt_node.split( "/jcr:content" ) ).get(0)		// node/attr path 
			, "jcr:content".concat( Arrays.asList( tgt_node.split( "/jcr:content" ) ).get(1) )
//			, false									// data, not site structure data
		);

	}
}