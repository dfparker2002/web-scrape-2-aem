package gov.sandiegocounty.util;


import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

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

	private static final String OES_HOME = /*String.format( */"http://192.168.200.87:4502%s"/*, CREDS)*/;

	static final String COOKIES_HEADER = "Set-Cookie";
    static java.net.CookieManager msCookieManager = new java.net.CookieManager();


// HTTP POST request :: HTML DATA
	/**
	 * @param e
	 * @param tgt_node node path						; e.g., /content/oes/en-us/test
	 * @param tgt_internal_page_path content/attr path	; e.g., jcr:content/oes-content/text/text
	 * @throws Exception
	 */
	static public void execPost( final String tgt_node 
		, final String tgt_internal_page_path , final Element e
	) throws Exception {

// OK
log.debug( "HttpURLConnectionRunner :: INPUT {} {}", tgt_node, tgt_internal_page_path);
/*
System.out.printf( "\n1 HttpURLConnectionRunner :: execPost \n"
	+ "INPUT"
	+ "\n- node %s"
	+ "\n- attr %s"
	+ "\n- data %s\n"
	, tgt_node, tgt_internal_page_path, e.outerHtml().substring(0, 25));
*/

/*
 * Build seed config
 */
		URL obj = new URL(String.format( OES_HOME, tgt_node) );
//System.err.println( " HttpURLConnectionRunner :: execPost TARGET NODE: " + obj.toString() );

/*
 * Open URL connection
 */
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		synchronized (con) {

/* 
 * This is to add file content
 */
			MultipartUtility multipart = new MultipartUtility(
				obj.toString(),
				StandardCharsets.ISO_8859_1.toString()
//				StandardCharsets.UTF_8.toString()
			);


//System.err.println( e.html().substring(0, 25));
			multipart.addFormField( tgt_internal_page_path, e.outerHtml() );


// Send post request
			List<String> response = multipart.finish();
log.debug( " HttpURLConnectionRunner :: execPost \n{}", String.join("\n** ", response));
		}
///////////////

	}
	
/////////////////
//HTTP POST structure data request
/**
* For use with non-standard (text) data
* E.g., 
* ./datetime: 2018-10-13T09:34:00.000-07:00
* ./datetime@TypeHint: Date
* 
* @param data
* @param tgt_node node path						; e.g., /content/oes/en-us/test
* @param tgt_internal_page_path content/attr path	; e.g., jcr:content/oes-content/text/text
* @throws Exception
*/
	static public void execPostComplexData(final String tgt_node
		, final String tgt_internal_page_path
		, final Map<String, String> data) throws Exception {

//OK
log.debug("INPUT \nnode {}\nattr path {}", tgt_node, tgt_internal_page_path);

System.out.printf(
 "======================\n2 HttpURLConnectionRunner :: execPostStructure \n" + "INPUT\n" +
 "- node %s\n" + "- attr %s\n" + "- data %s\n==============", tgt_node
 ,tgt_internal_page_path , data
);


//Send post request
/*
* Build seed config
*/
		URL obj = new URL(String.format(OES_HOME, tgt_node));
		System.err.println(" HttpURLConnectionRunner :: execPostStructure TARGET NODE: " + obj.toString());

/*
* Open URL connection
*/
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		synchronized (con) {

///////////////
/// * This is to add file content */
			MultipartUtility multipart = new MultipartUtility(obj.toString(), StandardCharsets.ISO_8859_1.toString());

//iterate data
			Iterator<Entry<String, String>> itr =  data.entrySet().iterator();
			while(itr.hasNext()){

				Map.Entry<String, String> entry = itr.next();
/*
* E.g., 
* ./datetime: 2018-10-13T09:34:00.000-07:00
* ./datetime@TypeHint: Date
*/
System.out.println("TEST :: Key is " + entry.getKey() + " Value is " + entry.getValue());
if(entry.getKey().equals("datetime")) {
	final Calendar c = JavaCalendarConverter.stringToSimpleDate( entry.getValue(), JavaCalendarConverter.asp_pattern);
System.out.println( " HttpURLConnectionRunner :: execPostComplexData " + c);
}
//System.out.println( " JavaCalendarConverter :: main " + JavaCalendarConverter.calendarToString(c, pattern));

			multipart.addFormField(tgt_internal_page_path.concat(entry.getKey()), entry.getValue());
			
			}
			List<String> response = multipart.finish();
///////////////
//System.out.println( " HttpURLConnectionRunner ::
/////////////// execPostStructure " +
log.debug(" HttpURLConnectionRunner :: execPost \n{}", String.join("\n** ", response));
		}
	}
/////////////////
	
// HTTP POST structure data request
	/**
	 * @param data
	 * @param tgt_node node path						; e.g., /content/oes/en-us/test
	 * @param tgt_internal_page_path content/attr path	; e.g., jcr:content/oes-content/text/text
	 * @throws Exception
	 */
	static public void execPostStructure( final String tgt_node 
		, final String tgt_internal_page_path, final String data 
	) throws Exception {

// OK
log.debug( "INPUT \nnode {}\nattr path {}", tgt_node, tgt_internal_page_path);
/*
System.out.printf( "\n2 HttpURLConnectionRunner :: execPostStructure \n"
	+ "INPUT\n"
	+ "- node %s\n"
	+ "- attr %s\n"
	+ "- data %s\n", 
		tgt_node
		,tgt_internal_page_path
		, data//StringUtils.chomp(data)
);
*/

// Send post request
/*
 * Build seed config
 */
		URL obj = new URL(String.format( OES_HOME, tgt_node) );
System.err.println( " HttpURLConnectionRunner :: execPostStructure TARGET NODE: " + obj.toString() );

/*
 * Open URL connection
 */
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		synchronized (con) {
	
///////////////
/// * This is to add file content */
		MultipartUtility multipart = new MultipartUtility( 
			obj.toString(),
			StandardCharsets.ISO_8859_1.toString()
		);

		multipart.addFormField( tgt_internal_page_path, StringUtils.chomp( data ));
System.out.println( " *** HttpURLConnectionRunner :: execPostStructure " + data);
		List<String> response = multipart.finish();
///////////////
//System.out.println( " HttpURLConnectionRunner :: execPostStructure " + 
log.debug( " HttpURLConnectionRunner :: execPost \n{}", 
		String.join("\n** ", response));
		}
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

		final String tgt_node = "/content/oes/en-us/test/jcr:content/oes-content/text/text";

//System.err.println( Arrays.asList( tgt_node.split( "/jcr:content" ) ) );
///tgt_node.split( "/jcr:content" )

		Element e = new Element( "p" );
		e.tagName("p");
		e.html("&nbsp;<p>stuff " + System.currentTimeMillis() + "</p>");
		HttpURLConnectionRunner.execPost( 
			Arrays.asList( tgt_node.split( "/jcr:content" ) ).get(0)		// node/attr path 
			, "jcr:content".concat( Arrays.asList( tgt_node.split( "/jcr:content" ) ).get(1) )
//			, false									// data, not site structure data
			, e
		);

	}
}