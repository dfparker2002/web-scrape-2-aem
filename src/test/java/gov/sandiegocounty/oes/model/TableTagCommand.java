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
public class TableTagCommand extends AbstractTagCommand implements ITagCommand {


	private static final Logger log = LoggerFactory.getLogger(TableTagCommand.class);


	static int cnt = -1;

	public TableTagCommand() {
//sling:resourceType=foundation/components/table
//tableData=<data>
	}


	/**
	 * @param PATH. page path; e.g., /content/oes/en-us/updates
	 * @param element. Superset of data in content panel
	 */
	static public void cmd(final String PATH, Element x, int node_seed_idx ) throws Exception {


//		try {

/*
 * Uniquify table in node structure
 */
			final String COUNTER = TableTagCommand.cnt < 0 ? "" : Integer.toString( TableTagCommand.cnt );
System.err.println( " :: cnt " + TableTagCommand.cnt );
			/* DO NORMAL TEXT PROCESSING

 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/oes-content/title
{
	"sling:resourceType":"oes/components/content/text"
}
 */
			//sling:resourceType=foundation/components/table
			//tableData=<data>

/*
 * assign sling:resourceType node definition :: foundation table 
 */

		doStructureViaREST(
			PATH
			,CONTENT_TILE_SEED.concat(TABLE)
				.concat(COUNTER).concat("/") // uniquify
				.concat(PROP_BASE_TYPE)
			, PROP_TABLE
		);

/*
 * 2 add content attributes; e.g., 
 tabledata=<table class="table table-striped news-list">...
 */

			doContentViaREST(
				PATH
				, CONTENT_TILE_SEED.concat(TABLE)
					.concat(COUNTER).concat("/")// jcr:content/oes-content/table
					.concat( "tableData" )	// jcr:content/oes-content/table/tableData=<CONTENT>
				, x							// CONTENT
			);


//		} catch (Exception e1) {
//		
//				e1.printStackTrace();
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