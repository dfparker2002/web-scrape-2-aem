/**
 * 
 */
package gov.sandiegocounty.oes.model;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.sandiegocounty.util.HttpURLConnectionRunner;


/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 */
public class HeadTagCommand extends AbstractTagCommand implements ITagCommand {


	private static final Logger log = LoggerFactory.getLogger(HeadTagCommand.class);

/* sling:resourceType */
//	final static Map<String,String> COMPONENT_LIB = new HashMap<>();

	final static String CONTENT_TILE_SEED = "jcr:content/oes-content/";
	final static String PAGE_CONTENT_SEED = "jcr:content/";

	static int cnt = -1;

	public HeadTagCommand() {
	}//div.page-copy > table.table

////	static final Element 
//	/**
//	 * @param file  E.g., C:\xampp\htdocs\www.sdcountyemergency.com\about\index.html
//	 * @param e
//	 * @param idx. Node uniquify seed
//	 * @return
//	 */
//	static public String processPage(String file_path, Elements e, int idx ) {
//
//
//		String result = null;
//
//
//
//System.out.printf( "###\nHeadTagCommand :: processPage : \nPATH %s\nidx %d\n###\n", file_path, idx );
//
//
//		List<String> specialNodes = Arrays.asList(new String[]
//			{"div.art-PostHeader","div.map-instructions"
//			,"div.xrm-attribute-value"	// remove,  div.art-PostHeader here
//			,"iframe" 					// video? @ donations
//			,"div.col-md-6" 			// 2 cols @ home
//			,"table.table" 				// table @ updates
//			} 
//		);
//
//
//		final String path_part = parsePathParticle(Paths.get(file_path).toFile());
//
///*
// * content
// */
////		HeadTagCommand.cnt = -1;
//			Element e0 = e.select("h1,h2,h3,h4").first();
//
//			switch (e0.tagName()) {
//
//			case "h1":
//			case "h2":
//			case "h3":
//			case "h4":
//
//				cmd(file_path, e0, idx);
//
//				break;
//
//			default:
//				break;
//
//			}
//
//		return result;
//	}


