/**
 * 
 */
package gov.sandiegocounty.oes.model;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

import gov.sandiegocounty.util.HttpURLConnectionRunner;

/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 */
public class AbstractTagCommand {

	
	final static String CONTENT_TILE_SEED = "jcr:content/oes-content/";
	final static String PAGE_CONTENT_SEED = "jcr:content/";

	final static Map<String,String> COMPONENT_LIB = new HashMap<>();
	final static String MAP = "map";; 			// increment
	final static String TABLE = "table"; 		// increment
	final static String TEXT = "text";	 		// increment
	final static String COLUMN = "column_flex"; // increment
	final static String TITLE = "jcr:title";	// increment
	final static String VIDEO = "table"; 		// increment
//	private static final String FLEX_COL_COUNT = null;

	final static String PROP_BASE_TYPE = 	"sling:resourceType";
	final static String PROP_TITLE = 		"jcr:title";
	final static String PROP_TEXTISRICH = 	"textIsRich";
	final static String PROP_NUM_COL = 		"num_col";

	final static String PROP_TEXT = 		"foundation/components/text";
	final static String PROP_TABLE = 		"foundation/components/table";
	final static String PROP_VIDEO = 		"foundation/components/video";
	final static String PROP_OES_TITLE =	"foundation/components/title";
	final static String PROP_COLUMN = 		"oesshared/components/content/column_flex";
	final static String PROP_PARSYS = 		"foundation/components/parsys";
	final static String PROP_IPARSYS = 		"foundation/components/iparsys";


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

System.out.printf( "\n AbstractTagCommand :: doContentViaREST : persist content \n"
	+ "INPUT\n"
	+ "- PATH %s\n"
	+ "- ATTR_PATH %s\n"
	+ "- data %s\n", 
		PATH
		, CONTENT_PATH
		, StringUtils.substring(elem.outerHtml(), 0, 25)
);
		try {

			HttpURLConnectionRunner.execPost(
				PATH 			// /content/oes/en-us/test
				, CONTENT_PATH
				, elem 			// [text] (property value)
			);

		} catch (Exception e) {

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

			e.printStackTrace();
		}
	}

}
