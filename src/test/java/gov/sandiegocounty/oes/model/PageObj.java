/**
 * 
 */
package gov.sandiegocounty.oes.model;


import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 */
public class PageObj {

	
	private static final Logger log = LoggerFactory.getLogger(PageObj.class);

/* sling:resourceType */
	final static Map<String,String> COMPONENT_LIB = new HashMap<>();
	final static String BASE_TYPE = "sling:resourceType";
	final static String MAP = "map";
	final static String TITLE = "jcr:title";
	final static String TABLE = "table";
	final static String TEXT = "text";
	final static String TEXTISRICH = "textIsRich";

	final static String CONTENT_TILE_SEED = "jcr:content/oes-content/";
/*
 * node counter / uniquifier
 */
	static int cnt = -1;
	static int row_cnt = -1;

	private static String last_tag;
	/* pkg visible */static Map<String,Element> PageCollection = new HashMap<>();


	public PageObj() {}


	/**
	 * @param file  E.g., C:\xampp\htdocs\www.sdcountyemergency.com\about\index.html
	 * @param e
	 * @return
	 */
	static public String processPage(File file, org.jsoup.select.Elements e
//		, int elem_count
	) {

//System.out.printf( " PageObj :: processPage : file %s elems %s", file.getName(), e );
//System.out.println( " PageObj :: processPage : parsePathParticle " + 
//		parsePathParticle(file)
//);

		String result = null;
		final String PATH = parsePathParticle(file);


System.out.printf( " \n0 PageObj :: processPage : \n> PATH %s\n> element size %d\n", PATH, e.size() );


		List<String> specialNodes = Arrays.asList(new String[]
			{"div.art-PostHeader","div.map-instructions"
			,"div.xrm-attribute-value"	// remove,  div.art-PostHeader here
			,"iframe" 					// video? @ donations
			,"div.col-md-6" 			// 2 cols @ home
			,"table.table" 				// table @ updates
			} 
		);


		switch (file.getParent()) {
		case "www.sdcountyemergency.com":
		case "en-us": // en home
		case "es-us": // es home
// homes

			result = "home";
System.out.println(" \nPageObj :: processPage HOME");

			break;

		default:


			if (e.size() > 0) { // content 9-col

				try {
/*
 * monitor tag precedence  */
					PageObj.last_tag = ""; 

					e.forEach(x -> {

String attrs = String.join(".",  x.attributes().asList().stream().map(Attribute::getValue).collect(Collectors.toList()) );
System.out.printf( "\n >> tag %s.%s", x.tagName(), attrs  );

						switch (x.tagName() ) {

						case "h1":
						case "h2":
						case "h3":
						case "h4":


							if( x.hasParent() && x.parent().is("div.oes-box") ) {
/*
 * belay column/headers
 * div.oes-box > h3.oes-box-title
 * avoid titles in maps, tables & columns
 */
							} else {

								HeadTagCommand.cmd(PATH, x, PageObj.cnt++);
							}
							break;

						case "div":
							
							if( 
// collect 6,6 rows (twitter)
								!x.select("h3.oes-box-title:contains(Twitter)").isEmpty()
								&& x.hasClass("col-md-6")
								&& x.parent().hasClass("row")
								&& x.select("div.xrm-attribute-value").isEmpty()

//								x.hasAttr("class") && ( x.attr("class").equals("row") )
//div.col-md-6 > div.oes-box > h3.oes-box-title[@text^=Twitter] 
							) {
System.err.println( "Twit cnt " + x.select("h3.oes-box-title:contains(Twitter)").size());
// h3 class="oes-box-title">Twitter / cal_fire
//								int tmpInt = PageObj.cnt;

//	System.err.println( " >> row? " + x.childNode(0).no);

//System.err.println( ">> ATTR " + x.getElementsByAttributeValue("class", "row"));
// OK
//System.err.println( ">> ATTR " + x.getElementsByAttributeValue("class", "col-md-12"));

//	System.err.println( ">>> col-md-12? " + x.childNode(0).attributes().get("class") );
//							if( x.hasAttr("class") && x.attr( "class").contains( "col-md-6") ) {
////System.err.printf("\n >>> .col-md-6 :: %s", x.attributes());
/*
 * avoid node index increments with dissimlar tags; no need to differentiate 
 */
// SUT
System.out.printf( "\n2 PageObj :: processPage HERE %s.%s\n", x.tagName(), x.attributes());
								TwitterColumnTagCommand.cmd( 
									PATH , x, x.select("h3.oes-box-title:contains(Twitter)").size()
								);


								break;

							} 
//							else if( 
//							x.hasAttr("class") && x.attr( "class").contains( "map-instructions") 
//							|| x.hasAttr("class") && x.attr( "class").contains( "map-filter") 
//							) {
//	
//								MapTagCommand.cmd(
//									PATH, x, PageObj.cnt++
//								);
//									
//								break;
//
//							} // end check for row and children


// TODO table							
//						case "div.map-instructions":
//
////							new MapTagCommand().processPage(
////								PATH, x.getAllElements(), PageObj.cnt++
////							);
//
//							break;

						default:
/// content/oes/es-us/about/jcr:content/oes-content/table
/// content/oes/es-us/about/jcr:content/oes-content/table/tableData
							break;
						}

						PageObj.last_tag = x.tagName();
/*
 * avoid node index increments with dissimlar tags; no need to differentiate  */
						if( PageObj.last_tag.equals(x.tagName())){
							PageObj.cnt++;
						}

					});

				} catch (Exception e1) {

					e1.printStackTrace();
				}
////////////////
//System.out.println( " PageObj :: processPage CONTENT FOUND " + e0.html());
//System.out.println( " PageObj :: processPage CONTENT FOUND " + e0.size());

			}
// NEVER HERE
			e = e.select("div.col-sm-3");
			if(e.size() > 0 ) {

//System.err.println( " PageObj :: processPage RAIL FOUND " + e0.html());
//System.err.println( " PageObj :: processPage RAIL FOUND " + e0.size());
//System.out.println(" PageObj :: processPage NOT home");
			}

			break;
		}

		return result;
	}

///**
// * @param PATH. page path; e.g., /content/oes/en-us/about
// * @param element. Superset of data in content panel
// */
//	public static void mapTagCommand( final String PATH, Element elem ) {
///*
// * map div.oes-box > h3.oes-box-title >
// * div.map-instructions
// */
//
//		try {
//
//			final String COUNTER = PageObj.cnt < 0 ? "" : Integer.toString(elem_cnt++);
//
//
///*
// * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test
//	jcr:content/oes-content/map
//{
//	"sling:resourceType":"oesshared/components/content/asis"
//}
// */
//System.out.println( " PageObj :: mapTagCommand TEST DATA " + elem);
//
//			HttpURLConnectionRunner.execPostStructure(
//				COMPONENT_LIB.get(MAP) 				// oesshared/components/content/asis  
//				, PATH								// /content/oes/en-us/test
//				, CONTENT_TILE_SEED.concat("map/")	// jcr:content/oes-content/map/
//					.concat(BASE_TYPE)				// sling:resourceType
//			);
//
//System.err.printf( " PageObj :: mapTagCommand PATH FILE \n%s\nattr path %s,\ncnt %d\n"
//	, PATH
//	, CONTENT_TILE_SEED.concat("map/" )
//	, PageObj.cnt
//);
//
//
///*
// * 2 add content attributes; e.g., 
// text=<iframe....>
// */
//log.debug( " PageObj :: mapTagCommand (header version) : elem html {}"
//	, elem.selectFirst( "div.map-instructions").parent().parent()
//);
//			HttpURLConnectionRunner.execPost(
//// CONTENT PATH: /content/oes/es-us/about
//// ATTR PATH:	jcr:content/oes-content/title/sling:resourceType
//// /content/oes
//
//				elem.selectFirst( "div.map-instructions")
//					.parent().parent()			// CONTENT
//				, PATH					 		// /content/oes/es-us/about
//				, CONTENT_TILE_SEED
//					.concat("map")
//					.concat(COUNTER.concat("/"))
//					.concat( MAP )		// jcr:content/oes-content/title/jcr:title=<CONTENT>
//			);
//
//		} catch (Exception e1) {
//
//			e1.printStackTrace();
//		}
//
////		try {
////
////			HttpURLConnectionRunner.execPost(new Element(elem.parent().parent().html()) // content
////				, COMPONENT_LIB.get(MAP) 				// parsys
////				, CONTENT_TILE_SEED.concat("map")
////					.concat(Integer.toString(elem_cnt++))	// path
//////				, true									// site structure data
////			);
////		} catch (Exception e1) {
////
////			e1.printStackTrace();
////		}
//	}

//	/**
//	 * @param PATH. page path; e.g., /content/oes/en-us/about
//	 * @param element. Superset of data in content panel
//	 * Usually page title are decorated with class=.art-PostHeader.. but not always
//	 * Other kinds of H* are page elements and not page headers
//	 */
//	static public void headTagCommand(final String PATH, Element x) {
//
//		try {
//
//			final String COUNTER = PageObj.cnt < 0 ? "" : Integer.toString(elem_cnt++);
///*
// * Non-page header / only a page element
// */
//			if (x.getElementsByAttributeValueMatching("class", "art-PostHeader").isEmpty()) {  // page data
///* DO NORMAL TEXT PROCESSING */
////System.out.println(" PageObj :: headTagCommand (non-header/page element version) : page data / NON HEADer elem data " 
////	+ x.selectFirst("h1,h2,h3,h4").parent()
////);
//
///*
// * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/oes-content/title
//{
//	"sling:resourceType":"oes/components/content/text"
//}
// */
//System.out.println( " PageObj :: headTagCommand TEST DATA " + new Element( COMPONENT_LIB.get(TITLE) ));
///*
// * text property
// */
//			HttpURLConnectionRunner.execPostStructure(
//				COMPONENT_LIB.get(TEXT) 				// "foundation/components/text"  
//				, PATH									// /content/oes/en-us/test
//				, CONTENT_TILE_SEED.concat("text")
//					.concat(COUNTER).concat("/") 		// jcr:content/oes-content/text#/
//					.concat(BASE_TYPE)					// sling:resourceType
//			);
///*
// * textIsRich attribute
// */
//			HttpURLConnectionRunner.execPostStructure(
//				"true"
//				, PATH									// /content/oes/en-us/test
//				, CONTENT_TILE_SEED.concat("text")
//					.concat(COUNTER).concat("/") 		//jcr:content/oes-content/text#/
//					.concat(TEXTISRICH)					// "textIsRich"
//			);
//
//System.err.printf( " PageObj :: processPage PATH FILE \n%s\nattr path %s,\ncnt %d\n"
//	, PATH
//	, CONTENT_TILE_SEED.concat("text/" )
//	, PageObj.cnt
//);
//
//
///*
// * 2 add content attributes; e.g., 
// jcr:title = Articulos
// */
//log.debug( " PageObj :: headTagCommand (header version) : elem html {}", x.selectFirst("h1,h2,h3,h4").html() );
//			HttpURLConnectionRunner.execPost(
//// CONTENT PATH: /content/oes/es-us/about
//// ATTR PATH:	jcr:content/oes-content/title/sling:resourceType
//// /content/oes
//
//				x.selectFirst("h1,h2,h3,h4")	// CONTENT
//				, PATH					 		// /content/oes/es-us/about
//				, CONTENT_TILE_SEED
//					.concat("text")
//					.concat(COUNTER).concat("/")// jcr:content/oes-content/text#/
//					.concat( TEXT )				// jcr:content/oes-content/title/jcr:title=<CONTENT>
//			);
//
//// / "NORMAL" h* node
//
//
//			} else { // page title
//
//
///*
// * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/oes-content/title
//{
//	"sling:resourceType":"oes/components/content/title"
//}
// */
//System.out.println( " PageObj :: headTagCommand TEST DATA " + new Element( COMPONENT_LIB.get(TITLE) ));
//			HttpURLConnectionRunner.execPostStructure(
//				COMPONENT_LIB.get(TITLE) 				// oesshared/components/content/title  
//				, PATH									// /content/oes/en-us/test
//				, CONTENT_TILE_SEED.concat("title/")	// jcr:content/oes-content/title/
//					.concat(BASE_TYPE)					// sling:resourceType
//			);
//
//System.err.printf( " PageObj :: processPage PATH FILE \n%s\nattr path %s,\ncnt %d\n"
//	, PATH
//	, CONTENT_TILE_SEED.concat("title/" )
//	, PageObj.cnt
//);
//
//
///*
// * 2 add content attributes; e.g., 
// jcr:title = Articulos
// */
//log.debug( " PageObj :: headTagCommand (header version) : elem html {}", x.selectFirst("h1,h2,h3,h4").html() );
//			HttpURLConnectionRunner.execPost(
//// CONTENT PATH: /content/oes/es-us/about
//// ATTR PATH:	jcr:content/oes-content/title/sling:resourceType
//// /content/oes
//
//				x.selectFirst("h1,h2,h3,h4")	// CONTENT
//				, PATH					 		// /content/oes/es-us/about
//				, CONTENT_TILE_SEED
//					.concat("title")
//					.concat(COUNTER.concat("/"))
//					.concat( TITLE )		// jcr:content/oes-content/title/jcr:title=<CONTENT>
//			);
//
//			}// end if check for page data
//
//		} catch (Exception e1) {
//
//			e1.printStackTrace();
//		}
//	}

	/**
	 * @param file
	 */
	public static String parsePathParticle(File file) {
/*
 * default lang & site prefix is US English
 */
		final String prefix = ( !file.getAbsolutePath().contains("en-us") && !file.getAbsolutePath().contains("es-us")) 
			? "en-us/"
			: "";

// (file.getAbsolutePath().contains("en-us") || file.getAbsolutePath().contains("es-us")) 
//		? "/content/oes/"
//		: ""
		final String uri_path = "/content/oesemergency/".concat(prefix).concat( file.getAbsolutePath() )
			.replace( "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com\\", "")
//			.replace( "C:\\xampp\\htdocs\\www.sdcountyemergency.com\\", "")
			.replace( "\\index.html", "")
			.replaceAll( "\\\\", "/")
			.replaceAll( ".index.html", "")
			;

System.out.println( " PageObj :: parsePathParticle : uri : " + uri_path );
		return uri_path;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {


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