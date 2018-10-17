/**
 * 
 */
package gov.sandiegocounty.oes.model;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.day.cq.commons.jcr.JcrConstants;

import gov.sandiegocounty.util.HttpURLConnectionRunner;


/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 *
 * DOM Manifest 
 *  - maps
 *  	- 2 primary maps type
 *  		a) arcgis
 *  			div.oes-box > ... stuff ... div.map-instructions (non-empty only) > iframe
 			   <iframe src="http://arcgis.com/apps/SimpleViewer/index.html?appid=bc85ca8bcfaa4324b53878533bc524d6" width="100%" height="460" frameborder="0" marginwidth="0" marginheight="0" scrolling="no"></iframe>
 *  		b) div.oes-box > ... stuff ... div#map-filter .... stuff.... div ...map stuff
 *  			azure maps
 *  - columns
 *  - twitter
 *  - table
 *  	div.oes-box > h3.oes-box-title > table.table.table-striped.news-list
 *  	sling:resourceType=foundation/components/table
 *  	tableData=<data>
 *    
 *  - image
 *  	adaptive image
 *  - video
 *  	sling:resourceType=foundation/components/video
 *  	width=560
 *  	height"315
 *  	videoid=12cNBNKIXv8
 *  	<iframe width="560" height="315" allowfullscreen="allowfullscreen" 
 *  		allow="encrypted-media" gesture="media" frameborder="0" 
 *  			src="https://www.youtube.com/embed/12cNBNKIXv8?rel=0&amp;showinfo=0"></iframe>
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

	final static String CONTENT_TILE_SEED = JcrConstants.JCR_CONTENT.concat( "/oes-content/" );
/*
 * node counter / uniquifier
 */
	static int cnt = -1;
	static int row_cnt = -1;

