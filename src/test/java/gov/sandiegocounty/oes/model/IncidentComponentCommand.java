/**
 * 
 */
package gov.sandiegocounty.oes.model;

import java.text.ParseException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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
				final Element datetime = x.select("div.col-sm-12").get(1); // 2nd position value; extract text
				final Element asl_link = getStringFromElement( x, "div.asl_link").get();

////////////
				Elements remainder = x.getAllElements().select("div.page-copy").first().getElementsByAttributeValue("class", "xrm-attribute-value");//.not("");

///////////
// remove scripts
				remainder.select("div.addthis_toolbox").next().remove();
				remainder.select("div.addthis_toolbox").remove();
//////////

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
					, asl_link.select("a").attr("href")
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
 * red-flag-warning-extended-through-to-wednesday-11-14-at-5-00pm-111218-1100
 * 2 add content attributes; e.g., 
 p = Articulos ...
 */
		Document n = Jsoup.parse(ee.html());
		removeComments(n);
System.out.println( " >>>> IncidentComponentCommand :: doNodesCreation " + n.select("body").first().html());
		doStructureViaREST(
			PATH
			, CONTENT_TILE_SEED
			.concat(INCIDENT_ITEM).concat("/")
			.concat("emergency-message-copy")
			, n.select("body").first().html()
		);

	}

    /**
     * @param node
     */
    private static void removeComments(Node node) {

    	for (int i = 0; i < node.childNodes().size();) { 
            Node child = node.childNode(i);

            if (child.nodeName().equals("#comment")) {
System.out.println( " <<< IncidentComponentCommand :: removeComments : removing " + child.outerHtml() );
                child.remove(); 
            }else { 
                removeComments(child); 
                i++; 
            } 
        } 
    }

    private static Optional<Element> getStringFromElement(Element nodeContent, String selector) {
        try {

        	if(StringUtils.isEmpty(selector)) {

        		return Optional.of(nodeContent);
        	} else {

        		return Optional.of(nodeContent.select(selector).first());
        	}

        } catch (Exception e) {
        	log.error(" Could not extract ASL LINK from HTML", e);
        }
        return Optional.empty();
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