/**
 * 
 */
package gov.sandiegocounty.oes.model;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.sandiegocounty.util.HttpURLConnectionRunner;


/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 */
public class ParagraphUpdateSectionTagCommand extends AbstractTagCommand implements ITagCommand {


	private static final Logger log = LoggerFactory.getLogger(ParagraphUpdateSectionTagCommand.class);

	static int cnt = -1;

	public ParagraphUpdateSectionTagCommand() {
	}//div.page-copy > table.table



	/**
	 * @param PATH. page path; e.g., /content/oes/en-us/about
	 * @param element. Superset of data in content panel
	 * Usually page title are decorated with class=.art-PostHeader.. but not always
	 * Other kinds of H* are page elements and not page headers
	 */
	static public void cmd(final String PATH, Element x, int node_seed_idx ) throws Exception {


//		try {


if( !StringUtils.isEmpty(x.html() ) ) { // end check empty para constructions
/*
 * Uniquify paragraph in node structure
 */
			final String COUNTER = ParagraphUpdateSectionTagCommand.cnt < 0 ? "" : Integer.toString(ParagraphUpdateSectionTagCommand.cnt);
System.err.println( " ParagraphUpdateSectionTagCommand :: cnt " + ParagraphUpdateSectionTagCommand.cnt );

			/* DO NORMAL TEXT PROCESSING

 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/oes-content/title
{
	"sling:resourceType":"oes/components/content/text"
}
 */

/*
 * assign sling:resourceType node definition :: para/text property 
 */

/*
 * NODE PATH + resourceType
// 0.a sling:resourceType = nt:resource
 * result: /content/ttc/en/jcr:content/content-column/sling:resourceType=foundation/components/parsys
*/
System.out.printf( " ParagraphUpdateSectionTagCommand :: cmd  :: doStructureViaREST \n"
	+ "INPUT\n"
	+ "- PATH %s\n"
	+ "- ATTR_PATH %s\n"
	+ "- data %s\n" 
	+ "- COUNTER %s\n", 
		PATH
		, CONTENT_TILE_SEED.concat(TEXT)
			.concat(COUNTER).concat("/")
			.concat(PROP_BASE_TYPE)
			, PROP_TEXT
		, COUNTER
);
Element datetime = x.getAllElements().first();
Element asl_link = x.getAllElements().last();
//x.getAllElements().remove(datetime);
//x.getAllElements().remove(asl_link);
Elements remainder = x.getAllElements();
remainder.remove(datetime);
remainder.remove(asl_link);

// Extract, excise date (first p)
	doDateTimeProperty(PATH, asl_link);
// process remainder of p
	doNodesCreation(PATH, remainder, COUNTER);
//// Extract, excise asl link (last div)
////	doAslLinkNodeCreation(PATH, x, COUNTER);
//	doAddStringProperty(PATH, asl_link, COUNTER);



} // end check empty para constructions

//		} catch (Exception e1) {
//
//			e1.printStackTrace();
//		}
	}


	/**
	 * @param PATH
	 * @param x
	 * @param COUNTER
	 */
	private static void doDateTimeProperty(final String PATH, Element e) {
/*
 * MODEL
 * 
	./sling:resourceType: oesshared/components/content/incident-item
	./jcr:lastModified: 
	./jcr:lastModifiedBy: 
	_charset_: utf-8
	:status: browser
	./datetime: 2018-10-13T09:34:00.000-07:00
	./datetime@TypeHint: Date
	./title: tghjk
	./emergency-message-copy:
				<ul>
				<li>hhhhhhhhhhhh</li>
				</ul>
	./asl-link: https://duckduckgo.com/
	./newsLink: /content/oesemergency/en-us/updates
	./type: news
	./mode: item
 */
		/*
		 * Structure should already by in place
		 * 
		 * 2 add content attributes; e.g., 
 			p = Articulos ...
		 */
		
		doAddDateTimeProperty(
			PATH
			, CONTENT_TILE_SEED
			.concat(INCIDENT_ITEM).concat("/")	// jcr:content/oes-content/text#/
			, e									// CONTENT
		);
	}

//	private static void doDatePropertViaREST(String pATH, String concat, Element e) {
//		// TODO Auto-generated method stub
//		
//	}

//
//
//	/**
//	 * @param PATH
//	 * @param e
//	 * @param COUNTER
//	 */
//	private static void doAddStringProperty(final String PATH, Element e, final String COUNTER) {
//
///*
// * Structure should already by in place
// * 
// * 2 add content attributes; e.g., 
// 	p = Articulos ...
// */
//		doStringPropertyViaREST(
//			PATH
//			, CONTENT_TILE_SEED
//				.concat("text")
//				.concat(COUNTER).concat("/")// jcr:content/oes-content/text#/
//				.concat( e.text() )		// jcr:content/oes-content/title/text=<CONTENT>
//			, e								// CONTENT
//		);
//	}


	/**
	 * @param PATH
	 * @param x
	 * @param COUNTER
	 */
	private static void doNodesCreation(final String PATH, Elements ee, final String COUNTER) {

		doStructureViaREST(
			PATH
			,CONTENT_TILE_SEED.concat(TEXT)
				.concat(COUNTER).concat("/")
				.concat(PROP_BASE_TYPE)
			, PROP_TEXT
		);

/*
 * assign node definition :: textIsRich attribute
 */
		doStructureViaREST(
			PATH
			,CONTENT_TILE_SEED.concat(TEXT)
				.concat(COUNTER).concat("/") 		// jcr:content/oes-content/text#/
				.concat(PROP_TEXTISRICH)			// "textIsRich"
			, "true"
		);


/*
 * 2 add content attributes; e.g., 
 p = Articulos ...
 */

		doBulkContentViaREST(
			PATH
			, CONTENT_TILE_SEED
				.concat("text")
				.concat(COUNTER).concat("/")// jcr:content/oes-content/text#/
				.concat( TEXT )				// jcr:content/oes-content/title/text=<CONTENT>
			, ee								// CONTENT
		);
	}


}
/*
/content/oes/
div.col-sm-9 > div.col-md-12 -> jcr:content/par#
	sling:resourceType = foundation/components/text, textIsRich=true

div.col-sm-9 > div.xrm-editable-html.xrm-attribute > div.xrm-attribute-value -> jcr:content/par#
	sling:resourceType = foundation/components/text, textIsRich=true

div.col-sm-9 > div.col-md-6:nth-child(1) -> jcr:content/content-columns/column_flex/flex_col_1c_0/
	sling:resourceType = foundation/components/parsys
		sling:resourceType = foundation/components/text, textIsRich=true

div.col-sm-9 > div.col-md-6:nth-child(2) -> jcr:content/content-columns/column_flex/flex_col_1c_1/
	sling:resourceType = foundation/components/parsys
		sling:resourceType = foundation/components/text, textIsRich=true

*/