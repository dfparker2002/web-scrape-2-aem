/**
 * 
 */
package gov.sandiegocounty.oes.model;

import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;

import org.apache.commons.lang3.StringUtils;
import org.apache.jackrabbit.commons.JcrUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;

import gov.sandiegocounty.util.HttpURLConnectionRunner;
import gov.sandiegocounty.util.JavaCalendarConverter;

/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 */
public class AbstractTagCommand {


	private static final Logger log = LoggerFactory.getLogger(AbstractTagCommand.class);


	final static String PAGE_CONTENT_SEED = JcrConstants.JCR_CONTENT.concat( "/");
	final static String CONTENT_TILE_SEED = PAGE_CONTENT_SEED.concat( "oes-content/");

	final static Map<String,String> COMPONENT_LIB = new HashMap<>();
	final static String MAP = "map";; 			// increment
	final static String TABLE = "table"; 		// increment
	final static String TEXT = "text";	 		// increment
	final static String COLUMN = "column_flex"; // increment
	final static String TITLE = "jcr:title";	// increment
	final static String VIDEO = "video"; 		// increment
	final static String INCIDENT_ITEM = "incident_item";// scalar
//	private static final String FLEX_COL_COUNT = null;

	final static String PROP_BASE_TYPE = 	"sling:resourceType";
	final static String PROP_TITLE = 		"jcr:title";
	final static String PROP_TEXTISRICH = 	"textIsRich";
	final static String PROP_NUM_COL = 		"num_col";

