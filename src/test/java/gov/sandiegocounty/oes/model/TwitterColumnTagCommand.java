/**
 * 
 */
package gov.sandiegocounty.oes.model;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.sandiegocounty.util.HttpURLConnectionRunner;


/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 */
public class TwitterColumnTagCommand extends AbstractTagCommand implements ITagCommand {

	
	private static final Logger log = LoggerFactory.getLogger(TwitterColumnTagCommand.class);

/*
 * TODO move this to Abstract constructor
 */
/* sling:resourceType */
	final static Map<String,String> COMPONENT_LIB = new HashMap<>();
	final static String MAP = "map";; 			// increment
	final static String TABLE = "table";; 		// increment
	final static String TEXT = "text";; 		// increment
	final static String COLUMN = "column_flex"; // increment
	final static String TITLE = "jcr:title";	// increment
//	private static final String FLEX_COL_COUNT = null;

	final static String PROP_BASE_TYPE = 	"sling:resourceType";
	final static String PROP_TITLE = 		"jcr:title";
	final static String PROP_TEXTISRICH = 	"textIsRich";
	final static String PROP_NUM_COL = 		"num_col";

	final static String PROP_TEXT = 		"foundation/components/text";
	final static String PROP_OES_TITLE =	"oesshared/components/content/title";
	final static String PROP_COLUMN = 		"oesshared/components/content/column_flex";
	final static String PROP_PARSYS = 		"foundation/components/parsys";
	final static String PROP_IPARSYS = 		"foundation/components/iparsys";
	final static String PROP_AS_IS = 		"oesshared/components/content/asis";
	final static String PROP_TABLE = 		"foundation/components/table";

	final static String CONTENT_TILE_SEED = "jcr:content/oes-content/";


