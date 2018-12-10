/**
 * 
 */
package gov.sandiegocounty.util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.sandiegocounty.oes.model.PageObj;


/**
 * @author David Parker (dfparker@aemintegrators.com)
 *
 *	Refs
 *		https://stackoverflow.com/questions/2056221/recursively-list-files-in-java#24006711
 *		https://stackoverflow.com/questions/20987214/recursively-list-all-files-within-a-directory-using-nio-file-directorystream
 *		https://kodejava.org/how-to-recursively-list-all-text-files-in-a-directory/
 *		https://www.javabrahman.com/quick-tips/java-recursively-list-files-subdirectories-directory-examples/
 */
public class JSoupParseOES {

	
	private static final Logger log = LoggerFactory.getLogger(JSoupParseOES.class);

//	final static String OES_DATA_SRC = "C:\\xampp\\htdocs\\www.sdcountyemergency.com";
//	NORMAL
//	final static String OES_DATA_SRC = "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com";
//	UPDATES ONLY
//	final static String OES_DATA_SRC = "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com\\updates";
//	final static String OES_DATA_SRC = "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com\\es-us\\updates\\52-magntiude-earthquake-felt-in-san-diego-county-061016-0135";
	final static String OES_DATA_SRC = "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com\\es-us\\updates";
//	final static String OES_DATA_SRC = "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com\\updates\\3.6-earthquake-hits-san-diego-county";
//	final static String OES_DATA_SRC = "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com\\updates\\alertsandiego-evacuation-order-issued-to-an-additional-3500-contacts-120717-1919";
//	ONE UPDATE ONLY
//	final static String OES_DATA_SRC = "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com\\updates\\active-shooter-at-san-diego-rockn-roll-marathon-060318-1325";
//	final static String OES_DATA_SRC = "C:\\Users\\User\\workspace\\oesshared\\website\\www.sdcountyemergency.com\\updates\\lilac-fire-update-120917-2329";
	
