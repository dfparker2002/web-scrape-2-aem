/**
 * 
 */
package gov.sandiegocounty.util;


import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

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

	final static String OES_DATA_SRC = "C:\\xampp\\htdocs\\www.sdcountyemergency.com";
	static int cnt = 0;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
/*
 * TO DO
 * process elements, remove from top of stack to avoid nested, duplicated elems 
 * 
 */

		try {

			Path path = Paths.get(OES_DATA_SRC);
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
//files.forEach(x-> System.out.println( " JSoupParseOES :: main " + x.toString() ));
/*
 * test
 */
			int page_idx = 284; // home/index
			ParseFile p = new ParseFile();

System.out.println( " JSoupParseOES :: main FILE UT " + files.get(page_idx).toString());
Element e = p.doParseFile(files.get(page_idx));//284
//
////System.out.println( " JSoupParseOES :: main " + files.get(1).toString());
////e = p.doParseFile(files.get(1));//284
////System.out.println( " JSoupParseOES :: main " + files.get(2).toString());
////e = p.doParseFile(files.get(2));//284
////System.out.println( " JSoupParseOES :: main " + files.get(3).toString());
////e = p.doParseFile(files.get(3));//284
//
			p = null;

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
// here
/*
 * 0
 *  invoke parse on "en-us/about" page
 *  expect "About us" at /content/oes/en-us/about/jcr:content/parcontent/title/jcr:title
 */
/*
 * 1 
 */
//  	p.headTagCommand( path, content);
//		e = e.selectFirst( "div.map-instructions").parent().parent();
/*
 * 2
 */
		PageObj page = new PageObj();
		page.headTagCommand(
			page.parsePathParticle( files.get(page_idx).toFile() )
			, e
		);

		page.mapTagCommand(
			page.parsePathParticle( 
				files.get(page_idx).toFile() )
				, e
		);

//		page.processPage(files.get(page_idx).toFile(), e.getAllElements());
		page = null;
		p = null;
		e = null;

/*
 * 3
 */
// PageObj work ...

/*
 * 4
 */
// PageObj -> HttpURLConnectionRunner work ...

		} catch (IOException e) {

			e.printStackTrace();
		}

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

/*
 * TEST H*
 * 0 Call JSoupParseOES(file)] 
 * 1 -> invoke doParseFile
 * 2 --> parse content @ page center
 * 3 ---> invoke PageObj(title element)
 * 4 -----> invoke HttpURLConnectionRunner(title element, file/node path, internal_props_path )
 */
	// here
/*
 * 0
 *  invoke parse on "en-us/about" page
 *  expect "About us" at /content/oes/en-us/about/jcr:content/parcontent/title/jcr:title
 */
/*
 * 1 
 */
	Element tmp = content.selectFirst( "h1,h2,h3");
System.err.println( "sub elems " + tmp );
/*
 * 2
 */
	PageObj p = new PageObj();
	final String path = p.parsePathParticle(file.toFile());
	p.processPage(file.toFile(), tmp.getAllElements());

//	p.processPage(file.toFile(), tmp.getAllElements());
	p = null;
	tmp = null;

/*
 * 3
 */
	// PageObj work ...

/*
 * 4
 */
	// PageObj -> HttpURLConnectionRunner work ...
	
/////////////
System.err.println(file.toString());
//System.err.println("\n=== CONTENT\n");
//System.err.println(content.getAllElements().size() );
//System.err.println(content );
////System.err.println(content_row.size());
////System.out.println(content_row.html());
//System.out.println( "HEADER 0? " + content.selectFirst( "h1,h2,h3").text());
//System.out.println( "HEADER 1? " +  ( content.selectFirst( "h1,h2,h3,h4").getElementsByAttributeValueMatching("class", "art-PostHeader").isEmpty() ) );
//if( ( content.selectFirst( "h1,h2,h3,h4").getElementsByAttributeValueMatching("class", "art-PostHeader").isEmpty() ) ) {
//
////	System.out.println( "HEADER 2? " + content.selectFirst( "h1,h2,h3,h4") );
//	System.out.println( " JSoupParseOES.ParseFile :: doParseFile " +
//		content.selectFirst( "h1,h2,h3,h4").parent()
////content.before(content.selectFirst( "h1,h2,h3,h4"))
//);
//	System.out.println( "HEADER 2? " + content.getAllElements().select( "h1,h2,h3,h4").prev("div").html() );
//}
//
//
//System.err.println("=== END CONTENT\n");


//System.err.printf("\n=== RIGHT RAIL\n %d\n%s\n=== END RIGHT RAIL\n", right_railcontent.size(), right_railcontent.html());
/* TODO 
	PageObj pObj = new PageObj();
//Elements e = doc.select("div.container > div.row");
	pObj.processPage(file.toFile(), content.getAllElements() );
*/
		return content;
		}

	}

	static class ParseRightRailFromFile {

		public Element doParseFile( Path file ) throws IOException {
			
			Document doc;
/*
 * 
 */
			doc = Jsoup.parse( new File(file.toString() ) , "UTF-8");

			Element right_rail 		= doc.selectFirst( "div.col-sm-3");
			Elements right_railcontent	= right_rail.select( "div.row");
			String commented_right_rail = (">> TBD: common rail components - add to template <<")
				.concat( new Comment( right_railcontent.html() ).toString() );


			final Map<String, String> manifest = new TreeMap<>();
			Elements test = new Elements();
			try {

				test = right_rail.getElementsByClass("no-value");

			} catch (NullPointerException e) {

			}

			if( null != test && test.size() > 0 ) {
				right_rail.select("div.xrm-attribute.no-value").remove();
			}

		return right_rail;
		}

	}

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