	final static String PROP_TEXT = 		"foundation/components/text";
	final static String PROP_TABLE = 		"foundation/components/table";
	final static String PROP_VIDEO = 		"oesshared/components/content/youtube";
	final static String PROP_OES_TITLE =	"foundation/components/title";
	final static String PROP_COLUMN = 		"oesshared/components/content/column-flex";
	final static String PROP_PARSYS = 		"foundation/components/parsys";
	final static String PROP_IPARSYS = 		"foundation/components/iparsys";
	final static String PROP_INCIDENT_ITEM ="oesshared/components/content/incident-item";


///**
// * 
// * Persist content & content attributes into node path
// * 
// * @param next_elem
// * @param PATH
// * @param elem_idx
// * @param CONTENT_PATH
// * @throws Exception
// * 
// */
//	static void doDatePropertyViaREST( final String PATH, String CONTENT_PATH, final Element elem ) {
//
//		try {
//
//			HttpURLConnectionRunner.execPost(
//				PATH 			// /content/oes/en-us/test
//				, CONTENT_PATH
//				, elem 			// [text] (property value)
//			);
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//	}

/**
 * 
 * Persist content & content attributes into node path
 * 
 * @param next_elem
 * @param PATH
 * @param elem_idx
 * @param CONTENT_PATH
 * @throws Exception
 * 
 */
	static void doContentViaREST( final String PATH, String CONTENT_PATH, final Element elem ) {
System.out.println( " <<< AbstractTagCommand :: doContentViaREST " + elem.outerHtml());
	System.out.printf( "\n AbstractTagCommand :: doContentViaREST : persist content \n"
		+ "INPUT\n"
		+ "- PATH %s\n"
		+ "- ATTR_PATH %s\n"
		+ "- data %s\n", 
			PATH
			, CONTENT_PATH
			, StringUtils.substring(elem.outerHtml(), 0, Math.min(25, elem.outerHtml().length()))
	);
			try {

				HttpURLConnectionRunner.execPost(
					PATH 				// /content/oes/en-us/test
					, CONTENT_PATH
					, elem				// [text] (property value)
//					, new Element(elem.outerHtml())		// [text] (property value)
				);

			} catch (Exception e) {

				e.printStackTrace();
			}
		}

/**
 * 
 * Persist content & content attributes into node path
 * 
 * @param PATH
 * @param elem_idx
 * @param CONTENT_PATH
 * @param nextelems
 * @throws Exception
 * 
 */
	static void doBulkContentViaREST( final String PATH, String CONTENT_PATH, final Elements elems ) {

System.out.printf( "\n AbstractTagCommand :: doContentViaREST : persist content \n"
	+ "INPUT\n"
	+ "- PATH %s\n"
	+ "- ATTR_PATH %s\n"
	+ "- data %s\n", 
	PATH
	, CONTENT_PATH
	, StringUtils.substring(elems.outerHtml(), 0, Math.min(25, elems.outerHtml().length()))
);
		try {

			HttpURLConnectionRunner.execPost(
				PATH 											// /content/oes/en-us/test
				, CONTENT_PATH
				, new Element("span").html(elems.outerHtml())	// [text] (property value)
			);

		} catch (Exception e) {
log.error( " :: doBulkContentViaREST : failed to persist normal content : {}", e.getMessage());
			e.printStackTrace();
		}
	}

/**
 * Persist node path & path attributes
 * 
 * @param PATH
 * @param CONTENT_PATH
 */
	static void doStructureViaREST( final String PATH, final String CONTENT_PATH, final String CONTENT ) {

		try {
/*
System.out.printf( "\n AbstractTagCommand :: doStructureViaREST  :: execPostStructure \n"
	+ "INPUT\n"
	+ "- PATH %s\n"
	+ "- ATTR_PATH %s\n"
	+ "- data %s\n", 
		PATH
		, CONTENT_PATH
		, CONTENT
);
*/
			HttpURLConnectionRunner.execPostStructure(
				PATH          					// /content/oes/en-us/test
				, CONTENT_PATH					// jcr:content/oes-content/column/column_flex
												// sling:resourceType			(property key)
				, StringUtils.chomp(CONTENT)	// foundation/components/parsys (property value)
			);

		} catch (Exception e) {

log.error( " :: doStructureViaREST : failed to persist node structure: {}", e.getMessage());
			e.printStackTrace();
		}
	}

////////////////////
/**
 * Persist node path & path attributes
 * 
 * @param PATH
 * @param CONTENT_PATH
 */
	static void doContentStringViaREST( final String PATH, String CONTENT_PATH, final String s ) {

		try {

			HttpURLConnectionRunner.execPostStructure(
				PATH					// /content/oes/en-us/test
				, CONTENT_PATH			// jcr:content/oes-content/column/column_flex
										// sling:resourceType			(property key)
				, StringUtils.chomp(s)	// foundation/components/parsys (property value)
			);

		} catch (Exception e) {

log.error( " :: doContentStringViaREST : failed to persist node structure: {}", e.getMessage());
			e.printStackTrace();
		}
	}

////////////////////
	/**
	 * @param PATH
	 * @param CONTENT_PATH
	 * @param CONTENT
	 * 
	 * src: 
	 * https://stackoverflow.com/questions/39341286/accessing-cq-aem-repository-from-external-standalone-application
	 * https://mvnrepository.com/artifact/org.apache.jackrabbit/jackrabbit-jcr2spi
	 */
	static void saveDateNodeProperty(final String PATH, final String CONTENT_PATH, final Calendar CONTENT) {

		try {

// Create a connection to the CQ repository running on local host
			Repository repository = JcrUtils.getRepository("http://192.168.200.87:4502/crx/server");

// Create a Session
			javax.jcr.Session session = repository.login(new SimpleCredentials("admin", "admin".toCharArray()));

// Create a node that represents the root node
			Node root = session.getRootNode();

// Store content
System.out.println( " AbstractTagCommand :: saveDateNodeProperty PATH " + StringUtils.removeStart( PATH, "/" ).concat("/").concat(CONTENT_PATH));
System.out.println( " AbstractTagCommand :: saveDateNodeProperty DATE " + CONTENT );

			Node adobe = root.getNode(StringUtils.removeStart( PATH, "/" ).concat("/").concat(CONTENT_PATH));

			adobe.setProperty("datetime", CONTENT);

// Retrieve content
System.out.println(adobe.getProperty("datetime").getString());

// Save the session changes and log outX
			session.save();
			session.logout();

		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}
/////////////////////////////
/**
 * @param PATH
 * @param x
 * @param COUNTER
 * @throws ParseException 
 */
	static void doAddDateTimeProperty(final String PATH
		, final String CONTENT_PATH, Element e ) throws ParseException {

		saveDateNodeProperty(
			PATH
			, CONTENT_PATH
//			, StringUtils.startsWith(CONTENT_PATH, "/") ? StringUtils.removeStart(CONTENT_PATH, "/") : CONTENT_PATH
			, JavaCalendarConverter.stringToSimpleDate(e.text(), JavaCalendarConverter.asp_pattern) 
		);
///*
//* MODEL
//* 
//			./sling:resourceType: oesshared/components/content/incident-item
//			./jcr:lastModified: 
//			./jcr:lastModifiedBy: 
//			_charset_: utf-8
//			:status: browser
//			./datetime: 2018-10-13T09:34:00.000-07:00
//			./datetime@TypeHint: Date
//			./title: tghjk
//			./emergency-message-copy:
//						<ul>
//						<li>hhhhhhhhhhhh</li>
//						</ul>
//			./asl-link: https://duckduckgo.com/
//			./newsLink: /content/oesemergency/en-us/updates
//			./type: news
//			./mode: item
//*/
///*
// * Structure should already by in place
// * 
// * 2 add content attributes; e.g., 
// * ./datetime: 2018-10-13T09:34:00.000-07:00
// * ./datetime@TypeHint: Date
// */
//		Map<String, String> m = new HashMap<>();
//		m.put("datetime", e.text() );
//		m.put("datetime@TypeHint", "Date" );
//
//		try {
//
//			HttpURLConnectionRunner.execPostComplexData(
//				PATH
//				, CONTENT_PATH	// jcr:content/oes-content/text#/
//				, m				// CONTENT
//			);
//
//		} catch (Exception e1) {
//
//log.error( " :: doAddDateTimeProperty : failed to persist complex content : {}", e1.getMessage());
//			e1.printStackTrace();
//		}
//
//		m = null;
	}
/////////////////////////////
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AbstractTagCommand o = new AbstractTagCommand();

		try {
/*
 * Node must be extant
 */
			o.saveDateNodeProperty(
				"content/oesemergency/en-us/updates/active-shooter-at-san-diego-rockn-roll-marathon-060318-1325"
//				"/content/oesemergency/en-us/updates/american-red-cross-opens-emergency-shelter-070618-1435"
				, "jcr:content/oes-content/incident_item"
				, JavaCalendarConverter.stringToSimpleDate(
					"10/10/2018 12:14:00 PM PDT"
					, JavaCalendarConverter.asp_pattern)
			);

		} catch (ParseException e) {

			e.printStackTrace();
		}
	}
}
/*
		final Calendar c = JavaCalendarConverter.stringToCalendar("2018-10-13T09:34:00.000-07:00", pattern);
		System.out.println( " JavaCalendarConverter :: main " + c);
		System.out.println( " JavaCalendarConverter :: main " + JavaCalendarConverter.calendarToString(c, pattern));)

*/