	static int elem_cnt = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
/*
 * TO DO
 * process elements, remove from top of stack to avoid nested, duplicated elems 
 * 
 */
		Path x = null;
		try(FileWriter fw = new FileWriter("processing-errs.txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw)
			) { // https://stackoverflow.com/questions/30307382/how-to-append-text-to-file-in-java-8-using-specified-charset#30307562
			// https://stackoverflow.com/questions/1625234/how-to-append-text-to-an-existing-file-in-java
			//https://stackoverflow.com/questions/13777336/how-to-append-an-exceptions-stacktrace-into-a-file-in-java


			Path path = Paths.get( OES_DATA_SRC );
			final List<Path> files = new ArrayList<>();

			Files.walkFileTree(path,

					new SimpleFileVisitor<Path>() {

						@Override
						public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) 
							throws IOException {

							if (!attrs.isDirectory() && String.valueOf(file).endsWith(".html")) {

								files.add(file);
							}
							return FileVisitResult.CONTINUE;

						}
					});


			/////////////////////
/*
 * Process ALL Pages
 */
			final ParseFile p = new ParseFile();

			int cnt = 0;
			for (Iterator<Path> iterator = files.iterator(); iterator.hasNext();) {
				x = iterator.next();

if( 
	!x.endsWith("es-us\\index.html")
	&& !x.endsWith("en-us\\index.html")
	&& x.toFile().getPath().contains("updates")

//	&& x.endsWith("donations\\index.html") // testing
//	&& x.endsWith("active-shooter-at-san-diego-rockn-roll-marathon-060318-1325\\index.html") // testing
) {
//			files.forEach(x-> {

//				try{ 
					Element e = p.doParseFile(x);

// TEST various pages explicitly 
////System.out.println( " JSoupParseOES :: main " + files.get(1).toString());
////e = p.doParseFile(files.get(1));//284

//////////////////

//////////////////
/*
 * TEST H*
 * 0 Call JSoupParseOES(file)] 
 * 1 -> invoke doParseFile
 * 2 --> parse content @ page center
 * 3 ---> invoke PageObj(title element)
 * 4 -----> invoke HttpURLConnectionRunner(title element, file/node path, internal_props_path )
 */

			PageObj page = new PageObj();
			if( null != e 
				&& null != e.getAllElements() 
//				&& !e.attributes().asList().isEmpty()
			) {

//				e.attributes().asList().stream()
//				.filter( o -> o != null )
//				.map(Attribute::getValue).collect(Collectors.toList() );
//			}
//System.out.println( " e " + e.getAllElements() );
System.out.printf( " <<< "
	+ "x.toFile() %s"
	+ ", e.getAllElements().size = %d "
	+ ", cnt = %d\n"
	, x.toFile().getPath()
	, e.getAllElements().size()
	, cnt
);


					page.processPage(
						x.toFile(), e.getAllElements()
					);


//			} catch (IOException e1) {
//
//
//} catch (IOException e) {
//    //exception handling left as an exercise for the reader
//}
//	Files.write(Paths.get("processing-errs.txt"), x.toAbsolutePath().concat(" :: ").concat( e1.getMessage() ).getBytes() );

//} catch (IOException e2) {
//
//	e2.printStackTrace();
//}
//
//				e1.printStackTrace();
//			}

//});// end page loop
				}// end if null elements check
				}// end if home check
cnt++;
			} // end page loop

			} catch (IOException e) {

			    try(Writer w = new FileWriter( "processing-errs.txt", true)) {

System.out.println( " JSoupParseOES :: main " + e.getMessage() );
System.out.println( " JSoupParseOES :: main " + ( null == Paths.get("processing-errs.txt")) );
Files.write( Paths.get("processing-errs.txt"), "".concat(" :: ").concat( e.getMessage() ).getBytes() );
//	x.toAbsolutePath().concat(" :: ").concat( 
//	e.getMessage().getBytes();
e.printStackTrace(new PrintWriter(new BufferedWriter(w)));
log.error( " :: err {}", e.getMessage() );

			    } catch (IOException e1) {

					e1.printStackTrace();
				}

				e.printStackTrace();
			}
			//// end process all pages ///////////


		
/////////////////////////////////
//files.forEach(x-> {
//// LIST AVAIL FILES
//	System.out.printf( " \nJSoupParseOES :: main %d %s", elem_cnt++, x.toString() 
//	);
//}
//);

//			/////////////////////
///*
// * test one page
// */
////			int page_idx = 284; // home/index
//			int page_idx = 3; // donations/index
////			int page_idx = 138; // about/index
//			ParseFile p = new ParseFile();
//
//System.out.println( " JSoupParseOES :: main FILE UT " + files.get(page_idx).toString());
//Element e = p.doParseFile(files.get(page_idx));
//
//// TEST various pages explicitly 
//////System.out.println( " JSoupParseOES :: main " + files.get(1).toString());
//////e = p.doParseFile(files.get(1));//284
//
//			p = null;
//
////////////////////
//
////////////////////
///*
// * TEST H*
// * 0 Call JSoupParseOES(file)] 
// * 1 -> invoke doParseFile
// * 2 --> parse content @ page center
// * 3 ---> invoke PageObj(title element)
// * 4 -----> invoke HttpURLConnectionRunner(title element, file/node path, internal_props_path )
// */
//
//		PageObj page = new PageObj();
//e.attributes().asList().stream().map(Attribute::getValue).collect(Collectors.toList());
//
//			page.processPage(
//				files.get(page_idx).toFile(), e.getAllElements()
//			);
//
//
//		page = null;
//		p = null;
//		e = null;

/*
 * 3
 */
// PageObj work ...

/*
 * 4
 */
// PageObj -> HttpURLConnectionRunner work ...


	}


	/**
	 * FindTextFilesVisitor.
	 * src: https://kodejava.org/how-to-recursively-list-all-text-files-in-a-directory/
	 */
	static class FindTextFilesVisitor extends SimpleFileVisitor<Path> {
		@Override
		public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

			if (file.toString().endsWith(".html")) {
				System.out.println(file.toUri() );
//				System.out.println(file.getFileName());
			}
			return FileVisitResult.CONTINUE;
		}
	}

	static class ParseFile {

		public Element doParseFile( Path file ) throws IOException {
			
			Document doc;
/*
 * 
 */
			
			doc = Jsoup.parse( new File(file.toString() ) , "UTF-8");

			Element misc	 		= doc.selectFirst( "div.page-header > h1");
			Element content 		= doc.selectFirst( "div.container > div.row > div.col-sm-9");
//			Elements content_row	= content.select("div.xrm-editable-html.xrm-attribute > div.xrm-attribute-value");//,div.row,div.oes-box


			final Map<String, String> manifest = new TreeMap<>();
			Elements test = new Elements();
			try {

				test = content.getElementsByClass("no-value");

			} catch (NullPointerException e) {

			}
			if( null != test && test.size() > 0 ) {
				content.select("div.xrm-attribute.no-value").remove();
			}

System.err.println( "clean results");
//System.err.println( "content " + content.selectFirst("h1,h2,h3,h4").text() );
if( misc != null && !misc.getAllElements().isEmpty() ) {
	content = content.insertChildren(0, misc);
}

/*
System.out.println("** o 0 content " + content.children().size() );
content = content.insertChildren(0, misc);
System.out.println("** o 1 content " + content.children().size() );
System.out.println("** o 2 content " + content );
*/

/*
 * TEST H*
 * 0 Call JSoupParseOES(file)] 
 * 1 -> invoke doParseFile
 * 2 --> parse content @ page center
 * 3 ---> invoke PageObj(title element)
 * 4 -----> invoke HttpURLConnectionRunner(title element, file/node path, internal_props_path )
 */

/*
 * 4
 */
	// PageObj -> HttpURLConnectionRunner work ...
	
/////////////
System.err.println(file.toString());

		return content;
		}

	}

//	static class ParseRightRailFromFile {
//
//		public Element doParseFile( Path file ) throws IOException {
//			
//			Document doc;
///*
// * 
// */
//			doc = Jsoup.parse( new File(file.toString() ) , "UTF-8");
//
//			Element right_rail 		= doc.selectFirst( "div.col-sm-3");
//			Elements right_railcontent	= right_rail.select( "div.row");
//			String commented_right_rail = (">> TBD: common rail components - add to template <<")
//				.concat( new Comment( right_railcontent.html() ).toString() );
//
//
//			final Map<String, String> manifest = new TreeMap<>();
//			Elements test = new Elements();
//			try {
//
//				test = right_rail.getElementsByClass("no-value");
//
//			} catch (NullPointerException e) {
//
//			}
//
//			if( null != test && test.size() > 0 ) {
//				right_rail.select("div.xrm-attribute.no-value").remove();
//			}
//
//		return right_rail;
//		}
//
//	}

	/**
	 * @param node
	 */
	private static void walkTree(Node node, String tgt) {
		for (int i = 0; i < node.childNodeSize();) {

			Node child = node.childNode(i);
			if (child.nodeName().equals(tgt)) {
//				child.remove(); // do something

			} else {

				walkTree(child, tgt);
				i++;
			}
		}
	}

}