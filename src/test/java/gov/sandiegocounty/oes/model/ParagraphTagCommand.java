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
public class ParagraphTagCommand extends AbstractTagCommand implements ITagCommand {


	private static final Logger log = LoggerFactory.getLogger(ParagraphTagCommand.class);

	static int cnt = -1;

	public ParagraphTagCommand() {
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
			final String COUNTER = ParagraphTagCommand.cnt < 0 ? "" : Integer.toString(ParagraphTagCommand.cnt);
System.err.println( " ParagraphTagCommand :: cnt " + ParagraphTagCommand.cnt );

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
System.out.printf( " ParagraphTagCommand :: cmd  :: doStructureViaREST \n"
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

				doContentViaREST(
					PATH
					, CONTENT_TILE_SEED
						.concat("text")
						.concat(COUNTER).concat("/")// jcr:content/oes-content/text#/
						.concat( TEXT )				// jcr:content/oes-content/title/text=<CONTENT>
					, x								// CONTENT
				);

} // end check empty para constructions

//		} catch (Exception e1) {
//
//			e1.printStackTrace();
//		}
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