//	private static String last_tag;
	/* pkg visible */static Map<String,Element> PageCollection = new HashMap<>();


	public PageObj() {}


	/**
	 * @param file  E.g., C:\xampp\htdocs\www.sdcountyemergency.com\about\index.html
	 * @param e
	 * @return
	 */
	static public void processPage(File file
		, org.jsoup.select.Elements e ) {

//System.out.printf( " PageObj :: processPage : file %s elems %s", file.getName(), e );
//System.out.println( " PageObj :: processPage : parsePathParticle " + 
//		parsePathParticle(file)
//);

//		String result = null;
		final String PATH = parsePathParticle(file);


//System.out.printf( " \n0 PageObj :: processPage : \n> PATH %s\n> element size %d\n", PATH, e.size() );


//		List<String> specialNodes = Arrays.asList(new String[]
//			{"div.art-PostHeader","div.map-instructions"
//			,"div.xrm-attribute-value"	// remove,  div.art-PostHeader here
//			,"iframe" 					// video? @ donations
//			,"div.col-md-6" 			// 2 cols @ home
//			,"table.table" 				// table @ updates
//			} 
//		);

		final String file_parent = file.getParentFile().getName() ;
		final String file_parent_parent = file.getParentFile().getParentFile().getName() ;
		final int count = file.getPath().split("\\\\").length;


//C:\Users\User\workspace\oesshared\website\www.sdcountyemergency.com\es-us\index.html
//		switch (file_parent_parent) { // process page contexts
		switch (file_parent) { // process updates contexts


			case "www.sdcountyemergency.com":
			case "en-us": // en home
			case "es-us": // es home
// homes


//			result = "home";
System.out.println(" \nPageObj :: processPage HOME");

				break;

			case "updates":
//			case "lilac-fire-update-120917-2329":

/*
 * :: Special case ::
 * Convert Elements to parent Element and process a page 
 * into a component
 */

				Element test = new Element("body");
				test = test.html(e.outerHtml());
//				test.insertChildren(0, e);
//System.out.println( " PageObj :: processPage " + test.selectFirst("div.col-sm-9" ).getAllElements().size() );
			try {

				IncidentComponentCommand.cmd(PATH, test.selectFirst("div.col-sm-9" ));

				final Document doc = Jsoup.parse( new File(file.toString() ) , "UTF-8");
				IncidentComponentCommand.makeTitle(PATH, doc.select("ul.breadcrumb > li").last() );
				

			} catch (Exception e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

//System.err.printf( "\n\n>>>> 1 file.getParent %s - %s\n", file.getParent(), file.getParentFile().getName() );
//					String STR_TITLE = "Incident";
//					if( null != e.select( "h1,h2,h3,h4").first() ) {
//
//						STR_TITLE = StringUtils.chomp( e.select( "h1,h2,h3,h4").first().text() );
//					}
/*
 * 
 */
////////////
//System.out.printf( " PageObj :: processPage \nPATH %s\nJCR_CONTENT %s\nSTR_TITLE %s"
//	,PATH
//	,JcrConstants.JCR_CONTENT
//		.concat("/").concat( TITLE)	// jcr:content/oes-content/title
//	, STR_TITLE	// <title>
//);
//
//////////////
//AbstractTagCommand.doStructureViaREST(
//	PATH
//	,CONTENT_TILE_SEED.concat(TEXT)
//		.concat("/")
//		.concat(AbstractTagCommand.PROP_BASE_TYPE)
//	, AbstractTagCommand.PROP_TEXT
//);
//
///*
//* assign node definition :: textIsRich attribute
//*/
//AbstractTagCommand.doStructureViaREST(
//	PATH
//	,CONTENT_TILE_SEED.concat(TEXT)
//		.concat("/") 		// jcr:content/oes-content/text#/
//		.concat(AbstractTagCommand.PROP_TEXTISRICH)			// "textIsRich"
//	, "true"
//);
//////////////
//
//HttpURLConnectionRunner.execPostStructure(
//	PATH          					// /content/oes/en-us/test
//	,JcrConstants.JCR_CONTENT
//	.concat("/").concat( TITLE)	// jcr:content/oes-content/title
//	, STR_TITLE	// <title>
//);
				break;

			default:
//
//				if (e.size() > 0) { // content 9-col
///*
// * monitor tag precedence  */
//					PageObj.last_tag = ""; 
//
//					e.forEach(x -> { // start lambda for-loop
//						try {
// 
//String attrs = String.join(".",  x.attributes().asList().stream().map(Attribute::getValue).collect(Collectors.toList()) );
////System.out.printf( "\n >> tag %s.%s", x.tagName(), attrs  );
//
//						switch (x.tagName() ) { // process element contexts
//
//						case "h1":
//						case "h2":
//						case "h3":
//						case "h4":
//
//
//							if( !x.select("iframe").isEmpty() ) {
///*
// * avoid embedded content
// */
//							} else if( x.hasParent() && x.parent().is("div.oes-box") ) {
///*
// * belay column/headers
// * div.oes-box > h3.oes-box-title
// * avoid titles in maps, tables & columns
// */
//
//							} else {
//
//								HeadTagCommand.cmd(PATH, x, HeadTagCommand.cnt++);
//
//							}
//
//							break;
//
///*						case "hr":
//
//							HorizRuleTagCommand.cmd(PATH, x, HorizRuleTagCommand.cnt++);
//
//							break;
//*/
//						case "iframe":
//
///*
// * 	<iframe width="560" height="315" allowfullscreen="allowfullscreen" allow="encrypted-media" gesture="media" frameborder="0" src="https://www.youtube.com/embed/12cNBNKIXv8?rel=0&amp;showinfo=0"></iframe>
//	<iframe src="http://arcgis.com/apps/SimpleViewer/index.html?appid=bc85ca8bcfaa4324b53878533bc524d6" width="100%" height="460" frameborder="0" marginwidth="0" marginheight="0" scrolling="no"></iframe>
// *
// */
//
//							if( x.hasAttr("src") 
//								&& x.attr("src").startsWith("https://www.youtube.com")	// video
//							) {
///*
// *  - video
// *  	sling:resourceType=foundation/components/video
// *  	width=560
// *  	height"315
// *  	videoid=12cNBNKIXv8
// *  	<iframe width="560" height="315" allowfullscreen="allowfullscreen" 
// *  		allow="encrypted-media" gesture="media" frameborder="0" 
// *  			src="https://www.youtube.com/embed/12cNBNKIXv8?rel=0&amp;showinfo=0"></iframe>
// */
//								VideoTagCommand.cmd(PATH, x, VideoTagCommand.cnt++);
//
//							} else if( x.hasAttr("src") // MAP 
//								&& x.attr("src").startsWith("http://arcgis.com")
//							) {
//
////								MapTagCommand.cmd(PATH, x, MapTagCommand.cnt++);
//							}
//
//							break;
//
//
//						case "div":
///*
// * DIVS support 
// *  - maps
// *  	- 2 primary maps type
// *  		a) arcgis
// *  			div.oes-box > ... stuff ... div.map-instructions (non-empty only) > iframe
// 			   <iframe src="http://arcgis.com/apps/SimpleViewer/index.html?appid=bc85ca8bcfaa4324b53878533bc524d6" width="100%" height="460" frameborder="0" marginwidth="0" marginheight="0" scrolling="no"></iframe>
// *  		b) div.oes-box > ... stuff ... div#map-filter .... stuff.... div ...map stuff
// *  			azure maps
// *  - columns
// *  	!div.col-md-8 [
// *  		div.col-md-3
// *  		div.col-md-4
// *  		div.col-md-6
// *  		div.col-md-8
// *  	]
// *  - twitter
// *  - table
// *  	div.oes-box > h3.oes-box-title > table.table.table-striped.news-list
// *  	sling:resourceType=foundation/components/table
// *  	tableData=<data>
// *    
// *  - image
// *  	adaptive image
// *  - video
// *  	<iframe width="560" height="315" allowfullscreen="allowfullscreen" allow="encrypted-media" gesture="media" frameborder="0" src="https://www.youtube.com/embed/12cNBNKIXv8?rel=0&amp;showinfo=0"></iframe>
// *  
// */
//							if(	x.hasClass("col-md-3")
//								|| x.hasClass("col-md-4")
//								|| x.hasClass("col-md-6")
//								|| x.hasClass("col-md-8")
//								) {// columns
//
//								ColumnTagCommand.cmd(PATH, x, ColumnTagCommand.cnt++ );
//								
//							}// end columns
//
//
//							if(
//								!x.select("h3.oes-box-title:contains(Twitter)").isEmpty()
//								&& x.hasClass("col-md-6")
//								&& x.parent().hasClass("row")
//								&& x.select("div.xrm-attribute-value").isEmpty()
//
////								x.hasAttr("class") && ( x.attr("class").equals("row") )
////div.col-md-6 > div.oes-box > h3.oes-box-title[@text^=Twitter] 
//							) { // TWITTER
//System.err.println( "Twit cnt " + x.select("h3.oes-box-title:contains(Twitter)").size());
//// h3 class="oes-box-title">Twitter / cal_fire
////								int tmpInt = PageObj.cnt;
//
///*
// * avoid node index increments with dissimlar tags; no need to differentiate 
// */
//// SUT
////System.out.printf( "\n2 PageObj :: processPage HERE %s : .%s\n", x.tagName(), x.attributes());
//								TwitterColumnTagCommand.cmd( 
//									PATH, x, x.select("h3.oes-box-title:contains(Twitter)").size()
//								);
//							} // END TWITTER
//							break;
//
//
//						case "table":
//
////							if( x.tagName().equals("table")
//////								&& x.hasClass("table")
////// div.oes-box > table.table.table-striped.news-list
////							) {
//////System.out.printf( "\n2 PageObj :: processPage TABLE  %s : .%s\n", x.tagName(), x.attributes());
////
//								TableTagCommand.cmd(PATH, x, TableTagCommand.cnt++ );
////							}
//
//							break;
//
//						case "li":
//						case "script":
//
//							break;
//
//
//						case "p":
//						case "hr":
//						case "ul":
//
///*
// * avoid node index increments with dissimlar tags; no need to differentiate 
// */
//// SUT
//							ParagraphTagCommand.cmd( 
//								PATH , x, ParagraphTagCommand.cnt++);
//
//							break;
//
////							ParagraphUpdateSectionTagCommand.cmd( 
////								PATH , x.parent(), ParagraphUpdateSectionTagCommand.cnt++);
////
////								break;
//
////							else if( 
////							x.hasAttr("class") && x.attr( "class").contains( "map-instructions") 
////							|| x.hasAttr("class") && x.attr( "class").contains( "map-filter") 
////							) {
////	
////								MapTagCommand.cmd(
////									PATH, x, PageObj.cnt++
////								);
////									
////								break;
////
////							} // end check for row and children
//
//
//// TODO table							
////						case "div.map-instructions":
////
//////							new MapTagCommand().processPage(
//////								PATH, x.getAllElements(), PageObj.cnt++
//////							);
////
////							break;
//
//						default:
///// content/oes/es-us/about/jcr:content/oes-content/table
///// content/oes/es-us/about/jcr:content/oes-content/table/tableData
//							break;
//						} // END  process element contexts
//
//						PageObj.last_tag = x.tagName();
///*
// * avoid node index increments with dissimlar tags; no need to differentiate  */
//						if( PageObj.last_tag.equals(x.tagName())){
//							PageObj.cnt++;
//						}
//
//} catch (Exception e1) {
//
//
//	try {
//
//		Files.write(Paths.get("processing-errs.txt"), PATH.concat(" :: ").concat( e1.getMessage() ).getBytes() );
//	} catch (IOException e2) {
//
//		e2.printStackTrace();
//	}
//
//}
//
//					}); // end lamda elements loop 
//
////				} catch (Exception e1) {
//
//
////					try {
////					
////						Files.write(Paths.get("processing-errs.txt"), PATH.concat(" :: ").concat( e1.getMessage() ).getBytes() );
////					} catch (IOException e2) {
////					
////						e2.printStackTrace();
////					}
////					e1.printStackTrace();
//
//				} // end if (e.size() > 0) { // content 9-col

				break;

			} // end switch (file_parent) { // process page contexts// END  process page contexts
// NEVER HERE
			e = e.select("div.col-sm-3");
			if(e.size() > 0 ) {

//System.err.println( " PageObj :: processPage RAIL FOUND " + e0.html());
			}

//			break;
//		} // END  process page contexts

//		return result;
	}


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

		File file = new File("C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com\\updates\\active-shooter-at-san-diego-rockn-roll-marathon-060318-1325\\index.html");
		final int count = file.getPath().split("\\\\").length;
//		System.out.println( " PageObj :: main " + file.separator);
		System.out.println( " PageObj :: main " + count);

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