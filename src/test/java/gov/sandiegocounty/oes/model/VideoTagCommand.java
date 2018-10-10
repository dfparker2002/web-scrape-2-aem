/**
 * 
 */
package gov.sandiegocounty.oes.model;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 */
public class VideoTagCommand extends AbstractTagCommand implements ITagCommand {


	private static final Logger log = LoggerFactory.getLogger(VideoTagCommand.class);


	static int cnt = -1;

	public VideoTagCommand() {
/*
 *  - video
 *  	sling:resourceType=foundation/components/video
 *  	width=560
 *  	height"315
 *  	videoid=12cNBNKIXv8
 *  	<iframe width="560" height="315" allowfullscreen="allowfullscreen" 
 *  		allow="encrypted-media" gesture="media" frameborder="0" 
 *  			src="https://www.youtube.com/embed/12cNBNKIXv8?rel=0&amp;showinfo=0"></iframe>
 */
	}


	/**
	 * @param PATH. page path; e.g., /content/oes/en-us/donations
	 * @param element. Superset of data in content panel
	 */
	static public void cmd(final String PATH, Element x, int node_seed_idx ) throws Exception {


//		try {
/*
 * Uniquify video in node structure
 */
			final String COUNTER = VideoTagCommand.cnt < 0 ? "" : Integer.toString( VideoTagCommand.cnt );
System.err.println( " :: cnt " + VideoTagCommand.cnt );
			/* DO NORMAL TEXT PROCESSING

 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/oes-content/title
{
	"sling:resourceType":"oes/components/content/text"
}
 */
			//sling:resourceType=foundation/components/video
			//videoid=<data>

/*
 * assign sling:resourceType node definition :: foundation  
 */

		doStructureViaREST(
			PATH
			,CONTENT_TILE_SEED.concat(VIDEO)
				.concat(COUNTER).concat("/") // uniquify
				.concat(PROP_BASE_TYPE)
			, PROP_VIDEO
		);

/*
 * Metadata
 */
// height
		doStructureViaREST(
			PATH
			,CONTENT_TILE_SEED.concat(VIDEO)
			.concat(COUNTER).concat("/") // uniquify
			.concat("height")
			, x.hasAttr("height") ? x.attr("height") : "400"
		);
// width
		doStructureViaREST(
			PATH
			,CONTENT_TILE_SEED.concat(VIDEO)
			.concat(COUNTER).concat("/") // uniquify
			.concat("width")
			, x.hasAttr("width") ? x.attr("width") : "600"
		);
// videoid
		doStructureViaREST(
			PATH
			,CONTENT_TILE_SEED.concat(VIDEO)
				.concat(COUNTER).concat("/") // uniquify
				.concat("videoid")
			, StringUtils.substringsBetween(x.attr("src"), "embed/", "?")[0]
		);

/*
 * 2 add content attributes; e.g., 
 *  	sling:resourceType=oesshared/components/content/youtube
 *  	width=560
 *  	height"315
 *  	videoid=12cNBNKIXv8
 */



//	} catch (Exception e1) {
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