	/**
	 * @param PATH. page path; e.g., /content/oes/en-us/about
	 * @param element. Superset of data in content panel
	 * Usually page title are decorated with class=.art-PostHeader.. but not always
	 * Other kinds of H* are page elements and not page headers
	 */
	static public void cmd(final String PATH, Element x, int node_seed_idx ) {

// TO DO move this data to Abstract, interface or other boilerplate prototype
// sling:resourceType
//		COMPONENT_LIB.put( PROP_BASE_TYPE,	PROP_PARSYS );
//		COMPONENT_LIB.put( TEXT, 			PROP_TEXT );	// add [path]/text
//															// add [path]/textIsRich=true
//		COMPONENT_LIB.put( TABLE, 			PROP_TABLE ); 	// add [path]/tableData
//		COMPONENT_LIB.put( MAP, 		 	PROP_AS_IS );	// add [path]/asis
//		COMPONENT_LIB.put( TITLE, 			PROP_OES_TITLE );// add [path]/jcr:title

		try {

			final String COUNTER = HeadTagCommand.cnt < 0 ? "" : Integer.toString(node_seed_idx);
			HeadTagCommand.cnt++;
/*
 * Non-page header / only a page element
 */
			if (x.getElementsByAttributeValueMatching("class", "art-PostHeader").isEmpty()) {  // page data
/* DO NORMAL TEXT PROCESSING

 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/oes-content/title
{
	"sling:resourceType":"oes/components/content/text"
}
 */
//System.out.print( " HeadTagCommand :: cmd: " + COMPONENT_LIB);
//System.out.print( "\n	, headTagCommand TITLE: " + COMPONENT_LIB.get(TITLE) );
//System.out.println( "\n	, component: " + new Element( COMPONENT_LIB.get(TITLE) ));
/*
 * assign node definition :: text property 
 */

/*
 * NODE PATH + resourceType
// 0.a sling:resourceType = nt:resource
 * result: /content/ttc/en/jcr:content/content-column/sling:resourceType=foundation/components/parsys
*/
System.out.printf( " HeadTagCommand :: cmd  :: doStructureViaREST \n"
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

			doStructureViaREST(
				PATH
				,CONTENT_TILE_SEED.concat(TEXT)
					.concat(COUNTER).concat("/")
					.concat(PROP_BASE_TYPE)
				, PROP_TEXT
			);

//			HttpURLConnectionRunner.execPostStructure(
//				PATH								// /content/oes/en-us/test
//				, CONTENT_TILE_SEED.concat(TEXT)
//					.concat(COUNTER).concat("/") 	// jcr:content/oes-content/text#/
//					.concat(PROP_BASE_TYPE)			// sling:resourceType
//				, COMPONENT_LIB.get(TEXT)			// "foundation/components/text"  
//			);
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

//			HttpURLConnectionRunner.execPostStructure(
//				PATH									// /content/oes/en-us/test
//				, CONTENT_TILE_SEED.concat("text")
//					.concat(COUNTER).concat("/") 		// jcr:content/oes-content/text#/
//					.concat(PROP_TEXTISRICH)			// "textIsRich"
//				, "true"
//			);

//System.err.printf( "======== HeadTagCommand :: processPage \nPATH: %s\nATTR PATH %s,\nCOUNTER %s\n======"
//	, PATH
//	, CONTENT_TILE_SEED.concat("text/" )
//	, COUNTER
//);


/*
 * 2 add content attributes; e.g., 
 jcr:title = Articulos
 */
//System.err.println( " 1 HeadTagCommand :: HeadTagCommand :: cmd (header version) : elem html "
////		+ "{}", 
//		+ x.selectFirst("h1,h2,h3,h4") );
			doContentViaREST(
				PATH
				, CONTENT_TILE_SEED
					.concat("text")
					.concat(COUNTER).concat("/")// jcr:content/oes-content/text#/
					.concat( TEXT )				// jcr:content/oes-content/title/text=<CONTENT>
				, x.selectFirst("h1,h2,h3,h4")	// CONTENT
			);

//			HttpURLConnectionRunner.execPost(
//// CONTENT PATH: /content/oes/es-us/about
//// ATTR PATH:	jcr:content/oes-content/title/sling:resourceType
//// /content/oes
//
//				PATH					 		// /content/oes/es-us/about
//				, CONTENT_TILE_SEED
//					.concat("text")
//					.concat(COUNTER).concat("/")// jcr:content/oes-content/text#/
//					.concat( TEXT )				// jcr:content/oes-content/title/text=<CONTENT>
//				, x.selectFirst("h1,h2,h3,h4")	// CONTENT
//			);

// / "NORMAL" h* node


			} else { // page title

// page title /////............................
//doContentViaREST(
//		PATH
//		, CONTENT_TILE_SEED
//			.concat("text")
//	.concat(COUNTER).concat("/")// jcr:content/oes-content/text#/
//	.concat( TEXT )				// jcr:content/oes-content/title/text=<CONTENT>
//	, x.selectFirst("h1,h2,h3,h4")	// CONTENT
//);

doStructureViaREST(
		PATH
		,PAGE_CONTENT_SEED.concat(TITLE)
	, x.selectFirst("h1,h2,h3,h4").text()
);

///////////////////............................
/*
 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/oes-content/title
{
	"sling:resourceType":"oes/components/content/title"
}
 */
System.out.println( " 2 HeadTagCommand :: HeadTagCommand :: cmd \n-TEST DATA " + new Element( TITLE ));

			doStructureViaREST(
				PATH
				, CONTENT_TILE_SEED.concat("title/")	// jcr:content/oes-content/title/
				.concat(PROP_BASE_TYPE)					// sling:resourceType
			, PROP_OES_TITLE 							// oesshared/components/content/title  
			);

//			HttpURLConnectionRunner.execPostStructure(
//				PATH									// /content/oes/en-us/test
//				, CONTENT_TILE_SEED.concat("title/")	// jcr:content/oes-content/title/
//					.concat(PROP_BASE_TYPE)					// sling:resourceType
//				, COMPONENT_LIB.get(PROP_TITLE) 				// oesshared/components/content/title  
//			);

System.err.printf( " HeadTagCommand :: cmd PATH FILE \n%s\nattr path %s,\ncnt %d\n"
	, PATH
	, CONTENT_TILE_SEED.concat("title/" )
	, node_seed_idx
);


/*
 * 2 add content attributes; e.g., 
 jcr:title = Articulos
 */
log.debug( " HeadTagCommand :: HeadTagCommand :: cmd (header version) : elem html {}", x.selectFirst("h1,h2,h3,h4").html() );
			doContentViaREST(
				PATH					 		// /content/oes/es-us/about
				, CONTENT_TILE_SEED
					.concat("title")
					.concat(COUNTER.concat("/"))
					.concat( TITLE )			// jcr:content/oes-content/title/jcr:title=<CONTENT>
				, x.selectFirst("h1,h2,h3,h4")	// CONTENT
			);
//			HttpURLConnectionRunner.execPost(
//// CONTENT PATH: /content/oes/es-us/about
//// ATTR PATH:	jcr:content/oes-content/title/sling:resourceType
//// /content/oes
//
//				PATH					 		// /content/oes/es-us/about
//				, CONTENT_TILE_SEED
//					.concat("title")
//					.concat(COUNTER.concat("/"))
//					.concat( TITLE )			// jcr:content/oes-content/title/jcr:title=<CONTENT>
//				, x.selectFirst("h1,h2,h3,h4")	// CONTENT
//			);

			}// end if check for page data

		} catch (Exception e1) {

			e1.printStackTrace();
		}
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