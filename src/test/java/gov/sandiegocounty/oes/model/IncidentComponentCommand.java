/**
 * 
 */
package gov.sandiegocounty.oes.model;

import java.text.ParseException;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 */
public class IncidentComponentCommand extends AbstractTagCommand implements ITagCommand {


	private static final Logger log = LoggerFactory.getLogger(IncidentComponentCommand.class);

	static int cnt = -1;

	public IncidentComponentCommand() {
	}//div.page-copy > table.table



	/**
	 * @param PATH. page path; e.g., /content/oes/en-us/about
	 * @param element. Superset of data in content panel
	 * Usually page title are decorated with class=.art-PostHeader.. but not always
	 * Other kinds of H* are page elements and not page headers
	 */
	static public void cmd(final String PATH, Element x ) throws Exception {


		final String COUNTER = IncidentComponentCommand.cnt < 0 ? "" : Integer.toString(IncidentComponentCommand.cnt);
System.err.println( " :: cnt " + IncidentComponentCommand.cnt );

///////////////////............................
//datetime
				Element datetime = x.select("div.col-sm-12").get(1); // 2nd position value; extract text 
				Element asl_link = x.select("a").last();

////////////
Elements remainder = x.getAllElements().select("div.page-copy").first().getElementsByAttributeValue("class", "xrm-attribute-value");

// build nested nodes
				doNodesCreation(PATH, remainder );


// Extract, excise date (first p)
System.out.println( " IncidentComponentCommand :: cmd datetime " + datetime);

				doDateTimeProperty(
					PATH
					, CONTENT_TILE_SEED
						.concat(INCIDENT_ITEM)		// jcr:content/oes-content/datetime=<CONTENT>
					, datetime
				);

				doStructureViaREST(
					PATH
					, CONTENT_TILE_SEED
						.concat(INCIDENT_ITEM)
						.concat("/").concat("asl-link")		// jcr:content/oes-content/asl-link=<CONTENT>
					, asl_link.attr("href")
				);
/*
 * REMOVE SCRIPT in TEXT?  
 * E.g.,div class="addthis_toolbox
 */



///////////////////............................


	}


	/**
	 * Non-page header / only a page element
	 * @param PATH
	 * @param x
	 */
	static void makeTitle(final String PATH, final Element x) {

		Element STR_TITLE = new Element("li").html( "Incident" );

log.debug("Make title {}", x.text());
 
		if( null != x.select( "li").first() ) {

			STR_TITLE = x;
		}
		
		doContentStringViaREST(
			PATH
			, PAGE_CONTENT_SEED
				.concat(PROP_TITLE)			// jcr:content/jcr:title=<CONTENT>
			, STR_TITLE.text()				// <title>
		);

		doContentStringViaREST(
			PATH
			, CONTENT_TILE_SEED				// jcr:content/oes-content/title=<>
				.concat(INCIDENT_ITEM)
				.concat("/").concat("title")// jcr:content/oes-content/incident_item/title=<>
			, STR_TITLE.text()				// <title>
		);

		}

	/**
	 * @param PATH
	 * @param x
	 * @param COUNTER
	 */
	private static void doDateTimeProperty(
			final String PATH
			, final String CONTENT_PATH
			, Element e) {
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
		try {

			doAddDateTimeProperty(
				PATH
				, CONTENT_TILE_SEED
					.concat(INCIDENT_ITEM)
//					.concat("/").concat("datetime")
				, e									// CONTENT
			);

		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	/**
	 * @param PATH
	 * @param x
	 * @param COUNTER
	 */
	private static void doNodesCreation(final String PATH, Elements ee) {

		doStructureViaREST(
			PATH
			, CONTENT_TILE_SEED
				.concat(INCIDENT_ITEM)
				.concat("/").concat(PROP_BASE_TYPE)
			, PROP_INCIDENT_ITEM
		);

		doStructureViaREST(
			PATH
			, CONTENT_TILE_SEED
				.concat(INCIDENT_ITEM).concat("/")
				.concat("mode")
			, "item"
		);
		
		doStructureViaREST(
			PATH
			, CONTENT_TILE_SEED
			.concat(INCIDENT_ITEM).concat("/")
			.concat("type")
			, "news"
		);


/*
 * 2 add content attributes; e.g., 
 p = Articulos ...
 */
		doStructureViaREST(
			PATH
			, CONTENT_TILE_SEED
			.concat(INCIDENT_ITEM).concat("/")
			.concat("emergency-message-copy")
			, ee.html()
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

emergency-message-copy
*/