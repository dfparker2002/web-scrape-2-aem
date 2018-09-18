/**
 * 
 */
package gov.sandiegocounty.oes.model;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.sandiegocounty.util.HttpURLConnectionRunner;


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

	final static String CONTENT_TILE_SEED = "jcr:content/parcontent/";
/*
 * node counter / uniquifier
 */
	static int cnt = -1;

	public PageObj() {

//sling:resourceType
		COMPONENT_LIB.put( BASE_TYPE,	"foundation/components/parsys" );
		COMPONENT_LIB.put( TEXT, 		"foundation/components/text" );		// add [path]/text
																			// add [path]/textIsRich=true
		COMPONENT_LIB.put( TABLE, 		"foundation/components/table" ); 	// add [path]/tableData
		COMPONENT_LIB.put( MAP, 		"oesshared/components/content/asis" );	// add [path]/asis
		COMPONENT_LIB.put( TITLE, 		"oesshared/components/content/title" );	// add [path]/jcr:title
	}//div.page-copy > table.table

//	static final Element 
	/**
	 * @param file  E.g., C:\xampp\htdocs\www.sdcountyemergency.com\about\index.html
	 * @param e
	 * @return
	 */
	static public String processPage(File file, org.jsoup.select.Elements e) {

//System.out.printf( " PageObj :: processPage : file %s elems %s", file.getName(), e );
//System.out.println( " PageObj :: processPage : parsePathParticle " + 
//		parsePathParticle(file)
//);

		String result = null;
		final String PATH = parsePathParticle(file);


System.out.printf( " PageObj :: processPage : PATH %s", PATH );


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
System.out.println(" PageObj :: processPage home");
			break;

		default:

//			result = file.getPath().concat( " not home" );
//System.err.println( "====\n" + result);

			Elements e0 = e.select("div.col-sm-9");
			
			if (e0.size() > 0) { // content 9-col

				final String path_part = parsePathParticle(file);
				try {
/*
 * content
 */
					PageObj.cnt = -1;
					e0.forEach(x -> {

						switch (x.tagName()) {

						case "h1":
						case "h2":
						case "h3":

							headTagCommand(PATH, x);

							break;

						case "div.map-instructions":

							mapTagCommand(PATH, x);

							break;

						default:
/// content/oes/es-us/about/jcr:content/parcontent/table
/// content/oes/es-us/about/jcr:content/parcontent/table/tableData
							break;
						}

					});

				} catch (Exception e1) {

					e1.printStackTrace();
				}
////////////////
//System.out.println( " PageObj :: processPage CONTENT FOUND " + e0.html());
//System.out.println( " PageObj :: processPage CONTENT FOUND " + e0.size());

			} 
			e0 = e.select("div.col-sm-3");
			if(e0.size() > 0 ) {

//System.err.println( " PageObj :: processPage RAIL FOUND " + e0.html());
//System.err.println( " PageObj :: processPage RAIL FOUND " + e0.size());
//System.out.println(" PageObj :: processPage NOT home");
			}

			break;
		}

		return result;
	}

