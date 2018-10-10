/**
 * 
 */
package gov.sandiegocounty.oes.model;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 */
public class ColumnTagCommand extends AbstractTagCommand implements ITagCommand {


	private static final Logger log = LoggerFactory.getLogger(ColumnTagCommand.class);


	static int cnt = -1;

	public ColumnTagCommand() {
	}//div.page-copy > table.table



	/**
	 * @param PATH. page path; e.g., /content/oes/en-us
	 * @param element. Superset of data in content panel
	 * Usually page title are decorated with class=.art-PostHeader.. but not always
	 * Other kinds of H* are page elements and not page headers
	 */
	static public void cmd(final String PATH, Element x, int node_seed_idx ) throws Exception {

//		try {


			final String COUNTER = ColumnTagCommand.cnt < 0 ? "" : Integer.toString(ColumnTagCommand.cnt);

/* DO NORMAL TEXT PROCESSING

 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/oes-content/column-flex
{
	"sling:resourceType":"oesshared/components/content/column-flex"
}
 */

/*System.out.printf( " ColumnTagCommand :: cmd  :: doStructureViaREST \n"
	+ "INPUT\n"
	+ "- PATH %s\n"
	+ "- ATTR_PATH %s\n"
	+ "- data %s\n", 
		PATH
		, CONTENT_TILE_SEED.concat(TEXT)
			.concat(COUNTER).concat("/")
			.concat(PROP_BASE_TYPE)
		, PROP_TEXT
);
*/
/*
 * assign node definition :: column property 
 */
			doStructureViaREST(
				PATH
				,CONTENT_TILE_SEED.concat(COLUMN)
					.concat(COUNTER).concat("/")
					.concat(PROP_BASE_TYPE)
				, PROP_COLUMN
			);

/*
 * assign node definition :: textIsRich attribute
 */

			final int col_cnt = Integer.parseInt( x.attr("class"), 10);
System.out.printf( " $$$$ col_cnt = %d", col_cnt );

			String cols = "6,6";
			if(col_cnt == 8) {
				cols = "8,4";
			} else if(col_cnt == 6) {
				cols = "6,6";
			} else if(col_cnt == 4) {
				cols = "4,8";
			} else if(col_cnt == 3) {
				cols = "3,9";
			}
			doStructureViaREST(
				PATH
				,CONTENT_TILE_SEED.concat(COUNTER)
					.concat(COUNTER).concat("/") 		// jcr:content/oes-content/text#/
					.concat(PROP_NUM_COL)				// "num_col"
				, cols
			);


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