	public TwitterColumnTagCommand() {}//div.page-copy > table.table

//	/**
//	 * @param file_path  E.g., C:\xampp\htdocs\www.sdcountyemergency.com\about\index.html
//	 * @param e
//	 * @return
//	 */
//	static public String processPage(String file_path, org.jsoup.select.Elements e, int idx) {
//
////System.out.printf( " MapTagCommand :: processPage : file %s elems %s", file.getName(), e );
////System.out.println( " MapTagCommand :: processPage : parsePathParticle " + 
////		parsePathParticle(file)
////);
//
//		String result = null;
////		final String PATH = parsePathParticle(file_path);
//
//
//System.out.printf( " MapTagCommand :: processPage : PATH %s", file_path );
//
//
//		List<String> specialNodes = Arrays.asList(new String[]
//			{"div.art-PostHeader"
//			,"div.map-instructions"
//			,"div.xrm-attribute-value"	// remove,  div.art-PostHeader here
//			,"iframe" 					// video? @ donations
//			,"div.col-md-6" 			// 2 cols @ e.g., home
//			,"table.table" 				// table @ updates
//			} 
//		);
//
//
//			Elements e0 = e.select("div.col-sm-9");
//
//
//				final String path_part = parsePathParticle( Paths.get(file_path).toFile() );
//
///*
// * content
// */
//				TwitterColumnTagCommand.cnt = -1;
//				e0.forEach(x -> {
//	
//					switch (x.tagName()) {
//
//					case "div.col-md-6":
//
//						TwitterColumnTagCommand.columnTagCommand(file_path, x );
//
//						break;
//
//					default:
//						break;
//					
//					}
//				}); // /for-each
//
//		return result;
//	}



/**
 * @param PATH. page path; e.g., /content/oes/en-us/about
 * @param element. Superset of data in content panel
 * Notes
 * 	Assume element passed into logic is div.col-md-6
 */
public 	static void cmd( final String PATH, Element elem, int elem_idx ) {

/*
 * map div.oes-box > h3.oes-box-title >
 * div.map-instructions
 */

		try {
/*
 * reject non columnar data
 */
			if( !elem.hasClass( "col-md-6" )) return;
/*
 * reject non twitter data
 */
	    	if( elem.select("h3.oes-box-title:contains(Twitter)").isEmpty()) return;

			final String COUNTER = PageObj.row_cnt < 0 ? "" : "_".concat( Integer.toString(PageObj.row_cnt) );

/*
 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test

** 0	AEM									ORIG WEB
"content-columns": {						row
** 1
	"column_flex": {						col-md-12
	    "num_col": "6,6",
	    "sling:resourceType": "ttcsd_2018/components/content/column_flex",
 ** 2.a										col-md-6
    	"flex_col_1c_1":
		    {
		        "sling:resourceType": "foundation/components/parsys",
		        "video_link": {}
		    },
 ** 2.b
	    "flex_col_1c_0": {					col-md-6
	      ":jcr:primaryType": "Name",
	      "jcr:primaryType": "nt:unstructured",
	      "sling:resourceType": "foundation/components/parsys",
	      "text": {
	        "text": "<p>Each year, our office returns thousands of dollars in unclaimed money to its rightful owners. We have five lists you can search: unclaimed property tax refunds, countywide unclaimed money, abandoned properties, unclaimed estates with heirs, and unclaimed estates without heirs. Select a list to search it. If you find your name, follow the instructions below it to claim your money.</p>\n",
	        "sling:resourceType": "foundation/components/text",
	        "textIsRich": "true"
	      }
	    }
	}
}
 */

/*
 * Build collection of paths & values
 */
    	final Elements E = elem.parent().select("h3.oes-box-title:contains(Twitter)");
    	IntStream.range(0, E.size()  ).forEach( idx -> {

// OK System.out.printf("put a twit e.%d = %s, sum %d ", idx, E.get(idx), (PageObj.PageCollection.size() ) );

			PageObj.PageCollection.put( 
    			String.format( "column_flex_%d/flex_col_1c_%d"
    				, idx
    				, (PageObj.PageCollection.size() )
    			)
    			, E.get(idx)
    		);
    	});
System.out.printf( "\n====\n ** TwitterColumnTagCommand :: cmd \n- PageObj.PageCollection %s\n- count %d"
	, PageObj.PageCollection.toString().join(",", "\n,")
	, PageObj.PageCollection.size()
);
/* -=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
column_flex : {
  "jcr:primaryType": "nt:unstructured",
  "num_col": "6,6",
  "sling:resourceType": "oesshared/components/content/column_flex",
  "flex_col_1c_0": {
    "jcr:primaryType": "nt:unstructured",
    "sling:resourceType": "foundation/components/parsys",
  },
  "flex_col_1c_1": {
    "jcr:primaryType": "nt:unstructured",
    "sling:resourceType": "foundation/components/parsys",
  }
}
 */
// 1 build a PROP_COLUMN / "oesshared/components/content/column_flex"
// 2 add a 6,6 attr
// 3 @ column_flex

  	  final String CONTENT_PATH = CONTENT_TILE_SEED.concat( COLUMN ).concat(COUNTER);
  	  final String[] pathKeys = PageObj.PageCollection.keySet().toArray(new String[PageObj.PageCollection.size()]);
System.out.printf( "\n====\n $$ TwitterColumnTagCommand :: cmd \n- CONTENT_PATH %s \n- pathKeys%s"
		, CONTENT_PATH
		, Arrays.asList(pathKeys)
);

///*
// * TODO loop PageObj.PageCollection map and build structure, content 
// */
//      IntStream.range(0, PageObj.PageCollection.size()  ).forEach( idx -> { // https://stackoverflow.com/questions/22793006/java-8-foreach-with-index
//
//    	  String CONTENT_PATH = CONTENT_TILE_SEED.concat( "content-column" )
//			.concat(COUNTER);
//
////System.out.println("\n=============");
////System.out.println("seed path " + PATH.concat(" - ").concat(CONTENT_PATH));
////System.out.println("Twitters " + E);
////System.out.println("PageObj.PageCollection " + PageObj.PageCollection);
//      	final String[] pathKeys = PageObj.PageCollection.keySet().toArray(new String[PageObj.PageCollection.size()]);
////System.out.println("pathKeys " + Arrays.asList(pathKeys));
////System.out.println("=============");
//
///*
// * NODE PATH + resourceType
//// 0.a sling:resourceType = nt:resource
// * result: /content/ttc/en/jcr:content/content-column/sling:resourceType=foundation/components/parsys
//*/
//			doStructureViaREST(
//				PATH
//				, CONTENT_PATH.concat("/" )
//					.concat(PROP_NUM_COL)
//				, PROP_COLUMN
//			);
//
/////////////////////
///*
//* NODE PATH + resourceType
//* COLUMN
////1.a sling:resourceType = ttcsd_2018/components/content/column_flex
// * result: /content/ttc/en/jcr:content/content-column/sling:resourceType=foundation/components/parsys
//*/
///*
// * TODO loop PageObj.PageCollection map and build structure, content 
// */
////System.err.printf( " PROB HERE - TwitterColumnTagCommand :: cmd : "
////	+ "\n- CONTENT_PATH %s"
////	+ "\n- pathKeys %s"
////	, CONTENT_PATH
////	, Arrays.asList(pathKeys) );
//
//		CONTENT_PATH = CONTENT_PATH.concat("/").concat(pathKeys[idx].split("/")[0]).concat("/");
////		CONTENT_PATH = CONTENT_PATH.concat(COLUMN).concat("/");
//	
////System.err.printf( "CONTENT_PATH %s\ndata = %s", CONTENT_PATH, PROP_COLUMN );
//	
//		doStructureViaREST(PATH, CONTENT_PATH.concat(PROP_NUM_COL), PROP_COLUMN);
//	
///*
// * container for divs
//* 1.b add content attributes; e.g., 
//		num_col=6,6
//*/
////    		CONTENT_PATH = CONTENT_PATH.concat(COLUMN);
//		doStructureViaREST(PATH, CONTENT_PATH.concat(PROP_NUM_COL), "6,6");
//
///////////////////
///* NODE PATH + resourceType
// * 0.a sling:resourceType = ttcsd_2018/components/content/column_flex
//*/
//		doStructureViaREST(PATH, CONTENT_PATH, PROP_PARSYS);
//
//
///*
// * TODO loop PageObj.PageCollection map and build structure, content 
// */
//System.out.println( " === START COL LOOP ====" );
//int COL_COUNT = 0;
//System.err.println(PageObj.PageCollection);
//for (Map.Entry<String, Element> entry : PageObj.PageCollection.entrySet()) {
////System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
//final String FLEX_COL_COUNT = Integer.toString(COL_COUNT);
//System.out.printf( " -- TwitterColumnTagCommand :: cmd starter CONTENT_PATH %s", CONTENT_PATH);
//System.out.printf( "\n 0 TwitterColumnTagCommand :: doStructureViaREST  :: execPostStructure \n"
//	+ "INPUT\n"
//	+ "- PATH %s\n"
//	+ "- ATTR_PATH %s\n"
//	+ "- data %s\n" 
//		,PATH
//		,CONTENT_PATH.concat(FLEX_COL_COUNT )
//			.concat("/" )				// jcr:content/oes-content/content-column#/
//			.concat("flex_col_1c_").concat(FLEX_COL_COUNT ).concat("/") 
//			.concat(PROP_BASE_TYPE)     // jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/sling:resourceType
//		,PROP_PARSYS 
//);
//
//	doStructureViaREST( 
//		PATH 
//		,CONTENT_PATH.concat(FLEX_COL_COUNT )
//			.concat("/" )				// jcr:content/oes-content/content-column#/
//			.concat("flex_col_1c_").concat(FLEX_COL_COUNT ).concat("/") 
//			.concat("/")
//			.concat(PROP_BASE_TYPE)		// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/sling:resourceType
//		, PROP_PARSYS
//	);
//
///*
// * 1.d 
// text=<column text>
// */
//	doContentViaREST( 
//		PATH 
//		,CONTENT_PATH.concat(FLEX_COL_COUNT )
//			.concat("/" )				// jcr:content/oes-content/content-column#/
//			.concat("flex_col_1c_").concat(FLEX_COL_COUNT ).concat("/") 
//			.concat("/")
//			.concat(TEXT)				// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
//			.concat("/").concat(TEXT)	// "text={content}
//		,entry.getValue()
//	);
//
///*
// * textIsRich attribute
// */
//	doStructureViaREST( 
//		PATH 
//		,CONTENT_PATH.concat(FLEX_COL_COUNT )
//			.concat("/" )				// jcr:content/oes-content/content-column#/
//			.concat("flex_col_1c_").concat(FLEX_COL_COUNT ).concat("/") 
//			.concat(FLEX_COL_COUNT ).concat("/")
//			.concat(TEXT).concat("/")	// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
//			.concat(PROP_TEXTISRICH)	// "textIsRich"
//		, "true" 
//	);
////	    		HttpURLConnectionRunner.execPostStructure(
////	    			"true"
////	    			, PATH								// /content/oes/en-us/test
////	    			, CONTENT_PATH.concat("flex_col_1c_")
////	    				.concat(Integer.toString(elem_idx--))	// 1
////	    				.concat("/")
////	    				.concat(TEXT)						// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
////	    				.concat("/")
////	    				.concat(PROP_TEXTISRICH)		// "textIsRich"
////	    		);
//	COL_COUNT++;
//}
////////////////////
///*
// *	Content placeholders
// * NODE PATH + resourceType
// * ___________________________________________________________________________________________
// * |/content/.../jcr:content/oes-content/content-column/column_flex/flex_col_1c_1  |foundation/components/parsys
// * |______________________________________________________________________________|___________
// * |/content/.../jcr:content/oes-content/content-column/column_flex/flex_col_1c_0  |foundation/components/parsys
// * |______________________________________________________________________________|___________
//// 1.a sling:resourceType = ttcsd_2018/components/content-column/column_flex/flex_col_1c_1
//// 1.b sling:resourceType = ttcsd_2018/components/content-column/column_flex/flex_col_1c_0
// * 
//// 1.c sling:resourceType = ttcsd_2018/components/content-column/column_flex/flex_col_1c_1
//// 1.d sling:resourceType = ttcsd_2018/components/content-column/column_flex/flex_col_1c_0
//
// * 1.a 
// jcr:content/oes-content/content-column/column_flex/flex_col_1c_1/sling:resourceType=foundation/components/parsys
// */
///* PROBLEM HERE
///////////////////////////// /*
///*
//
// * 1.b 
// jcr:content/oes-content/content-column/column_flex/flex_col_1c_1/sling:resourceType=foundation/components/parsys
// */
///*
// * 3 parts / col
// * ----
// * 1 sling:resourceType
// * 2 text = [html]
// * 3 textIsRich
// */
////System.out.printf( " 0 TwitterColumnTagCommand :: doStructureViaREST  :: execPostStructure \n"
////		+ "INPUT\n"
////		+ "- PATH %s\n"
////		+ "- ATTR_PATH %s\n"
////		+ "- data %s\n", 
////			PROP_PARSYS 
////			,PATH
////			,CONTENT_PATH.concat("flex_col_1c_")	// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0
////				.concat(FLEX_COL_COUNT )			// 1
////				.concat("/")
////				.concat(PROP_BASE_TYPE)        		// sling:resourceType
////);
////		doStructureViaREST( 
////			PATH 
////			, CONTENT_PATH.concat("flex_col_1c_")	// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0
////				.concat(FLEX_COL_COUNT )			// 1
//////    				.concat(Integer.toString(elem_idx))	// 1
////				.concat("/")
////				.concat(PROP_BASE_TYPE)        		// sling:resourceType
////			, PROP_PARSYS
////		);
//////    		HttpURLConnectionRunner.execPostStructure(
//////    			PROP_PARSYS						// foundation/components/parsys   (property value)
//////    			, PATH                			// /content/oes/en-us/test
//////    			, CONTENT_PATH.concat("flex_col_1c_")	// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0
//////    				.concat(Integer.toString(elem_idx))	// 1
//////    				.concat("/")
//////    				.concat(PROP_BASE_TYPE)        	// sling:resourceType
//////    		);
////
/////*
//// * 1.d 
//// text=<column text>
//// */
////System.err.println( "LAST DATA " + next_elem );//.select("div.col-md-6 > div.oes-box").last()
////
////		doContentViaREST( 
////			next_elem 
////			,PATH 
////			, CONTENT_PATH.concat("flex_col_1c_")
////				.concat( FLEX_COL_COUNT )	// 1
////				.concat("/")
////				.concat(TEXT)				// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
////				.concat("/")
////				.concat(TEXT)				// "text={content}
////		);
////
//////    		HttpURLConnectionRunner.execPost(
//////    				next_elem		// [text] (property value)  
//////    				, PATH                					// /content/oes/en-us/test
//////    				, CONTENT_PATH.concat("flex_col_1c_")
//////    					.concat(Integer.toString(elem_idx))	// 1
//////    					.concat("/")
//////    					.concat(TEXT)						// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
//////    					.concat("/")
//////    					.concat(TEXT)						// "text={content}
//////    			);
////
////
/////*
//// * textIsRich attribute
//// */
////		doStructureViaREST( 
////				PATH 
////				,CONTENT_PATH.concat("flex_col_1c_")
////					.concat( FLEX_COL_COUNT )	// 1
////					.concat("/")
////					.concat(TEXT)				// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
////					.concat("/")
////					.concat(PROP_TEXTISRICH)	// "textIsRich"
////				, "true" 
////			);
//////    		HttpURLConnectionRunner.execPostStructure(
//////    			"true"
//////    			, PATH								// /content/oes/en-us/test
//////    			, CONTENT_PATH.concat("flex_col_1c_")
//////    				.concat(Integer.toString(elem_idx--))	// 1
//////    				.concat("/")
//////    				.concat(TEXT)						// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
//////    				.concat("/")
//////    				.concat(PROP_TEXTISRICH)		// "textIsRich"
//////    		);
//
//////////////////////////////
//
/////*
//// * 1.e 
//// jcr:content/oes-content/content-column/column_flex/flex_col_1c_1/sling:resourceType=foundation/components/parsys
//// */
/////*
//// * 3 parts / col
//// * ----
//// * 1 sling:resourceType
//// * 2 text = [html]
//// * 3 textIsRich
//// */
////    		HttpURLConnectionRunner.execPostStructure(
////    			PROP_PARSYS						// foundation/components/parsys   (property value)
////    			, PATH                			// /content/oes/en-us/test
////    			, CONTENT_PATH.concat("flex_col_1c_")	// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0
////    				.concat(Integer.toString(elem_idx))	// 0
////    				.concat("/")
////    			.concat(PROP_BASE_TYPE)        	// sling:resourceType
////    		);
////
/////*
//// * 1.f 
//// text=<column text>
//// */
////System.err.println( "FIRST DATA " + elem ); //elem.select("div.col-md-6 > div.oes-box").first()
////    		HttpURLConnectionRunner.execPost(
////    			elem.select("div.oes-box").first()		// [text] (property value)  
////    			, PATH                					// /content/oes/en-us/test
////    			, CONTENT_PATH.concat("flex_col_1c_")
////    				.concat(Integer.toString(elem_idx))	// 0
////    				.concat("/")
////    				.concat(TEXT)						// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
////    				.concat("/")
////    				.concat(TEXT)						// "text={content}
////    		);
////
/////*
//// * textIsRich attribute
//// */
////    		HttpURLConnectionRunner.execPostStructure(
////    			"true"
////    			, PATH									// /content/oes/en-us/test
////    			, CONTENT_PATH.concat("flex_col_1c_")
////    				.concat(Integer.toString(elem_idx))	// 0
////    				.concat("/")
////    				.concat(TEXT)					// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
////    				.concat("/")
////    				.concat(PROP_TEXTISRICH)		// "textIsRich"
////    		);
//		PageObj.PageCollection.clear();
//		});

//////////////////////////////////////////////	    	
//    	final Elements ELEMS = elem.select("h3.oes-box-title:contains(Twitter)").first()
//    		.parent()/*oes-box*/
//			.parent()/*col-md-6*/
//			.parent()/*row*/
//			.children();
//
////	    final Elements ELEMS = elem.select("div.col-md-6 > div.oes-box > h3.oes-box-title:contains(Twitter)");
//    IntStream.range(0, E.size()  ).forEach( idx -> { // https://stackoverflow.com/questions/22793006/java-8-foreach-with-index
//
//    	final Element next_elem = b.get(idx);
////		final Element next_elem = ELEMS.get(idx);
////		final String COUNTER 	= PageObj.row_cnt < 0 ? "" : "_".concat( Integer.toString(PageObj.row_cnt++) );
////		final String FLEX_COL_COUNT	= Integer.toString( idx % 2 );
//
//
//System.err.println(
//	"TwitterColumnTagCommand :: cmd :"
//		+ "\n  ALL " + ELEMS.size() 
//		+ "\n, COUNTER " + COUNTER 
//		+ "\n, FLEX_COL_COUNT " + FLEX_COL_COUNT
//		+ "\n, idx " + idx
//		+ "\n, next_elem " + next_elem
//);
//
//		String CONTENT_PATH = CONTENT_TILE_SEED.concat( "content-column" )
//			.concat(COUNTER).concat("/" );
///*
// * NODE PATH + resourceType
//// 0.a sling:resourceType = nt:resource
// * result: /content/ttc/en/jcr:content/content-column/sling:resourceType=foundation/components/parsys
//*/
//		doStructureViaREST(
//			PATH
//			, CONTENT_PATH
//				.concat(PROP_NUM_COL)
//			, COMPONENT_LIB.get(PROP_BASE_TYPE)
//		);
//
////	try {
////		HttpURLConnectionRunner.execPostStructure(
////			COMPONENT_LIB.get(PROP_BASE_TYPE)// foundation/components/parsys   (property value)
////			, PATH                			// /content/oes/en-us/test/column
////			, CONTENT_PATH			  		// jcr:content/oes-content/content-column
////				.concat(PROP_BASE_TYPE)    	// sling:resourceType 				(property key)
////		);
////	} catch (Exception e) {
////		// TODO Auto-generated catch block
////		e.printStackTrace();
////	}
//
//
/////////////////////
///*
//* NODE PATH + resourceType
//* COLUMN
////1.a sling:resourceType = ttcsd_2018/components/content/column_flex
// * result: /content/ttc/en/jcr:content/content-column/sling:resourceType=foundation/components/parsys
//*/
//	CONTENT_PATH = CONTENT_PATH.concat(COLUMN).concat("/");
//
////System.err.printf( "CONTENT_PATH %s\ndata = %s", CONTENT_PATH, PROP_COLUMN );
//
//	doStructureViaREST(PATH, CONTENT_PATH.concat(PROP_NUM_COL), PROP_COLUMN);
//
////	try {
////		HttpURLConnectionRunner.execPostStructure(
////			PROP_COLUMN						// oesshared/components/content/column_flex (property value)
////			, PATH							// /content/oes/en-us/test
////			, CONTENT_PATH					// jcr:content/oes-content/content-column/column_flex
////				.concat(PROP_BASE_TYPE) 	// sling:resourceType 			(property key)
////		);
////	} catch (Exception e) {
////		// TODO Auto-generated catch block
////		e.printStackTrace();
////	}
//
///*
// * container for divs
//* 1.b add content attributes; e.g., 
//	num_col=6,6
//*/
////	CONTENT_PATH = CONTENT_PATH.concat(COLUMN);
//	doStructureViaREST(PATH, CONTENT_PATH.concat(PROP_NUM_COL), "6,6");
////	try {
////		HttpURLConnectionRunner.execPostStructure(
////			"6,6" 							// num_col = 6,6 				(property value)
////			, PATH							// /content/oes/en-us/test
////			, CONTENT_PATH					// jcr:content/oes-content/content-column/column_flex
////				.concat(PROP_NUM_COL)		// num_col			 			(property key)
////		);
////	} catch (Exception e) {
////		// TODO Auto-generated catch block
////		e.printStackTrace();
////	}
//
///////////////////
///* NODE PATH + resourceType
// * 0.a sling:resourceType = ttcsd_2018/components/content/column_flex
//*/
//	doStructureViaREST(PATH, CONTENT_PATH, PROP_PARSYS);
//
//
////////////////////
///*
// *	Content placeholders
// * NODE PATH + resourceType
// * ___________________________________________________________________________________________
// * |/content/.../jcr:content/oes-content/content-column/column_flex/flex_col_1c_1  |foundation/components/parsys
// * |______________________________________________________________________________|___________
// * |/content/.../jcr:content/oes-content/content-column/column_flex/flex_col_1c_0  |foundation/components/parsys
// * |______________________________________________________________________________|___________
//// 1.a sling:resourceType = ttcsd_2018/components/content-column/column_flex/flex_col_1c_1
//// 1.b sling:resourceType = ttcsd_2018/components/content-column/column_flex/flex_col_1c_0
// * 
//// 1.c sling:resourceType = ttcsd_2018/components/content-column/column_flex/flex_col_1c_1
//// 1.d sling:resourceType = ttcsd_2018/components/content-column/column_flex/flex_col_1c_0
//
// * 1.a 
// jcr:content/oes-content/content-column/column_flex/flex_col_1c_1/sling:resourceType=foundation/components/parsys
// */
///* PROBLEM HERE
///////////////////////////// /*
///*
//
// * 1.b 
// jcr:content/oes-content/content-column/column_flex/flex_col_1c_1/sling:resourceType=foundation/components/parsys
// */
///*
// * 3 parts / col
// * ----
// * 1 sling:resourceType
// * 2 text = [html]
// * 3 textIsRich
// */
//System.out.printf( " 0 TwitterColumnTagCommand :: doStructureViaREST  :: execPostStructure \n"
//	+ "INPUT\n"
//	+ "- PATH %s\n"
//	+ "- ATTR_PATH %s\n"
//	+ "- data %s\n", 
//		PROP_PARSYS 
//		,PATH
//		,CONTENT_PATH.concat("flex_col_1c_")		// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0
//			.concat(FLEX_COL_COUNT )			// 1
//			.concat("/")
//			.concat(PROP_BASE_TYPE)        		// sling:resourceType
//);
//	doStructureViaREST( 
//		PATH 
//		, CONTENT_PATH.concat("flex_col_1c_")		// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0
//			.concat(FLEX_COL_COUNT )			// 1
////			.concat(Integer.toString(elem_idx))	// 1
//			.concat("/")
//			.concat(PROP_BASE_TYPE)        		// sling:resourceType
//		, PROP_PARSYS
//	);
////	HttpURLConnectionRunner.execPostStructure(
////		PROP_PARSYS						// foundation/components/parsys   (property value)
////		, PATH                			// /content/oes/en-us/test
////		, CONTENT_PATH.concat("flex_col_1c_")	// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0
////			.concat(Integer.toString(elem_idx))	// 1
////			.concat("/")
////			.concat(PROP_BASE_TYPE)        	// sling:resourceType
////	);
//
///*
// * 1.d 
// text=<column text>
// */
//System.err.println( "LAST DATA " + next_elem );//.select("div.col-md-6 > div.oes-box").last()
//
//	doContentViaREST( 
//		next_elem 
//		,PATH 
//		, CONTENT_PATH.concat("flex_col_1c_")
//			.concat( FLEX_COL_COUNT )	// 1
//			.concat("/")
//			.concat(TEXT)				// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
//			.concat("/")
//			.concat(TEXT)				// "text={content}
//	);
//
////	HttpURLConnectionRunner.execPost(
////			next_elem		// [text] (property value)  
////			, PATH                					// /content/oes/en-us/test
////			, CONTENT_PATH.concat("flex_col_1c_")
////				.concat(Integer.toString(elem_idx))	// 1
////				.concat("/")
////				.concat(TEXT)						// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
////				.concat("/")
////				.concat(TEXT)						// "text={content}
////		);
//
//
///*
// * textIsRich attribute
// */
//	doStructureViaREST( 
//			PATH 
//			,CONTENT_PATH.concat("flex_col_1c_")
//				.concat( FLEX_COL_COUNT )	// 1
//				.concat("/")
//				.concat(TEXT)				// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
//				.concat("/")
//				.concat(PROP_TEXTISRICH)	// "textIsRich"
//			, "true" 
//		);
////	HttpURLConnectionRunner.execPostStructure(
////		"true"
////		, PATH								// /content/oes/en-us/test
////		, CONTENT_PATH.concat("flex_col_1c_")
////			.concat(Integer.toString(elem_idx--))	// 1
////			.concat("/")
////			.concat(TEXT)						// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
////			.concat("/")
////			.concat(PROP_TEXTISRICH)		// "textIsRich"
////	);
//
//////////////////////////////
//
/////*
//// * 1.e 
//// jcr:content/oes-content/content-column/column_flex/flex_col_1c_1/sling:resourceType=foundation/components/parsys
//// */
/////*
//// * 3 parts / col
//// * ----
//// * 1 sling:resourceType
//// * 2 text = [html]
//// * 3 textIsRich
//// */
////	HttpURLConnectionRunner.execPostStructure(
////		PROP_PARSYS						// foundation/components/parsys   (property value)
////		, PATH                			// /content/oes/en-us/test
////		, CONTENT_PATH.concat("flex_col_1c_")	// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0
////			.concat(Integer.toString(elem_idx))	// 0
////			.concat("/")
////		.concat(PROP_BASE_TYPE)        	// sling:resourceType
////	);
////
/////*
//// * 1.f 
//// text=<column text>
//// */
////System.err.println( "FIRST DATA " + elem ); //elem.select("div.col-md-6 > div.oes-box").first()
////	HttpURLConnectionRunner.execPost(
////		elem.select("div.oes-box").first()		// [text] (property value)  
////		, PATH                					// /content/oes/en-us/test
////		, CONTENT_PATH.concat("flex_col_1c_")
////			.concat(Integer.toString(elem_idx))	// 0
////			.concat("/")
////			.concat(TEXT)						// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
////			.concat("/")
////			.concat(TEXT)						// "text={content}
////	);
////
/////*
//// * textIsRich attribute
//// */
////	HttpURLConnectionRunner.execPostStructure(
////		"true"
////		, PATH									// /content/oes/en-us/test
////		, CONTENT_PATH.concat("flex_col_1c_")
////			.concat(Integer.toString(elem_idx))	// 0
////			.concat("/")
////			.concat(TEXT)					// jcr:content/oes-content/content-column/column_flex/flex_col_1c_0/text
////			.concat("/")
////			.concat(PROP_TEXTISRICH)		// "textIsRich"
////	);
//
//	});

////////////////////////////

	    } catch (Exception e1) {

	      e1.printStackTrace();
	    }

	  }

///**
// * @param next_elem
// * @param PATH
// * @param elem_idx
// * @param CONTENT_PATH
// * @throws Exception
// */
//	private static void doContentViaREST( final String PATH, String CONTENT_PATH, final Element elem ) {
//		try {
//
//			HttpURLConnectionRunner.execPost(
//				PATH 			// /content/oes/en-us/test
//				, CONTENT_PATH
//				, elem 	// [text] (property value)
//			);
//
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
//	}
//
///**
// * @param PATH
// * @param CONTENT_PATH
// */
//private static void doStructureViaREST( final String PATH, final String CONTENT_PATH, final String CONTENT ) {
//
//	try {
//
//System.out.printf( " TwitterColumnTagCommand :: doStructureViaREST  :: execPostStructure \n"
//	+ "INPUT\n"
//	+ "- PATH %s\n"
//	+ "- ATTR_PATH %s\n"
//	+ "- data %s\n", 
//		PATH
//		, CONTENT_PATH
//		, CONTENT
//);
//		HttpURLConnectionRunner.execPostStructure(
//			PATH          	// /content/oes/en-us/test
//			, CONTENT_PATH	// jcr:content/oes-content/column/column_flex
//							// sling:resourceType			(property key)
//			, CONTENT		// foundation/components/parsys (property value)
//		);
//
//	} catch (Exception e) {
//
//		e.printStackTrace();
//	}
//}

/**
 * @param args
 */
public static void main(String[] args) {

	Path p = Paths.get("C:\\xampp\\htdocs\\www.sdcountyemergency.com\\about\\index.html");
	System.out.println( " MapTagCommand :: main " + p.getParent());
	
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