/**
 * @param PATH. page path; e.g., /content/oes/en-us/about
 * @param element. Superset of data in content panel
 */
	public static void mapTagCommand( final String PATH, Element elem ) {
/*
 * map div.oes-box > h3.oes-box-title >
 * div.map-instructions
 */

		try {

			final String COUNTER = PageObj.cnt < 0 ? "" : Integer.toString(cnt++);


/*
 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test
	jcr:content/parcontent/map
{
	"sling:resourceType":"oesshared/components/content/asis"
}
 */
System.out.println( " PageObj :: mapTagCommand TEST DATA " + elem);
			HttpURLConnectionRunner.execPostStructure(
				COMPONENT_LIB.get(MAP) 				// oesshared/components/content/asis  
				, PATH								// /content/oes/en-us/test
				, CONTENT_TILE_SEED.concat("map/")	// jcr:content/parcontent/map/
					.concat(BASE_TYPE)				// sling:resourceType
			);

System.err.printf( " PageObj :: mapTagCommand PATH FILE \n%s\nattr path %s,\ncnt %d\n"
	, PATH
	, CONTENT_TILE_SEED.concat("map/" )
	, PageObj.cnt
);


/*
 * 2 add content attributes; e.g., 
 text=<iframe....>
 */
log.debug( " PageObj :: mapTagCommand (header version) : elem html {}"
	, elem.selectFirst( "div.map-instructions").parent().parent()
);
			HttpURLConnectionRunner.execPost(
// CONTENT PATH: /content/oes/es-us/about
// ATTR PATH:	jcr:content/parcontent/title/sling:resourceType
// /content/oes

				elem.selectFirst( "div.map-instructions")
					.parent().parent()			// CONTENT
				, PATH					 		// /content/oes/es-us/about
				, CONTENT_TILE_SEED
					.concat("map")
					.concat(COUNTER.concat("/"))
					.concat( MAP )		// jcr:content/parcontent/title/jcr:title=<CONTENT>
			);

		} catch (Exception e1) {

			e1.printStackTrace();
		}

//		try {
//
//			HttpURLConnectionRunner.execPost(new Element(elem.parent().parent().html()) // content
//				, COMPONENT_LIB.get(MAP) 				// parsys
//				, CONTENT_TILE_SEED.concat("map")
//					.concat(Integer.toString(cnt++))	// path
////				, true									// site structure data
//			);
//		} catch (Exception e1) {
//
//			e1.printStackTrace();
//		}
	}

	/**
	 * @param PATH. page path; e.g., /content/oes/en-us/about
	 * @param element. Superset of data in content panel
	 * Usually page title are decorated with class=.art-PostHeader.. but not always
	 * Other kinds of H* are page elements and not page headers
	 */
	static public void headTagCommand(final String PATH, Element x) {

		try {

			final String COUNTER = PageObj.cnt < 0 ? "" : Integer.toString(cnt++);
/*
 * Non-page header / only a page element
 */
			if (x.getElementsByAttributeValueMatching("class", "art-PostHeader").isEmpty()) {  // page data
/* DO NORMAL TEXT PROCESSING */
//System.out.println(" PageObj :: headTagCommand (non-header/page element version) : page data / NON HEADer elem data " 
//	+ x.selectFirst("h1,h2,h3,h4").parent()
//);

/*
 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/parcontent/title
{
	"sling:resourceType":"oes/components/content/text"
}
 */
System.out.println( " PageObj :: headTagCommand TEST DATA " + new Element( COMPONENT_LIB.get(TITLE) ));
/*
 * text property
 */
			HttpURLConnectionRunner.execPostStructure(
				COMPONENT_LIB.get(TEXT) 				// "foundation/components/text"  
				, PATH									// /content/oes/en-us/test
				, CONTENT_TILE_SEED.concat("text")
					.concat(COUNTER).concat("/") 		// jcr:content/parcontent/text#/
					.concat(BASE_TYPE)					// sling:resourceType
			);
/*
 * textIsRich attribute
 */
			HttpURLConnectionRunner.execPostStructure(
				"true"
				, PATH									// /content/oes/en-us/test
				, CONTENT_TILE_SEED.concat("text")
					.concat(COUNTER).concat("/") 		//jcr:content/parcontent/text#/
					.concat(TEXTISRICH)					// "textIsRich"
			);

System.err.printf( " PageObj :: processPage PATH FILE \n%s\nattr path %s,\ncnt %d\n"
	, PATH
	, CONTENT_TILE_SEED.concat("text/" )
	, PageObj.cnt
);


/*
 * 2 add content attributes; e.g., 
 jcr:title = Articulos
 */
log.debug( " PageObj :: headTagCommand (header version) : elem html {}", x.selectFirst("h1,h2,h3,h4").html() );
			HttpURLConnectionRunner.execPost(
// CONTENT PATH: /content/oes/es-us/about
// ATTR PATH:	jcr:content/parcontent/title/sling:resourceType
// /content/oes

				x.selectFirst("h1,h2,h3,h4")	// CONTENT
				, PATH					 		// /content/oes/es-us/about
				, CONTENT_TILE_SEED
					.concat("text")
					.concat(COUNTER).concat("/")// jcr:content/parcontent/text#/
					.concat( TEXT )				// jcr:content/parcontent/title/jcr:title=<CONTENT>
			);

// / "NORMAL" h* node


			} else { // page title


/*
 * 1 create parsys; E.g.,http://192.168.200.87:4502/content/oes/en-us/test/jcr:content/parcontent/title
{
	"sling:resourceType":"oes/components/content/title"
}
 */
System.out.println( " PageObj :: headTagCommand TEST DATA " + new Element( COMPONENT_LIB.get(TITLE) ));
			HttpURLConnectionRunner.execPostStructure(
				COMPONENT_LIB.get(TITLE) 				// oesshared/components/content/title  
				, PATH									// /content/oes/en-us/test
				, CONTENT_TILE_SEED.concat("title/")	// jcr:content/parcontent/title/
					.concat(BASE_TYPE)					// sling:resourceType
			);

System.err.printf( " PageObj :: processPage PATH FILE \n%s\nattr path %s,\ncnt %d\n"
	, PATH
	, CONTENT_TILE_SEED.concat("title/" )
	, PageObj.cnt
);


/*
 * 2 add content attributes; e.g., 
 jcr:title = Articulos
 */
log.debug( " PageObj :: headTagCommand (header version) : elem html {}", x.selectFirst("h1,h2,h3,h4").html() );
			HttpURLConnectionRunner.execPost(
// CONTENT PATH: /content/oes/es-us/about
// ATTR PATH:	jcr:content/parcontent/title/sling:resourceType
// /content/oes

				x.selectFirst("h1,h2,h3,h4")	// CONTENT
				, PATH					 		// /content/oes/es-us/about
				, CONTENT_TILE_SEED
					.concat("title")
					.concat(COUNTER.concat("/"))
					.concat( TITLE )		// jcr:content/parcontent/title/jcr:title=<CONTENT>
			);

			}// end if check for page data

		} catch (Exception e1) {

			e1.printStackTrace();
		}
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
		final String uri_path = "/content/oes/".concat(prefix).concat( file.getAbsolutePath() )
			.replace( "C:\\xampp\\htdocs\\www.sdcountyemergency.com\\", "")
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